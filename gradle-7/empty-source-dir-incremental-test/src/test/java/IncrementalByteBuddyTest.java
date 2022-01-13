import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IncrementalByteBuddyTest {

	private static int maxVariantIndex;
	private static Set<Integer> expectedVariants; 
	private static Set<Integer> notExepctedVariants; 
	
	@BeforeAll
	static void beforeClass() {
		System.out.println("=== expectedVariants Property: " + System.getProperty("expectedVariants"));
		maxVariantIndex  = Integer.parseInt(System.getProperty("maxVariants", "3"));
		expectedVariants = Stream.of(System.getProperty("expectedVariants", "").split(","))
				  .map(String::trim)
				  .filter(index -> index.length()>0)
				  .map(Integer::parseInt)
				  .filter(index -> (index>0) && (index <= maxVariantIndex))
				  .collect(Collectors.toCollection(TreeSet::new));
		
		Integer[] allIndices = new Integer[maxVariantIndex];
		Arrays.setAll(allIndices, p -> p+1 );
		notExepctedVariants = new TreeSet<Integer>(Arrays.asList(allIndices));
		notExepctedVariants.removeAll(expectedVariants);
	}
	
	@Test
	void test() throws Exception {
		System.out.println("=== maxVariants:         " + maxVariantIndex);
		System.out.println("=== expectedVariants:    " + expectedVariants.toString());
		System.out.println("=== notExepctedVariants: " + notExepctedVariants.toString());

		StringBuilder str = new StringBuilder();
		for (int variant : expectedVariants) {
			str.append(checkHelloByteBuddyClass(variant, true)).append('\n');
		}
		
		for (int variant : notExepctedVariants) {
			checkHelloByteBuddyClass(variant, false);
		}
		System.out.println(str.toString());
	}

	private String checkHelloByteBuddyClass(int variantIndex, boolean expectClassExists) throws Exception {
		final String className = "HelloByteBuddy"+variantIndex;
		if (!expectClassExists) {
			try {
				Class.forName(className);
				fail("class "+className+" could be resolved, but must not exist");
			} catch (ClassNotFoundException e) {
				// as expected
				return null;
			}
		}
	
		final Class<?> helloByteBuddyClass = Class.forName(className);
		final Constructor<?> helloByteBuddyConstructor = helloByteBuddyClass.getConstructor();
		final Object helloByteBuddyInstance = helloByteBuddyConstructor.newInstance();
		String res =  helloByteBuddyInstance.toString();
		assertEquals(className+"{str=Hello from "+className+"!}", res);
		return res;
	}

}
