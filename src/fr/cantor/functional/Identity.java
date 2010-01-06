package fr.cantor.functional;

import fr.cantor.functional.Functions.Function1;

/**
 * A function that directly returns its parameter without modification
 * @param <T> Type to return
 */
public class Identity<T> implements Function1<T, T>
{
	public T call(T t) throws Exception 
	{
		return t;
	};	
}
