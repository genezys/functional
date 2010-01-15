package fr.cantor.functional.functions.predicates;

public class NotPredicate1<T> implements Predicate1<T>
{
	private Predicate1<T> m_predicate;

	public NotPredicate1(Predicate1<T> predicate)
	{
		m_predicate = predicate;
	}
	
	public Boolean call(T t) throws Exception 
	{
		return !m_predicate.call(t);
	}
}
