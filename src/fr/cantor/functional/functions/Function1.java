/**
 * 
 */
package fr.cantor.functional.functions;

import fr.cantor.functional.exceptions.FunctionalException;

public interface Function1<R, T1>
{
	public R call(T1 t1) throws FunctionalException;
}