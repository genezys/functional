package fr.cantor.functional.test;

import org.junit.Assert;
import org.junit.Test;

import fr.cantor.functional.functions.Identity;

public class TestIdentity
{
	@Test
	public void testIdentity() throws Exception
	{
		Assert.assertEquals(null, new Identity<Object>().call(null));
		Assert.assertEquals(Integer.valueOf(3), new Identity<Integer>().call(3));
	}
}
