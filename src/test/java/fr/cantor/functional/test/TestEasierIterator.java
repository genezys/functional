package fr.cantor.functional.test;

import org.junit.Assert;
import org.junit.Test;

import fr.cantor.functional.EasierIterator;
import fr.cantor.functional.Iterator;
import fr.cantor.functional.exceptions.FunctionalException;

public class TestEasierIterator
{
	@Test
	public void testEmptyIterator()
	{
		Iterator<Integer> iterator = new EasierIterator<Integer>()
		{
			@Override
			protected Integer getCurrent() throws FunctionalException
			{
				return null;
			}
			@Override
			protected boolean moveNext() throws FunctionalException
			{
				return false;
			}
		};
		
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
	}
	
	public void testSimple()
	{
		Iterator<Integer> iterator = new EasierIterator<Integer>()
		{
			@Override
			protected Integer getCurrent() throws FunctionalException
			{
				return 1000 + m_count;
			}
			@Override
			protected boolean moveNext() throws FunctionalException
			{
				m_count += 1;
				return m_count < 3;
			}
			int m_count = 0;
		};
		
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(Integer.valueOf(1001), iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(Integer.valueOf(1002), iterator.next());
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
	}
}
