package fr.cantor.functional.functions.predicates;

import fr.cantor.functional.exceptions.FunctionalException;

public class NotPredicate1<T> implements Predicate1<T>
{
	private Predicate1<T> m_predicate;

	public NotPredicate1(Predicate1<T> predicate)
	{
		m_predicate = predicate;
	}
	
	public Boolean call(T t) throws FunctionalException 
	{
		return !m_predicate.call(t);
	}
}
