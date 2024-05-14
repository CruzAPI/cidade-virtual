package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Readable;
import org.bukkit.generator.structure.Structure;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectReader<T>
{
	protected final Common plugin;
	protected final Readers readers;
	protected final ObjectInput in;
	private final Reader<T> reader;
	private final Class<T> type;
	
	private final Map<Integer, T> references = new HashMap<>();
	private int currentId;
	
	
	public ObjectReader(Readers readers, Class<T> type) throws InvalidVersionException
	{
		this.plugin = readers.getPlugin();
		this.readers = readers;
		this.in = readers.getObjectInput();
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
		case 1:
			int id = in.readInt();
			Integer hash = (references.get(id) == null ? null : references.get(id).hashCode());
			return references.get(id);
		case 0:
			T reference = readable.read();
			references.put(currentId++, reference);
			
			if(reference == readObject(reference))
			{
				return reference;
			}
			
			throw new IOException("Method readObject(" + reference.getClass().getSimpleName() + ") is returning an invalid reference!");
		case -1:
		default:
			return null;
		}
	}
	
	public final T readReference(Readable<T> readable) throws IOException, ClassNotFoundException
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
