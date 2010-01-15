/**
 * 
 */
package fr.cantor.functional.functions;

public interface Function1<R, T1>
{
	public R call(T1 t1) throws Exception;
}