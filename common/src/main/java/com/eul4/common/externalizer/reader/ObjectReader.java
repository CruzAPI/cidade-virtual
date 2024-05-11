package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Readable;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectReader<T>
{
	protected final Readers readers;
	protected final ObjectInput in;
	private final Reader<T> reader;
	
	private final Map<Integer, T> references = new HashMap<>();
	private int currentId;
	
	public ObjectReader(Readers readers) throws InvalidVersionException
	{
		this.readers = readers;
		this.in = readers.getObjectInput();
		
		final ObjectType objectType = CommonObjectType.OBJECT;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private T readerVersion0(Readable<T> readable) throws IOException, ClassNotFoundException
	{
		boolean isReference = in.readBoolean();
		
		if(isReference)
		{
			return references.get(in.readInt());
		}
		else
		{
			T reference = readable.read();
			references.put(currentId++, reference);
			
			if(reference == readObject(reference))
			{
				return reference;
			}
			
			throw new IOException("Method readObject(" + reference.getClass().getSimpleName() + ") is returning an invalid reference!");
		}
	}
	
	protected final T readReference(Readable<T> readable) throws IOException, ClassNotFoundException
	{
		return reader.readObject(readable);
	}
	
	protected abstract T readObject(T object) throws IOException, ClassNotFoundException;
	
	@FunctionalInterface
	private interface Reader<T>
	{
		T readObject(Readable<T> supplier) throws IOException, ClassNotFoundException;
	}
}
