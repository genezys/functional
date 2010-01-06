package fr.cantor.functional;

/**
 * A read-only implementation of java.util.Iterator
 * @param <T> Type to iterate on
 */
public abstract class Iterator<T> extends Iterable<T> implements java.util.Iterator<T>
{
	public static <T> Iterator<T> wrap(final java.util.Iterator<T> it)
	{
		return new Iterator<T>()
		{
			public boolean hasNext() { return it.hasNext(); }
			public T next() { return it.next(); }
		};
	}
	
	public static <T> Iterator<T> wrap(final Iterator<T> it)
	{
		return it;
	}
	
	/**
	 * Our iterator is read-only
	 * @throws UnsupportedOperationException This Iterator class in read-only
	 */
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Iterator is also iterable in a for-loop, yes this is convenient
	 * @return this iterator
	 */
	public Iterator<T> iterator()
	{
		return this;
	}
}
