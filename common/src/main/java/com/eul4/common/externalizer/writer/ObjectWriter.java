package com.eul4.common.externalizer.writer;

import lombok.RequiredArgsConstructor;

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
			int referenceId = references.get(reference);
			out.writeInt(referenceId);
		}
		else
		{
			writeObject(reference);
			references.put(reference, currentId++);
		}
	}
	
	protected abstract void writeObject(T object) throws IOException;
	
}
