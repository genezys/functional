package fr.cantor.functional.functions;

import fr.cantor.functional.exceptions.FunctionalException;

/**
 * A function that directly returns its parameter without modification
 * 
 * @param <T>
 *            Type to return
 */
public class Identity<T> implements Function1<T, T>
{
	public T call(T t) throws FunctionalException
	{
		return t;
	};
}
