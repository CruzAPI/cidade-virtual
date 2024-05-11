package com.eul4.common.type.player;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;

@FunctionalInterface
public interface ReaderConstructor
{
	ObjectReader<?> newInstance(Readers readers) throws InvalidVersionException;
}