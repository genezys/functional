package fr.cantor.functional.exceptions;

@SuppressWarnings("serial")
public class FunctionalException extends Exception
{
	public FunctionalException()
	{
		super();
	}
	
	public FunctionalException(Throwable t)
	{
		super(t);
	}
	
	public FunctionalException(String msg)
	{
		super(msg);
	}

	public FunctionalException(String msg, Throwable t)
	{
		super(msg, t);
	}	
}
