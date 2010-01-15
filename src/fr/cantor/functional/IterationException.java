package fr.cantor.functional;

@SuppressWarnings("serial")
public class IterationException extends Exception
{
	public IterationException()
	{
		super();
	}
	
	public IterationException(Throwable t)
	{
		super(t);
	}
	
	public IterationException(String msg)
	{
		super(msg);
	}

	public IterationException(String msg, Throwable t)
	{
		super(msg, t);
	}	
}
