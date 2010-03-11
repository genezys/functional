package fr.cantor.functional;

import junit.framework.Test;
import junit.framework.TestSuite;
import fr.cantor.functional.test.TestEasierIterator;
import fr.cantor.functional.test.TestIdentity;
import fr.cantor.functional.test.TestIterable;
import fr.cantor.functional.test.TestPrimeNumbers;
import fr.cantor.functional.test.TestRange;

public class AllTests extends TestSuite 
{
	public static Test suite() 
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestEasierIterator.class);
		suite.addTestSuite(TestIdentity.class);
		suite.addTestSuite(TestIterable.class);
		suite.addTestSuite(TestPrimeNumbers.class);
		suite.addTestSuite(TestRange.class);
		return suite;
	}
	
}
