package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectReader<T>
{
	protected final Common plugin;
	protected final Readers readers;
	protected final DataInput in;
	private final Reader<T> reader;
	protected final Class<T> type;
	
	private final Map<Integer, T> references = new HashMap<>();
	private int currentId;
	
	public ObjectReader(Readers readers, Class<T> type) throws InvalidVersionException
	{
		this.plugin = readers.getPlugin();
		this.readers = readers;
		this.in = readers.getDataInput();
		this.type = type;
		
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
		byte refByte = in.readByte();
		
		switch(refByte)
		{
		case 3:
			int id = in.readInt();
			return references.get(id);
		case 0:
			T reference = readable.read();
			references.put(currentId++, reference);
			getReader().readObject(reference);
			
			return reference;
		case -1:
		default:
			return null;
		}
	}
	
	public final T readReference(Readable<T> readable) throws IOException, ClassNotFoundException
	{
		return reader.readObject(readable);
	}
	
	protected abstract com.eul4.common.wrapper.Reader<T> getReader();
	
	@FunctionalInterface
	private interface Reader<T>
	{
		T readObject(Readable<T> supplier) throws IOException, ClassNotFoundException;
	}
}
