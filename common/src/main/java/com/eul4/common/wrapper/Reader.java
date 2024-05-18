package com.eul4.common.wrapper;

import java.io.IOException;

@FunctionalInterface
public interface Reader<T>
{
	void readObject(T object) throws IOException, ClassNotFoundException;
	
	static <T> Reader<T> identity()
	{
		return t -> {};
	}
}
