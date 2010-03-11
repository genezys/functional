package fr.cantor.functional.test;

import java.util.Iterator;

import junit.framework.TestCase;
import fr.cantor.functional.Range;

public class TestRange extends TestCase
{
	public void testCountEmpty()
	{
		Iterator<Integer> iterator = new Range(0).iterator();
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}
	
	public void testCountMany()
	{
		Iterator<Integer> iterator = new Range(3).iterator();
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(0), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(2), iterator.next());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}

	public void testRangeMany()
	{
		Iterator<Integer> iterator = new Range(3, 5).iterator();
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(3), iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(5), iterator.next());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}
}
