package fr.cantor.functional;

import java.util.NoSuchElementException;

/**
 * Abstract class to implement the Iterator interface
 * using a simpler contract than the default one:
 * - moveNext() moves the cursor to next element 
 *   and returns false if it could not move
 * - getCurrent() returns current element under cursor
 */
public abstract class EasierIterator<T> extends Iterator<T>
{
	protected abstract boolean moveNext();
	
	protected abstract T getCurrent();
	
	public boolean hasNext()
	{
		advance();
		return m_bHasNext;
	}

	public T next()
	{
		advance();
		m_bShouldMoveToNext = true;
		if( !m_bHasNext )
		{
			throw new NoSuchElementException();
		}
		return getCurrent();
	}

	private boolean m_bShouldMoveToNext = true;
	private boolean m_bHasNext = false;

	private void advance()
	{
		if ( m_bShouldMoveToNext )
		{
			m_bShouldMoveToNext = false;
			m_bHasNext = moveNext();
		}
	}
}
