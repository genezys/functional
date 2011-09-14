package fr.cantor.functional;

import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.functions.Function1;


public class IterableArray<T> extends Iterable<T> 
{
	private Iterable<T> m_values;
	
	public IterableArray(final T[] values) 
	{
		m_values = new Range(values.length).map(new Function1<T, Integer>() 
		{
			public T call(Integer i) throws FunctionalException 
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
