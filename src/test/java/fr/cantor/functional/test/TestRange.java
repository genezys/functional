package fr.cantor.functional.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import fr.cantor.functional.Range;

public class TestRange
{
	@Test
	public void testCountEmpty()
	{
		Iterator<Integer> iterator = new Range(0).iterator();
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testCountMany()
	{
		Iterator<Integer> iterator = new Range(3).iterator();
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(Integer.valueOf(0), iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(Integer.valueOf(1), iterator.next());
		Assert.assertEquals(Integer.valueOf(2), iterator.next());
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testRangeMany()
	{
		Iterator<Integer> iterator = new Range(3, 5).iterator();
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(Integer.valueOf(3), iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(Integer.valueOf(4), iterator.next());
		Assert.assertEquals(Integer.valueOf(5), iterator.next());
		Assert.assertFalse(iterator.hasNext());
		Assert.assertFalse(iterator.hasNext());
	}
}
