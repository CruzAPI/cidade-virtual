package com.eul4.common.wrapper;

import java.io.IOException;

@FunctionalInterface
public interface BiParameterizedReadable<T, P0, P1>
{
	Readable<T> getReadable(P0 param0, P1 param1) throws IOException, ClassNotFoundException;
}
