package fr.cantor.functional;

import fr.cantor.functional.test.TestIdentity;
import fr.cantor.functional.test.TestIterable;
import fr.cantor.functional.test.TestPrimeNumbers;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite 
{
	public static Test suite() 
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestIdentity.class);
		suite.addTestSuite(TestIterable.class);
		suite.addTestSuite(TestPrimeNumbers.class);
		return suite;
	}
	
}
