package fr.cantor.functional.functions.operators;

import fr.cantor.functional.exceptions.FunctionalException;
import fr.cantor.functional.functions.Function2;

public class IntegerMultiply implements Function2<Integer, Integer, Integer>
{
	public Integer call(Integer n1, Integer n2) throws FunctionalException
	{
		return n1 * n2;
	}

}
