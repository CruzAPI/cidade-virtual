package com.eul4.common.wrapper;

import java.io.IOException;

@FunctionalInterface
public interface Reader<T>
{
	T readObject() throws IOException, ClassNotFoundException;
}
