package com.eul4.common.wrapper;

import java.io.IOException;

@FunctionalInterface
public interface Readable<T>
{
	T read() throws IOException, ClassNotFoundException;
}
