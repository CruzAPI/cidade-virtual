package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.TimedTick;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class TimedTickReader extends ObjectReader<TimedTick>
{
	@Getter
	private final Reader<TimedTick> reader;
	private final Readable<TimedTick> readable;
	
	public TimedTickReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TimedTick.class);
		
		final ObjectType objectType = CommonObjectType.TIMED_TICK;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private TimedTick readableVersion0() throws IOException, ClassNotFoundException
	{
		long creationTick = in.readLong();
		long durationTick = in.readLong();
		
		return TimedTick.builder()
				.creationTick(creationTick)
				.durationTick(durationTick)
				.build();
	}
	
	public TimedTick readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
