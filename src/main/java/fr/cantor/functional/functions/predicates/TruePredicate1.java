package fr.cantor.functional.functions.predicates;

public class TruePredicate1<T> implements Predicate1<T>
{
	public Boolean call(T t)
	{
		return true;
	}
}
