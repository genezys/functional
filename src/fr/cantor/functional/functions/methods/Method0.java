package fr.cantor.functional.functions.methods;

import java.lang.reflect.Method;

import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.functions.Function1;

public class Method0<R, T1> implements Function1<R, T1>
{
	private String m_name;
	private Method m_method;

	public Method0(String name)
	{
		m_name = name;
	}
	
	@SuppressWarnings("unchecked")
	public R call(T1 t1) throws FunctionalException 
	{
		try
		{
			if ( m_method == null )
			{
				m_method = t1.getClass().getMethod(m_name);
			}
			return (R) m_method.invoke(t1);
		}
		catch ( RuntimeException e ) 
		{
			throw e;
		}
		catch ( Exception e )
		{
			throw new FunctionalException(e);
		}
	}
}
