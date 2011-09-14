package fr.cantor.functional;

import java.util.NoSuchElementException;

import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.exceptions.FunctionalRuntimeException;

/**
 * Abstract class to implement the Iterator interface
 * using a simpler contract than the default one:
 * - moveNext() moves the cursor to next element 
 *   and returns false if it could not move
 * - getCurrent() returns current element under cursor
 */
public abstract class EasierIterator<T> extends Iterator<T>
{
	protected abstract boolean moveNext() throws FunctionalException;
	
	protected abstract T getCurrent() throws FunctionalException;
	
	public boolean hasNext()
	{
		try
		{
			advance();
			return m_bHasNext;
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
		catch ( FunctionalException e )
		{
			throw new FunctionalRuntimeException(e);
		}
	}

	private boolean m_bShouldMoveToNext = true;
	private boolean m_bHasNext = false;

	protected void advance() throws FunctionalException
	{
		if ( m_bShouldMoveToNext )
		{
			m_bShouldMoveToNext = false;
			m_bHasNext = moveNext();
		}
	}
}
