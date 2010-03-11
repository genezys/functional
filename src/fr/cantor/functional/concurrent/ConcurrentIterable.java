package fr.cantor.functional.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.cantor.functional.Iterable;
import fr.cantor.functional.Iterator;
import fr.cantor.functional.Range;
import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.exceptions.FunctionalRuntimeException;
import fr.cantor.functional.functions.Function1;
import fr.cantor.functional.functions.Injecter;

public class ConcurrentIterable<T> extends Iterable<T>
{
	private Iterable<T> m_iterable;
	private int m_countThreads;

	public ConcurrentIterable(Iterable<T> iterable)
	{
		this(iterable, Runtime.getRuntime().availableProcessors());
	}
	
	public ConcurrentIterable(Iterable<T> iterable, int countThreads)
	{
		m_iterable = iterable;
		m_countThreads = countThreads;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return m_iterable.iterator();
	}
	
	/**
	 * Override the implementation of inject in order to run concurrently every algorithm
	 * based on it: any(), all(), dump(), join(), etc.
	 */
	@Override
	protected <V> V injectWithIterator(Iterator<T> it, V value, Injecter<V,T> injecter) 
	{
		ExecutorService executor = Executors.newFixedThreadPool(m_countThreads);
		for ( int i = 0; i < m_countThreads; i += 1 ) 
		{
			executor.execute(new Iteration<V, T>(injecter, value, it));
		}
		executor.shutdown();
		
		try
		{
			executor.awaitTermination(365, TimeUnit.DAYS); // Infinite ?
		}
		catch ( InterruptedException e )
		{
			// Nothing?
		}
		return value;
	}
	
	private static class Iteration<V, T> implements Runnable
	{
		private Injecter<V, T> m_injecter;
		private V m_result;
		private Iterator<T> m_iterator;
	
		private Iteration(Injecter<V, T> injecter, V result, Iterator<T> iterator)
		{
			m_injecter = injecter;
			m_result = result;
			m_iterator = iterator;
		}
	
		public void run()
		{
			while( true )
			{
				try
				{
					T next = m_iterator.next();
					synchronized( m_result )
					{
						try
						{
							m_result = m_injecter.call(m_result, next);
						}
						catch ( FunctionalRuntimeException e ) 
						{
							throw e;
						}
						catch ( Exception e ) 
						{
							throw new FunctionalRuntimeException(e);
						}
					}
				}
				catch ( NoSuchElementException e )
				{
					break;
				}
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws Exception 
	{
		for( int i = 0; i < 10; ++i )
		{
			test();
		}
	}
	
	public static void test() throws FunctionalException
	{
		// Iterated on all numbers from 0 to 999 and transform them to words 
		// adding some free time to simulate a CPU yield action like a disk IO 
		Range range = new Range(100000);
		Iterable<Integer> integers = Iterable.wrap(range.dump(new ArrayList<Integer>()));
		final Iterable<String> it = integers.map(new Function1<String, Integer>() 
		{
			public String call(Integer n) throws FunctionalException
			{
				return String.format("%09d", n)
				.replaceAll("0", " Zero ")
				.replaceAll("1", " One ")
				.replaceAll("2", " Two ")
				.replaceAll("3", " Three ")
				.replaceAll("4", " Four ")
				.replaceAll("5", " Five ")
				.replaceAll("6", " Six ")
				.replaceAll("7", " Seven ")
				.replaceAll("8", " Eight ")
				.replaceAll("9", " Nine ")
				.trim()
				;
			}
		});
		
		final List<String> list1 = new ArrayList<String>(100000);
		final List<String> list2 = new ArrayList<String>(100000);
				
		System.out.println("single-threaded: " + profile(new Runnable()
		{
			public void run()
			{
				try
				{
					it.dump(list1);
				}
				catch ( FunctionalException e )
				{
					throw new FunctionalRuntimeException(e);
				}
			}
		}));
		System.out.println("multi-threaded:  " + profile(new Runnable()
		{
			public void run()
			{
				try
				{
					it.concurrently().dump(list2);
				}
				catch ( FunctionalException e )
				{
					throw new FunctionalRuntimeException(e);
				}
			}
		}));
		
		Collections.sort(list1);
		Collections.sort(list2);
		
		if ( list1.equals(list2) )
		{
			return;
		}
		else
		{
			System.out.println("Searching for error");
			for ( int i = 0; i < list1.size(); i++ )
			{
				String str1 = list1.get(i);
				String str2 = list2.get(i);
				if ( !str1.equals(str2) )
				{
					System.out.println(i);
					System.out.print(list1.get(i - 1));
					System.out.print("\t");
					System.out.println(list2.get(i - 1));
					System.out.print(str1);
					System.out.print("\t");
					System.out.println(str2);
					System.out.print(list1.get(i + 1));
					System.out.print("\t");
					System.out.println(list2.get(i + 1));
					throw new RuntimeException("Multi threaded iteration failed");
				}
			}
		}
	}
	
	private static long profile(Runnable r)
	{
		long begin = System.nanoTime();
		r.run();
		long duration = System.nanoTime() - begin;
		return duration / 1000;
	}
}
