package fr.cantor.functional.test;

import junit.framework.TestCase;
import fr.cantor.functional.Iterable;
import fr.cantor.functional.Range;
import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.functions.Injecter;
import fr.cantor.functional.functions.predicates.Predicate1;

public class TestPrimeNumbers extends TestCase 
{
	public void testPrimeNumbers() throws FunctionalException
	{
		Iterable<Integer> numbers = new Range(1, 100);
		Iterable<Integer> primes = numbers.inject(numbers, new Injecter<Iterable<Integer>, Integer>()
		{
			public Iterable<Integer> call(Iterable<Integer> numbers, final Integer number) throws FunctionalException
			{
				// We don't test for 1 which is implicit
				if ( number <= 1 )
				{
					return numbers;
				}
				// Only keep in numbers those that do not divide by number
				return numbers.reject(new Predicate1<Integer>()
				{
					public Boolean call(Integer n) throws FunctionalException
					{
						return n > number && n % number == 0;
					}
				});
			}
		});
		
		assertEquals("1,2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97", primes.join(","));
	}
}
