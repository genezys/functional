package fr.cantor.functional;

import fr.cantor.functional.exceptions.FunctionalException;



/**
 * An Iterable range of integers. 
 * Either from 0 for a specific number of integers, or from a value to another.
 */
public class Range extends Iterable<Integer>
{
	private int m_nBegin;
	private int m_nEnd;
	
	/**
	 * Construct an 0-based index iterator which iterates a given number of times.
	 * Used for example to iterate on a indexed collection.
	 * @param nCount Number of times to iterate
	 */
	public Range(int nCount)
	{
		this(0, nCount - 1);
	}
	
	/**
	 * Construct an index iterator which iterates on a integer range
	 * @param nStart First index to iterate on (included)
	 * @param nEnd Last index to iterate on (included)
	 */
	public Range(int nStart, int nEnd)
	{
		m_nBegin = nStart;
		m_nEnd = nEnd;
	}

	public Iterator<Integer> iterator()
	{
		return new EasierIterator<Integer>()
		{
			private Integer m_nIndex = m_nBegin - 1;
			
			@Override
			protected Integer getCurrent() throws FunctionalException
			{
				return m_nIndex;
			}
			
			@Override
			protected boolean moveNext() throws FunctionalException
			{
				m_nIndex += 1;
				return (m_nIndex <= m_nEnd);
			}
		}.synchronizedIterator();
	}
}