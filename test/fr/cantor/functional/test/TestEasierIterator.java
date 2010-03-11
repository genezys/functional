package fr.cantor.functional.test;

import junit.framework.TestCase;
import fr.cantor.functional.EasierIterator;
import fr.cantor.functional.Iterator;
import fr.cantor.functional.exceptions.FunctionalException;

public class TestEasierIterator extends TestCase
{
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
		
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
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
		
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(1001), iterator.next());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(1002), iterator.next());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}
}
