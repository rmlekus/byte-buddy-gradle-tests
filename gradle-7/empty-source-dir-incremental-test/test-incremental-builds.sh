#!/bin/sh

function error_exit {
    echo "$1" >&2   ## Send message to stderr. Exclude >&2 if you don't want it that way.
    exit "${2:-1}"  ## Return a code specified by $2 or 1 by default.
}

function printHeader {
	echo "=========================================================="
	echo "   $1"
	echo "=========================================================="
}

MAIN_SRC=src/main/java
ATTIC_SRC=src/attic/java
GRADLE_OPTS="--full-stacktrace --refresh-dependencies "
GRADLE_DEBUG_OPTS="--full-stacktrace --refresh-dependencies --debug -Dorg.gradle.debug=true --no-daemon"

# GRADLE_OPTS="--full-stacktrace --refresh-dependencies"
# GRADLE_DEBUG_OPTS="--full-stacktrace --refresh-dependencies"


# start with no sources in main
(
	printHeader "step 1: clean"
	rm -f ${MAIN_SRC}/*.java && \
		./gradlew ${GRADLE_OPTS} clean  || error_exit "project cleanup failed"
) 2>&1 | tee step-1-clean-with-no-sources.log

# initial/full build with no sources
(
	printHeader "step 2: initial empty "
	./gradlew -DexpectedVariants= ${GRADLE_OPTS} build  || error_exit "Initial build without sources failed, see: https://github.com/raphw/byte-buddy/issues/1157"
) 2>&1 | tee step-2-build-initially-with-no-sources.log

# incremental compile with one source
(
	printHeader "step 3: 1 "
	cp ${ATTIC_SRC}/HelloByteBuddy1.java ${MAIN_SRC} && \
		./gradlew -DexpectedVariants=1 ${GRADLE_OPTS} build  || error_exit "Incremental build with 1 source file failed"
) 2>&1 | tee step-3-build-with-first-source-added.log

# incremental compile with one more source
(
	printHeader "step 4: 1,2 "
	cp ${ATTIC_SRC}/HelloByteBuddy2.java ${MAIN_SRC} && \
		./gradlew -DexpectedVariants=1,2 ${GRADLE_OPTS} build || error_exit "Initial Build with one more sources failed"
)  2>&1 | tee step-4-build-with-added-source.log
# incremental compile with one more source
(
	printHeader "step 5: 2,3 "
	cp ${ATTIC_SRC}/HelloByteBuddy3.java ${MAIN_SRC} && \
		rm -f ${MAIN_SRC}/HelloByteBuddy1.java  && \
		./gradlew -DexpectedVariants=2,3 ${GRADLE_OPTS} build || error_exit "Initial Build with +/- one more source files failed"
) 2>&1 | tee step-5-build-with-added-and-removed-source.log

# start with a full/incremental build containing 1 java file
(
	printHeader "step 6:  final empty "
	rm -f ${MAIN_SRC}/*.java && \
		./gradlew -DexpectedVariants= ${GRADLE_OPTS} build || error_exit "Initial Build with +/- one more source files failed"
) 2>&1 | tee step-6-build-with-all-sources-removed.log
