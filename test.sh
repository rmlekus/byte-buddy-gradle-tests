#!/bin/sh

export JAVA_HOME=`/usr/libexec/java_home -v 11`                   
export PATH="${JAVA_HOME}/bin:${PATH}"

CURRENT_DIR=$PWD
export GRADLE_USER_HOME="${CURRENT_DIR}/gradle-user-home"
mkdir -p "${GRADLE_USER_HOME}"

(
	cd gradle-7/empty-source-dir-incremental-test && \
		./test-incremental-builds.sh
)

(
	cd gradle-7/raw-classes-dir-handling-test   && \
		./gradlew clean build
)

(
	cd gradle-5/raw-classes-dir-handling-test && \
		./gradlew clean build
)



