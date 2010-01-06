package fr.cantor.functional.concurrent;

import fr.cantor.functional.Iterable;
import fr.cantor.functional.Iterator;

public class ConcurrentIterable<T> extends Iterable<T>
{
	private Iterable<T> m_iterable;

	public ConcurrentIterable(Iterable<T> iterable)
	{
		m_iterable = iterable;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new ConcurrentIterator<T>(m_iterable.iterator());
	}
}
