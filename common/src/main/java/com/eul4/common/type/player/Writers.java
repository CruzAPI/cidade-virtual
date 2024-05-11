package com.eul4.common.type.player;

import com.eul4.common.externalizer.writer.ObjectWriter;

import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Writers
{
	private final ObjectOutput out;
	
	private final Map<Class<? extends ObjectWriter<?>>, ObjectWriter<?>> writers = new HashMap<>();
	
	public static Writers of(ObjectOutput out, ObjectType[] objectTypes)
	{
		return new Writers(out, objectTypes);
	}
	
	private Writers(ObjectOutput out, ObjectType[] objectTypes)
	{
		this.out = out;
		
		for(ObjectType objectType : objectTypes)
		{
			Optional.of(objectType)
					.map(ObjectType::getExternalizerType)
					.ifPresent(this::createWriter);
		}
	}
	
	public ObjectOutput getObjectOutput()
	{
		return out;
	}
	
	private void createWriter(ExternalizerType externalizerType)
	{
		writers.put(externalizerType.getWriterClass(), externalizerType.newInstance(this));
	}
	
	public <W extends ObjectWriter<?>> W getWriter(Class<W> type)
	{
		return type.cast(writers.get(type));
	}
}
