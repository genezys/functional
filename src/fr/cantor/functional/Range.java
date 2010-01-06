package fr.cantor.functional;

import java.util.NoSuchElementException;

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
		if ( m_nEnd < m_nBegin )
		{
			throw new IllegalArgumentException("End cannot be lower than start");
		}
	}

	public Iterator<Integer> iterator()
	{
		return new Iterator<Integer>()
		{
			private Integer m_nIndex = m_nBegin;
			
			public boolean hasNext() 
			{ 
				return ( m_nIndex <= m_nEnd ); 
			}
			
			public Integer next() 
			{ 
				synchronized( m_nIndex )
				{
					if ( !hasNext() )
					{
						throw new NoSuchElementException();
					}
					return m_nIndex++;
				}
			}
		};
	}
}