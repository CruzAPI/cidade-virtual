package com.eul4.common.externalizer.writer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class ObjectWriter<T>
{
	protected final ObjectOutput out;
	
	private final Map<T, Integer> references = new HashMap<>();
	
	private int currentId;
	
	public void writeReference(T reference) throws IOException
	{
		out.writeBoolean(references.containsKey(reference));
		
		if(references.containsKey(reference))
		{
			out.writeInt(references.get(reference));
		}
		else
		{
			references.put(reference, currentId++);
			writeObject(reference);
		}
	}
	
	protected abstract void writeObject(T object) throws IOException;
}
