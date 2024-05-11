package com.eul4.common.type.player;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import lombok.SneakyThrows;

import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Readers
{
	private final ObjectInput in;
	private final Map<ObjectType, Byte> versions;
	
	private final Map<Class<? extends ObjectReader<?>>, ObjectReader<?>> readers = new HashMap<>();
	
	public static Readers of(ObjectInput in, Map<ObjectType, Byte> versions) throws InvalidVersionException
	{
		return new Readers(in, versions);
	}
	
	private Readers(ObjectInput in, Map<ObjectType, Byte> versions) throws InvalidVersionException
	{
		this.in = in;
		this.versions = versions;
		
		for(ObjectType objectType : versions.keySet())
		{
			Optional.ofNullable(objectType.getExternalizerType()).ifPresent(this::createReader);
		}
	}
	
	public ObjectInput getObjectInput()
	{
		return in;
	}
	
	@SneakyThrows
	private void createReader(ExternalizerType externalizerType)
	{
		readers.put(externalizerType.getReaderClass(), externalizerType.newInstance(this));
	}
	
	public <R extends ObjectReader<?>> R getReader(Class<R> type)
	{
		return type.cast(readers.get(type));
	}
	
	public Map<ObjectType, Byte> getVersions()
	{
		return versions;
	}
}
