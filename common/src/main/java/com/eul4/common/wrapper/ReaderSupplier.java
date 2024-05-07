package com.eul4.common.wrapper;

import java.io.IOException;
import java.util.function.Supplier;

@FunctionalInterface
public interface ReaderSupplier<T>
{
	T readObject(Supplier<T> supplier) throws IOException, ClassNotFoundException;
}
