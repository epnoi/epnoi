package epnoi;

import junit.framework.Assert;

import org.junit.Test;

public class ProofTest {

	@Test
	public void testPasses() {
		String expected = "Hello, JUnit!";
		String hello = "Hello, JUnit!";
		Assert.assertEquals(hello, expected);
	}


}
