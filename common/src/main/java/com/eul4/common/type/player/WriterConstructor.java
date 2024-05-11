package com.eul4.common.type.player;

import com.eul4.common.externalizer.writer.ObjectWriter;

@FunctionalInterface
public interface WriterConstructor
{
	ObjectWriter<?> newInstance(Writers writers);
}