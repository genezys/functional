package fr.cantor.functional;

import fr.cantor.functional.Functions.Function1;

public class IterableArray<T> extends Iterable<T> 
{
	private Iterable<T> m_values;
	
	public IterableArray(final T[] values) 
	{
		m_values = new Range(values.length).map(new Function1<T, Integer>() 
		{
			public T call(Integer i) throws Exception 
			{
				return values[i];
			}
		});
	}
	
	public Iterator<T> iterator() 
	{
		return m_values.iterator();
	}
}
