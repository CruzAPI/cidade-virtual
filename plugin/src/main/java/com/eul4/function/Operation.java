package com.eul4.function;

@FunctionalInterface
public interface Operation<N extends Number & Comparable<N>>
{
	void execute(N amount);
}
