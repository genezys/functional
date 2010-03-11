package fr.cantor.functional;

public class SynchronizedIterator<T> extends Iterator<T>
{
	private java.util.Iterator<T> m_iterator;

	public SynchronizedIterator(java.util.Iterator<T> iterator)
	{
		m_iterator = iterator;
	}
	
	public synchronized boolean hasNext()
	{
		return m_iterator.hasNext();
	}

	public synchronized T next()
	{
		return m_iterator.next();
	}
	
	/**
	 * This iterator is already synchronized.
	 * @return this
	 */
	@Override
	public Iterator<T> synchronizedIterator()
	{
		return this;
	}
}
