package fr.cantor.functional.test;

import fr.cantor.functional.functions.Identity;
import junit.framework.TestCase;

public class TestIdentity extends TestCase
{
	public void testIdentity() throws Exception
	{
		assertEquals(null, new Identity<Object>().call(null));
		assertEquals(Integer.valueOf(3), new Identity<Integer>().call(3));
	}
}
