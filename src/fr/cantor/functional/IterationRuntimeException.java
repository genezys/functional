package fr.cantor.functional;

@SuppressWarnings("serial")
public class IterationRuntimeException extends RuntimeException
{
	public IterationRuntimeException()
	{
		super();
	}
	
	public IterationRuntimeException(Throwable t)
	{
		super(t);
	}
	
	public IterationRuntimeException(String msg)
	{
		super(msg);
	}

	public IterationRuntimeException(String msg, Throwable t)
	{
		super(msg, t);
	}	
}
