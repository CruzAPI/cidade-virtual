package com.eul4.common.wrapper;

import java.io.IOException;

@FunctionalInterface
public interface ParameterizedReadable<T, P0>
{
	Readable<T> getReadable(P0 param0) throws IOException, ClassNotFoundException;
}
