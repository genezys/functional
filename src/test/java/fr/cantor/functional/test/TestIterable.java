package fr.cantor.functional.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.cantor.functional.Iterable;
import fr.cantor.functional.IterableArray;
import fr.cantor.functional.Iterator;
import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.functions.Identity;
import fr.cantor.functional.functions.methods.Method0;
import fr.cantor.functional.functions.operators.IntegerAdd;
import fr.cantor.functional.functions.operators.IntegerMultiply;
import fr.cantor.functional.functions.predicates.FalsePredicate1;
import fr.cantor.functional.functions.predicates.Predicate1;
import fr.cantor.functional.functions.predicates.TruePredicate1;
import fr.cantor.functional.nuple.Pair;

public class TestIterable
{
	@Test
	public void testCombineSameSize() throws FunctionalException
	{
		final Iterable<String> strings1 = array("plop1", "onk1");
		final Iterable<String> strings2 = array("plop2", "onk2");
		final Iterator<Pair<String, String>> combined = strings1.combine(strings2).iterator();
		
		Assert.assertTrue(combined.hasNext());
		Pair<String, String> pair;
		pair = combined.next();
		Assert.assertEquals("plop1", pair.first);
		Assert.assertEquals("plop2", pair.second);
		
		Assert.assertTrue(combined.hasNext());
		pair = combined.next();
		Assert.assertEquals("onk1", pair.first);
		Assert.assertEquals("onk2", pair.second);
		
		Assert.assertFalse(combined.hasNext());
	}
	
	@Test
	public void testCombineSmaller() throws FunctionalException
	{
		final Iterable<String> strings1 = array("plop1", "onk1");
		final Iterable<String> strings2 = array("plop2");
		final Iterator<Pair<String, String>> combined = strings1.combine(strings2).iterator();
		
		Assert.assertTrue(combined.hasNext());
		Pair<String, String> pair;
		pair = combined.next();
		Assert.assertEquals("plop1", pair.first);
		Assert.assertEquals("plop2", pair.second);
		
		Assert.assertTrue(combined.hasNext());
		pair = combined.next();
		Assert.assertEquals("onk1", pair.first);
		Assert.assertNull(pair.second);
		
		Assert.assertFalse(combined.hasNext());
	}

	@Test
	public void testCombineLarger() throws FunctionalException
	{
		final Iterable<String> strings1 = array("plop1");
		final Iterable<String> strings2 = array("plop2", "onk2");
		final Iterator<Pair<String, String>> combined = strings1.combine(strings2).iterator();
		
		Assert.assertTrue(combined.hasNext());
		Pair<String, String> pair;
		pair = combined.next();
		Assert.assertEquals("plop1", pair.first);
		Assert.assertEquals("plop2", pair.second);
		
		Assert.assertTrue(combined.hasNext());
		pair = combined.next();
		Assert.assertNull(pair.first);
		Assert.assertEquals("onk2", pair.second);
		
		Assert.assertFalse(combined.hasNext());
	}
	
	@Test
	public void testEquals() throws FunctionalException
	{
		Assert.assertTrue(array("plop", "onk").equals(array("plop", "onk")));
	}
	
	@Test
	public void testDump() throws FunctionalException
	{
		List<String> astr = array("plop", "onk").dump(new ArrayList<String>());
		Assert.assertEquals(2, astr.size());
		Assert.assertEquals("plop", astr.get(0));
		Assert.assertEquals("onk", astr.get(1));
	}
	
	@Test
	public void testJoin() throws FunctionalException
	{
		Assert.assertEquals("plop,onk,gloubi", array("plop", "onk", "gloubi").join(","));
	}
	
	@Test
	public void testInject() throws FunctionalException
	{
		Iterable<Integer> numbers = array(1, 2, 3, 4, 5, 6);
		Assert.assertEquals(Integer.valueOf(21), numbers.inject(0, new IntegerAdd()));
		Assert.assertEquals(Integer.valueOf(720), numbers.inject(new IntegerMultiply()));
	}
	
	@Test
	public void testWithIndex() throws FunctionalException
	{
		Iterable<Integer> numbers = array(5, 4, 3, 2, 1);
		Iterator<Pair<Integer, Integer>> numbersWithIndex = numbers.withIndex().iterator();
		Assert.assertEquals(new Pair<Integer, Integer>(0, 5), numbersWithIndex.next());
		Assert.assertEquals(new Pair<Integer, Integer>(1, 4), numbersWithIndex.next());
		Assert.assertEquals(new Pair<Integer, Integer>(2, 3), numbersWithIndex.next());
		Assert.assertEquals(new Pair<Integer, Integer>(3, 2), numbersWithIndex.next());
		Assert.assertEquals(new Pair<Integer, Integer>(4, 1), numbersWithIndex.next());
		Assert.assertFalse(numbersWithIndex.hasNext());
	}
	
	@Test
	public void testAllAny() throws FunctionalException
	{
		Iterable<Integer> numbers = array(1, 2, 3, 4, 5, 6);
		Assert.assertFalse(numbers.all(new Predicate1<Integer>()
		{
			public Boolean call(Integer n) throws FunctionalException
			{
				return n <= 5;
			}
		}));
		Assert.assertTrue(numbers.all(new Predicate1<Integer>()
		{
			public Boolean call(Integer n) throws FunctionalException
			{
				return n <= 6;
			}
		}));
		Assert.assertFalse(numbers.any(new Predicate1<Integer>()
		{
			public Boolean call(Integer n) throws FunctionalException
			{
				return n > 6;
			}
		}));
		Assert.assertTrue(numbers.any(new Predicate1<Integer>() 
		{
			public Boolean call(Integer n) throws FunctionalException
			{
				return n > 5;
			}
		}));
	}
	
	@Test
	public void testFirst() throws FunctionalException
	{
		Assert.assertEquals(null, array().first());
		Assert.assertEquals("plop", array("plop", "onk").first());
		Assert.assertEquals("plop", array("plop", "onk").select(new TruePredicate1<String>()).first());
		Assert.assertEquals(null, array("plop", "onk").select(new FalsePredicate1<String>()).first());
		Assert.assertEquals("Onk", array("Plop", "Onk", "plop", "onk").select(new Predicate1<String>()
		{
			public Boolean call(String str) throws FunctionalException
			{
				return str.substring(0, 1).toUpperCase().equals("O");
			}
		}).first());
	}
	
	@Test
	public void testMap() throws FunctionalException, SecurityException, NoSuchMethodException
	{
		Assert.assertEquals(array("plop", "onk"), array("plop", "onk").map(new Identity<String>()));
		Assert.assertEquals(array("PLOP", "ONK"), array("plop", "onk").map(new Method0<String, String>("toUpperCase")));
	}
	
	@Test
	public void testSelect() throws FunctionalException
	{
		Assert.assertEquals(array("plop", "onk"), array("plop", "onk").select(new TruePredicate1<String>()));
		Assert.assertFalse(array("plop", "onk").select(new FalsePredicate1<String>()).iterator().hasNext());
		Assert.assertEquals(array("Plop", "plop"), array("Plop", "Onk", "plop", "onk").select(new Predicate1<String>()
		{
			public Boolean call(String str) throws FunctionalException
			{
				return str.substring(0, 1).toUpperCase().equals("P");
			}
		}));
	}
	
	private <T> Iterable<T> array(T... values)
	{
		return new IterableArray<T>(values);
	}
}
