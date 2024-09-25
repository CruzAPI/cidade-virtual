package com.eul4.common.externalizer.writer;

import com.eul4.common.Common;
import com.eul4.common.type.player.Writers;
import lombok.RequiredArgsConstructor;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class ObjectWriter<T>
{
	protected final Common plugin;
	protected final Writers writers;
	protected final DataOutput out;
	
	private final Map<T, Integer> references = new HashMap<>();
	
	private int currentId;
	private final Class<T> type;
	
	public ObjectWriter(Writers writers, Class<T> type)
	{
		this.plugin = writers.getPlugin();
		this.writers = writers;
		this.out = writers.getDataOutput();
		this.type = type;
	}
	
	public void writeReferenceNotNull(T reference) throws IOException, NullPointerException
	{
		writeReference(Objects.requireNonNull(reference));
	}
	
	public void writeReference(T reference) throws IOException
	{
		if(reference == null)
		{
			out.writeByte(-1);
			return;
		}
		
		out.writeByte(references.containsKey(reference) ? 3 : 0);
		
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
