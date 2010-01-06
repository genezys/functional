package fr.cantor.functional;


public final class Functions
{
	public static interface Function1<R, T1> { public R call(T1 t1) throws Exception; }	
	public static interface Function2<R, T1, T2> { public R call(T1 t1, T2 t2) throws Exception; }
	public static interface Function3<R, T1, T2, T3> { public R call(T1 t1, T2 t2, T3 t3) throws Exception; }
	public static interface Function4<R, T1, T2, T3, T4> { public R call(T1 t1, T2 t2, T3 t3, T4 t4) throws Exception; }
	public static interface Function5<R, T1, T2, T3, T4, T5> { public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws Exception; }
	
	public static interface Procedure1<T1> extends Function1<Void, T1> {}
	public static interface Procedure2<T1, T2> extends Function2<Void, T1, T2> {}
	public static interface Procedure3<T1, T2, T3> extends Function3<Void, T1, T2, T3> {}
	public static interface Procedure4<T1, T2, T3, T4> extends Function4<Void, T1, T2, T3, T4> {}
	public static interface Procedure5<T1, T2, T3, T4, T5> extends Function5<Void, T1, T2, T3, T4, T5> {}

	public static interface Predicate1<T1> extends Function1<Boolean, T1> {}	
	public static interface Predicate2<T1, T2> extends Function2<Boolean, T1, T2> {}	
	public static interface Predicate3<T1, T2, T3> extends Function3<Boolean, T1, T2, T3> {}	
	public static interface Predicate4<T1, T2, T3, T4> extends Function4<Boolean, T1, T2, T3, T4> {}	
	public static interface Predicate5<T1, T2, T3, T4, T5> extends Function5<Boolean, T1, T2, T3, T4, T5> {}
	
	public static interface Injecter<T1, T2> extends Function2<T1, T1, T2> {}
}