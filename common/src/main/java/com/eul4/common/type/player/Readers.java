package com.eul4.common.type.player;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.DataInput;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Readers
{
	@Getter
	private final Common plugin;
	private final DataInput in;
	private final Map<ObjectType, Byte> versions;
	
	private final Map<Class<? extends ObjectReader>, ObjectReader> readers = new HashMap<>();
	
	public static Readers of(Common plugin, DataInput in, Map<ObjectType, Byte> versions) throws InvalidVersionException
	{
		return new Readers(plugin, in, versions);
	}
	
	private Readers(Common plugin, DataInput in, Map<ObjectType, Byte> versions) throws InvalidVersionException
	{
		this.plugin = plugin;
		this.in = in;
		this.versions = versions;
		
		for(ObjectType objectType : versions.keySet())
		{
			Optional.ofNullable(objectType.getExternalizerType()).ifPresent(this::createReader);
		}
	}
	
	public DataInput getDataInput()
	{
		return in;
	}
	
	@SneakyThrows
	private void createReader(ExternalizerType externalizerType)
	{
		readers.put(externalizerType.getReaderClass(), externalizerType.newInstance(this));
	}
	
	public <R extends ObjectReader> R getReader(Class<R> type)
	{
		return type.cast(readers.get(type));
	}
	
	public Map<ObjectType, Byte> getVersions()
	{
		return versions;
	}
}
