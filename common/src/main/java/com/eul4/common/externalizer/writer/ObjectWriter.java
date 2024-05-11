package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class ObjectWriter<T>
{
	protected final Writers writers;
	protected final ObjectOutput out;
	
	private final Map<T, Integer> references = new HashMap<>();
	
	private int currentId;
	
	public ObjectWriter(Writers writers)
	{
		this.writers = writers;
		this.out = writers.getObjectOutput();
	}
	
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
