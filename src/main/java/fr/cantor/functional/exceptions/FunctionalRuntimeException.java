package fr.cantor.functional.exceptions;

@SuppressWarnings("serial")
public class FunctionalRuntimeException extends RuntimeException
{
	public FunctionalRuntimeException()
	{
		super();
	}
	
	public FunctionalRuntimeException(Throwable t)
	{
		super(t);
	}
	
	public FunctionalRuntimeException(String msg)
	{
		super(msg);
	}

	public FunctionalRuntimeException(String msg, Throwable t)
	{
		super(msg, t);
	}	
}
