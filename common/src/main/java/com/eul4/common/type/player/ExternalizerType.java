package com.eul4.common.type.player;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.externalizer.writer.ObjectWriter;

public interface ExternalizerType
{
	ObjectReader<?> newInstance(Readers readers) throws InvalidVersionException;
	Class<? extends ObjectReader<?>> getReaderClass();
	ObjectWriter<?> newInstance(Writers writers);
	Class<? extends ObjectWriter<?>> getWriterClass();
}
