package fr.cantor.functional.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.cantor.functional.Functions;
import fr.cantor.functional.Iterable;
import fr.cantor.functional.Iterator;
import fr.cantor.functional.Range;
import fr.cantor.functional.Functions.Injecter;

public class ConcurrentIterator<T> extends Iterator<T> 
{
	/** 
	 * Utility function to easily dump concurrently an Iterable
	 * @param <T> Type to iterate on
	 * @param <C> Collection type
	 * @param iterable iterable to dump
	 * @param collection collection to dump into
	 * @return the collection argument filled with the iterable elements
	 */
	public static <T, C extends Collection<T>> C dump(Iterable<T> iterable, C collection)
	{
		return new ConcurrentIterator<T>(iterable.iterator()).dump(collection);
	}
	
	/** 
	 * Utility function to easily dump concurrently an Iterable
	 * @param <T> Type to iterate on
	 * @param <C> Collection type
	 * @param iterable iterable to dump
	 * @param collection collection to dump into
	 * @return the collection argument filled with the iterable elements
	 */
	public static <T, C extends Collection<T>> C dump(Iterable<T> iterable, int nBatchSize, C collection)
	{
		return new ConcurrentIterator<T>(iterable.iterator(), nBatchSize).dump(collection);
	}
	
	/**
	 * Wraps a standard Iterator 
	 * @param iterator
	 */
	public ConcurrentIterator(Iterator<T> iterator) 
	{
		this(iterator, 8);
	}
	
	/**
	 * Wrap a standard iterator and specify the number of parallel computing asked 
	 * @param iterator 
	 * @param nBatchSize number of tasks to run concurrently
	 */
	public ConcurrentIterator(Iterator<T> iterator, int nBatchSize) 
	{
		m_iterator = iterator;
		m_nBatchSize = nBatchSize;
	}

	/**
	 * Delegate Iterator methods to our internal iterator
	 */
	public boolean hasNext() 
	{
		return m_iterator.hasNext();
	}
	
	/**
	 * Delegate Iterator methods to our internal iterator
	 */
	public T next() 
	{
		return m_iterator.next();
	}
	
	
	/**
	 * Override the implementation of inject in order to run concurrently every algorithm
	 * based on it: any(), all(), dump(), join(), etc.
	 */
	@Override
	protected <V> V injectWithIterator(Iterator<T> it, V value, Functions.Injecter<V,T> injecter) 
	{
		final ExecutorService executor = Executors.newCachedThreadPool();
		final ArrayList<Future<V>> tasks = new ArrayList<Future<V>>();
		
		// Add a batch of task to start
		tasks.ensureCapacity(m_nBatchSize);
		for( int i = 0; i < m_nBatchSize; ++i )
		{
			tasks.add(submitIterationToExecutor(executor, it, value, injecter));
		}
		
		while ( true )
		{
			// Run the tasks
			Thread.yield();

			// Replaced finished tasks by new ones
			for ( int i = 0; i < tasks.size(); i++ ) 
			{
				Future<V> future = tasks.get(i);
				if( future != null && future.isDone() )
				{
					try
					{
						value = future.get();
						tasks.set(i, submitIterationToExecutor(executor, it, value, injecter));
					}
					catch( ExecutionException e )
					{
						if ( e.getCause() instanceof NoSuchElementException )
						{
							// One iteration has thrown, stop iteration
							executor.shutdown();
						}
					} 
					catch ( InterruptedException e ) 
					{
						throw new RuntimeException(e);
					}
				}
			}
			
			// If there is no task left, end of the injection
			if ( executor.isTerminated() )
			{
				return value;
			}
		}
	}
	
	private Iterator<T> m_iterator;
	private int m_nBatchSize;

	private <V> Future<V> submitIterationToExecutor(final ExecutorService executor, final Iterator<T> it, final V value, final Injecter<V, T> injecter) 
	{
		if ( executor.isShutdown() )
		{
			return null;
		}
		return executor.submit(new Callable<V>() 
		{
			public V call() throws Exception 
			{
				T next = it.next();
				synchronized ( value )
				{
					return injecter.call(value, next);
				}
			}
		});
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) 
	{
		// Iterated on all numbers from 0 to 999 and transform them to words 
		// adding some free time to simulate a CPU yield action like a disk IO 
		final Iterable<String> it = new Range(1000).map(new Functions.Function1<String, Integer>() 
		{
			public String call(Integer n) throws Exception 
			{
				Thread.sleep(1); // just doing some fast IO disk task ? 1ms pause
				return Integer.toString(n)
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
		
		// Ask for 100 parallel tasks (note that it may not be that much in reality)
		new ConcurrentIterator<String>(it.iterator(), 100).dump(new HashSet<String>());
		it.dump(new HashSet<String>());
		
		final Set<String> set1 = new HashSet<String>();
		final Set<String> set2 = new HashSet<String>();
		
		System.out.println("single-threaded: " + profile(new Runnable()
		{
			public void run()
			{
				it.dump(set2);
			}
		}));
		System.out.println("multi-threaded: " + profile(new Runnable()
		{
			public void run()
			{
				new ConcurrentIterator<String>(it.iterator(), 100).dump(set1);
			}
		}));
		
		// On my iMac Core 2 Duo 2.8 GHz
		// single-threaded: ~1000000 ns
		// multi-threaded:  ~100000 ns
	}
	
	private static long profile(Runnable r)
	{
		long begin = System.nanoTime();
		r.run();
		long duration = System.nanoTime() - begin;
		return duration / 1000;
	}
}
