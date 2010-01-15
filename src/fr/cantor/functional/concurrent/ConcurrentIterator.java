package fr.cantor.functional.concurrent;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.cantor.functional.Injecter;
import fr.cantor.functional.Iterable;
import fr.cantor.functional.Iterator;
import fr.cantor.functional.Range;
import fr.cantor.functional.functions.Function1;

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
		this(iterator, 10 * getAvailableProcessors());
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
	@SuppressWarnings("unchecked")
	@Override
	protected <V> V injectWithIterator(Iterator<T> it, V value, Injecter<V,T> injecter) 
	{
		final ExecutorService executor = Executors.newFixedThreadPool(getAvailableProcessors());
		
		// Add a batch of task to start
		final Future<V>[] tasks = new Future[m_nBatchSize];
		for( int i = 0; i < tasks.length; ++i )
		{
			tasks[i] = submitIterationToExecutor(executor, it, value, injecter);
		}
		
		while ( true )
		{
			// Run the tasks
			Thread.yield();
			
			// Replaced finished tasks by new ones
			for ( int i = 0; i < tasks.length; i++ ) 
			{
				Future<V> future = tasks[i];
				if( future != null && future.isDone() )
				{
					try
					{
						value = future.get();
						tasks[i] = submitIterationToExecutor(executor, it, value, injecter);
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
	
	private static int getAvailableProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws InterruptedException 
	{
		// Iterated on all numbers from 0 to 999 and transform them to words 
		// adding some free time to simulate a CPU yield action like a disk IO 
		final Iterable<String> it = new Range(10000).map(new Function1<String, Integer>() 
		{
			public String call(Integer n) throws Exception 
			{
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
		
		final Set<String> set1 = new HashSet<String>();
		final Set<String> set2 = new HashSet<String>();
				
		System.out.println("single-threaded: " + profile(new Runnable()
		{
			public void run()
			{
				it.dump(set2);
			}
		}));
		System.out.println("multi-threaded:  " + profile(new Runnable()
		{
			public void run()
			{
				new ConcurrentIterator<String>(it.iterator(), 100).dump(set1);
			}
		}));
	}
	
	private static long profile(Runnable r)
	{
		long begin = System.nanoTime();
		r.run();
		long duration = System.nanoTime() - begin;
		return duration / 1000;
	}
}
