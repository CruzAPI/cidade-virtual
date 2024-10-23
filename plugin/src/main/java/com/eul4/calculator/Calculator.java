package com.eul4.calculator;

public abstract class Calculator<N extends Number & Comparable<N>>
{
	public abstract N getZeroSample();
	public abstract N add(N augend, N addend);
	public abstract N subtract(N minuend, N subtrahend);
}
