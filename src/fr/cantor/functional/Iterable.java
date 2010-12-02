package fr.cantor.functional;

import java.util.Collection;
import java.util.NoSuchElementException;

import fr.cantor.functional.concurrent.ConcurrentIterable;
import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.exceptions.FunctionalRuntimeException;
import fr.cantor.functional.functions.Function1;
import fr.cantor.functional.functions.Function2;
import fr.cantor.functional.functions.predicates.NotPredicate1;
import fr.cantor.functional.functions.predicates.Predicate1;
import fr.cantor.functional.functions.procedures.Procedure1;
import fr.cantor.functional.nuple.Pair;

/**
 * An extension to java.lang.Iterable to add functional operations like each,
 * filter, map, inject, etc.
 * 
 * @param <T>
 *            Type to iterate on
 */
public abstract class Iterable<T> implements java.lang.Iterable<T>
{	
	/**
	 * Create an Iterable from an existing java.lang.Iterable
	 * 
	 * @param <T>
	 *            Type to iterate on
	 * @param iterable
	 *            existing java.lang.Iterable
	 * @return a new Iterable object wrapping the iterable parameter
	 */
	public static <T> Iterable<T> wrap(final java.lang.Iterable<T> iterable)
	{
		return new Iterable<T>()
		{
			public Iterator<T> iterator()
			{
				return new SynchronizedIterator<T>(Iterator.wrap(iterable.iterator()));
			}
		};
	}
	
	/**
	 * This methods is used in order to efficiently wrap an java.lang.Iterable
	 * that would also be of our class
	 * 
	 * @param <T>
	 *            Type to iterate on
	 * @param iterable
	 *            existing Iterable
	 * @return directly the iterable parameter
	 * @see #wrap(java.lang.Iterable)
	 */
	public static <T> Iterable<T> wrap(final Iterable<T> iterable)
	{
		return iterable;
	}
	
	/**
	 * The only method to implement.
	 * We suggest you just return an implementation of our Iterator.
	 * @see Iterator
	 */
	public abstract Iterator<T> iterator();
	
	/**
	 * Compare two iterables to see if they return the same elements.
	 * They must be of equal size of each element must either be the same (==)
	 * or equals.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if ( obj == this )
		{
			return true;
		}
		if ( obj instanceof Iterable )
		{
			final Iterable<Object> it = (Iterable<Object>) obj;
			try
			{
				return combine(it).inject(true, new Function2<Boolean, Boolean, Pair<T, Object>>()
				{
					public Boolean call(Boolean bOtherEquals, Pair<T, Object> pair) throws FunctionalException 
					{
						return bOtherEquals && ( ( pair.first == pair.second ) || pair.first.equals(pair.second) );
					}
				});
			}
			catch ( FunctionalException e )
			{
				// equals cannot throw :-(
				throw new FunctionalRuntimeException(e);
			}
		}
		return false;
	}
	
	/**
	 * Computes a value by processing every elements in the iterator Used to
	 * build one value from every elements in the iterator. This version takes
	 * the initial value to inject and returns a value of the same type.
	 * 
	 * @param <V>
	 *            Type of the value to inject and of the resulting value
	 * @param value
	 *            initial value to inject
	 * @param injecter
	 *            Functor that will apply the injection on each element
	 * @return the injected value modified by the injecter called for every
	 *         elements
	 * @throws FunctionalException 
	 */
	public <V> V inject(V value, final Function2<V, V, T> injecter) throws FunctionalException
	{
		return injectWithIterator(iterator(), value, injecter);
	}

	/**
	 * Computes a value by processing every elements in the iterator This
	 * version uses the first value of the iterator as the initial value
	 * 
	 * @param injecter
	 *            injecter object to use to compute each element injection
	 * @return injected value modified by the injecter called for every elements
	 * @throws FunctionalException 
	 * @see #inject(Object, Injecter)
	 */
	public T inject(final Function2<T, T, T> injecter) throws FunctionalException
	{
		Iterator<T> it = iterator();
		if ( !it.hasNext() )
		{
			return null;
		}
		return injectWithIterator(it, it.next(), injecter);
	}

	/**
	 * Internal implementation of inject.
	 * @param <V> Type to inject and return
	 * @param it iterator containing the elements to inject on
	 * @param value initial value to inject
	 * @param injecter closure to execute the injection
	 * @return the injected value modified from all the injection
	 * @throws FunctionalException 
	 */
	protected <V> V injectWithIterator(Iterator<T> it, V value, final Function2<V, V, T> injecter) throws FunctionalException
	{
		while ( it.hasNext() )
		{
			value = injecter.call(value, it.next());
		}
		return value;
	}

	/**
	 * Transform every elements in the iterator
	 * 
	 * @param <V>
	 *            Type of the returned iterator element
	 * @param mapper
	 *            Functor that will transform each element of the iterator
	 * @return an Iterator containing the transformed elements
	 */
	public <V> Iterable<V> map(final Function1<V, T> mapper)
	{
		return new Iterable<V>()
		{
			public Iterator<V> iterator()
			{
				final Iterator<T> it = Iterable.this.iterator();
				return new Iterator<V>()
				{
					public boolean hasNext() { return it.hasNext(); }
	
					public V next()
					{
						T next = it.next();
						try
						{
							return mapper.call(next);
						}
						catch ( FunctionalException e )
						{
							// Iterator#next cannot throw :-(
							throw new FunctionalRuntimeException(e);
						}
					}
				};
			}
		};
	}

	/**
	 * Filter an Iterable to keep only some elements
	 * @param predicate Predicate that returns true for elements to be kept
	 * @return an Iterator containing the retained elements
	 */
	public Iterable<T> select(final Predicate1<T> predicate)
	{
		return new Iterable<T>()
		{
			public Iterator<T> iterator()
			{
				final Iterator<T> it = Iterable.this.iterator();
				return new EasierIterator<T>()
				{
					private T m_tCurrent;
					
					@Override
					protected T getCurrent()
					{
						return m_tCurrent;
					}

					@Override
					protected boolean moveNext() throws FunctionalException
					{
						while ( it.hasNext() )
						{
							T t = it.next();
							if ( predicate.call(t) )
							{
								m_tCurrent = t;
								return true;
							}
						}
						return false;
					}
				};
			}
		};
	}
	
	public Iterable<Pair<Integer, T>> withIndex()
	{
		final int[] counter = new int[]{ 0 };
		return map(new Function1<Pair<Integer, T>, T>()
		{
			public Pair<Integer, T> call(T value) throws FunctionalException
			{
				return new Pair<Integer, T>(counter[0]++, value);
			}
		});
	}

	/**
	 * @return the first element of the iterator or null if it does not exists
	 * @throws FunctionalException 
	 */
	public T first() throws FunctionalException
	{
		return inject(new Function2<T, T, T>()
		{
			public T call(T first, T value)
			{
				return first;
			}
		});
	}

	/**
	 * @params predicate Predicate describing the first element to search
	 * @return the first element of the iterator or null if it does not exists
	 * @throws FunctionalException 
	 */
	public T first(Predicate1<T> predicate) throws FunctionalException
	{
		return select(predicate).first();
	}

	/**
	 * Calls procedure for each element in the iterator
	 * 
	 * @param procedure
	 *            Functor that will process each element
	 * @throws Exception
	 */
	public void each(final Procedure1<T> procedure) throws FunctionalException
	{
		inject(null, new Function2<Void, Void, T>() 
		{
			public Void call(Void unused, T value) throws FunctionalException 
			{
				procedure.call(value);
				return null;
			}
		});
	}

	/**
	 * Search for any element that satisfies the predicate. Stop the iteration
	 * after one element has been found.
	 * 
	 * @param predicate
	 *            Predicate to test each element
	 * @return true if one element satisfies the predicate, false otherwise
	 * @throws FunctionalException
	 */
	public boolean any(final Predicate1<T> predicate) throws FunctionalException
	{
		return inject(false, new Function2<Boolean, Boolean, T>() 
		{
			public Boolean call(Boolean others, T value) throws FunctionalException 
			{
				return others || predicate.call(value);
			}
		});
	}

	/**
	 * @param predicate
	 *            Predicate to test each element
	 * @return true if all elements satisfy the predicate, false otherwise
	 * @throws FunctionalException
	 */
	public boolean all(final Predicate1<T> predicate) throws FunctionalException 
	{
		return inject(true, new Function2<Boolean, Boolean, T>() 
		{
			public Boolean call(Boolean others, T value) throws FunctionalException 
			{
				return others && predicate.call(value);
			}
		});
	}
	
	/**
	 * Rejects an Iterable to keep only some elements
	 * @param predicate Predicate that returns true for elements to be kept
	 * @return an Iterator containing the retained elements
	 */
	public Iterable<T> reject(final Predicate1<T> predicate)
	{
		return select(new NotPredicate1<T>(predicate));
	}

	/**
	 * Dumps the iterator in a collection
	 * 
	 * @param collection the collection to append elements into
	 * @return the collection filled with all elements from the iterator
	 * @throws FunctionalException 
	 */
	public <C extends Collection<T>> C dump(C collection) throws FunctionalException
	{
		return inject(collection, new Function2<C, C, T>()
		{
			public C call(C collection, T t)
			{
				collection.add(t);
				return collection;
			}
		});
	}
	
	/**
	 * Concatenate every elements separated by a text. Iterator elements are
	 * transformed into String by calling toString()
	 * 
	 * @param separator
	 *            text to use to separator elements
	 * @return the concatenated string
	 * @throws FunctionalException 
	 */
	public String join(final String separator) throws FunctionalException
	{
		return inject(new StringBuilder(), new Function2<StringBuilder, StringBuilder, T>()
		{
			private boolean m_first = true;

			public StringBuilder call(StringBuilder sb, T value) throws FunctionalException 
			{
				if ( m_first )
				{
					m_first = false;
				}
				else
				{
					sb.append(separator);
				}
				sb.append(value);
				return sb;
			}
		}).toString();
	}

	/**
	 * Combines this iterable with another one to produce an iterable
	 * of pairs of each type of the combined iterabled.
	 * If both iterables do not contain the same amount of objects
	 * @param <V> Iterated type of the other iterable
	 * @param it Iterable to combine
	 * @return an Iterable containing pairs of this iterable with the other. 
	 */
	public <V> Iterable<Pair<T, V>> combine(final java.lang.Iterable<V> it)
	{
		return new Iterable<Pair<T,V>>()
		{
			public Iterator<Pair<T, V>> iterator()
			{
				final Iterator<T> it1 = Iterable.this.iterator();
				final Iterator<V> it2 = Iterable.wrap(it).iterator();
				return new Iterator<Pair<T,V>>()
				{
					public boolean hasNext()
					{
						return it1.hasNext() || it2.hasNext();
					}
					public Pair<T, V> next()
					{
						T value1 = getNextValue(it1);
						V value2 = getNextValue(it2);
						return new Pair<T, V>(value1, value2);
					}
					private <S> S getNextValue(final Iterator<S> it)
					{
						try 
						{
							return it.hasNext() ? it.next() : null;
						} 
						catch ( NoSuchElementException e ) 
						{
							return null;
						}
					}
				};
			}
		};
	}
	
	/**
	 * Wraps the current Iterable in a ConcurrentIterable
	 * to dispatch iteration on multiple threads
	 * @return
	 */
	public Iterable<T> concurrently()
	{
		return new ConcurrentIterable<T>(this);
	}
	
	/**
	 * Wraps the current Iterable in a ConcurrentIterable
	 * to dispatch iteration on multiple threads
	 * @return
	 */
	public Iterable<T> concurrently(int countThreads)
	{
		return new ConcurrentIterable<T>(this, countThreads);
	}
}