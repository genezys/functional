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
	protected abstract boolean moveNext() throws Exception;
	
	protected abstract T getCurrent() throws Exception;
	
	public boolean hasNext()
	{
		try
		{
			advance();
			return m_bHasNext;
		}
		catch ( IterationRuntimeException e )
		{
			throw e;
		} 
		catch ( Exception e )
		{
			throw new IterationRuntimeException(e);
		}
	}

	public T next()
	{
		try
		{
			advance();
			m_bShouldMoveToNext = true;
			if( !m_bHasNext )
			{
				throw new NoSuchElementException();
			}
			return getCurrent();
		}
		catch ( IterationRuntimeException e )
		{
			throw e;
		} 
		catch ( Exception e )
		{
			throw new IterationRuntimeException(e);
		}
	}

	private boolean m_bShouldMoveToNext = true;
	private boolean m_bHasNext = false;

	private void advance() throws Exception
	{
		if ( m_bShouldMoveToNext )
		{
			m_bShouldMoveToNext = false;
			m_bHasNext = moveNext();
		}
	}
}
