package com.synaptik.athena;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

public class AthenaTestResultTests extends TestCase {

	public void testParse() throws Exception {
		final String DATA = 
			"Failure in testMethod:\n"+
			"junit.framework.AssertionFailedError: Unexpected exception\n" +
			"\tat org.junit.Assert.fail(Assert.java:74)\n" +
			"\tat UnexpectedExceptionTest1.testGet(Unknown Source)\n" +
			"\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n";
		AthenaTestResult atr = new AthenaTestResult();
		atr.parse(DATA, null);
		
		assertEquals("junit.framework.AssertionFailedError", atr.failureClass);
		assertEquals("Unexpected exception", atr.failure);
		assert(atr.failureException.startsWith("junit.framework.AssertionFailedError: Unexpected"));
	}
	
	public void testParse_NoMessage() throws Exception {
		final String DATA = 
			"Failure in testMethod:\n"+
			"junit.framework.AssertionFailedError:\n" +
			"\tat org.junit.Assert.fail(Assert.java:74)\n" +
			"\tat UnexpectedExceptionTest1.testGet(Unknown Source)\n" +
			"\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n";
		AthenaTestResult atr = new AthenaTestResult();
		atr.parse(DATA, null);
		
		assertEquals("junit.framework.AssertionFailedError", atr.failureClass);
		assertEquals("", atr.failure);
		//assertEquals(DATA, atr.failureException);
		assert(atr.failureException.startsWith("junit.framework.AssertionFailedError: Unexpected"));
		
	}
	
	public void testParse_Compare() throws Exception {
		
		AthenaTestResult atr = new AthenaTestResult();
		String DATA = ReadStringFromFile ("testData/FailedCompare.txt");

		atr.parse(DATA, null);
		assertEquals("junit.framework.ComparisonFailure", atr.failureClass);
		assertEquals("Multiply Decimal Values expected:<...29...> but was:<...8...>", atr.failure);
		//assertEquals(DATA, atr.failureException);
		assert(atr.failureException.startsWith("junit.framework.ComparisonFailure: Multiply"));

		
		//assertNotNull(atr.failure);

	}	
	
	public void testGetTestNameFromString() throws Exception {
		assertEquals("testMyTest", AthenaTestResult.getTestNameFromString("testMyTest(com.synaptik)"));
		assertEquals(null, AthenaTestResult.getTestNameFromString(null));
	}
	
	public void testGetClassNameFromString() throws Exception {
		assertEquals("com.synaptik", AthenaTestResult.getClassNameFromString("testMyTest(com.synaptik)"));
		assertEquals(null, AthenaTestResult.getClassNameFromString(null));
	}
	
	public void testEscapeHTML() throws Exception {
		assertEquals("&lt;&gt;&quot;ABC", AthenaTestResult.escapeHTML("<>\"ABC"));
		assertEquals(null, AthenaTestResult.escapeHTML(null));
	}
	


	public void testFileEx () throws Exception {
		//File root = new File("testData/coco");
        //System.out.println(root.getAbsolutePath());
		String tString = ReadStringFromFile ("testData/TestString.txt");
		assertEquals (tString, "Filler text");
		
	}
	/*Java doesn't support multi-line string literals, so here's a workaround */
	public String ReadStringFromFile(String fName) {
		StringBuilder b = new StringBuilder();
		try {
		FileReader fileIn = new FileReader(fName);
        BufferedReader buffIn = new BufferedReader(fileIn);
            String line = buffIn.readLine();
            while (line != null) {
                b.append(line);
                line = buffIn.readLine();
                if (line != null) b.append("\n");
            }
        }
        catch(IOException e){
        	fail ("Can't access resource file: "+fName);
        };
        return b.toString();
	}

}
