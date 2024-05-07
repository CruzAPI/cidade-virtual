package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectReader<T>
{
	protected final ObjectInput in;
	private final Reader<T> reader;
	
	private final Map<Integer, T> references = new HashMap<>();
	private int currentId;
	
	public ObjectReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		this.in = in;
		
		if(commonVersions.getObjectVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid ObjectExternal version: " + commonVersions.getObjectVersion());
		}
	}
	
	private T readerVersion0() throws IOException, ClassNotFoundException
	{
		boolean isReference = in.readBoolean();
		
		if(isReference)
		{
			return references.get(in.readInt());
		}
		else
		{
			return references.computeIfAbsent(currentId++, (key) -> readObjectSneaky());
		}
	}
	
	public final T readReference() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
	
	@SneakyThrows
	private T readObjectSneaky()
	{
		return readObject();
	}
	
	protected abstract T readObject() throws IOException, ClassNotFoundException;
}
