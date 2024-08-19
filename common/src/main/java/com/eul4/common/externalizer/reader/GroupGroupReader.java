package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.GroupGroup;
import com.eul4.common.model.permission.TimedTick;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;

public class GroupGroupReader extends ObjectReader<GroupGroup>
{
	@Getter
	private final Reader<GroupGroup> reader;
	private final Readable<GroupGroup> readable;
	
	public GroupGroupReader(Readers readers) throws InvalidVersionException
	{
		super(readers, GroupGroup.class);
		
		final ObjectType objectType = CommonObjectType.GROUP_GROUP;
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
	
	private GroupGroup readableVersion0() throws IOException, ClassNotFoundException
	{
		UUID groupUniqueId = readers.getReader(UUIDReader.class).readReference();
		TimedTick timedTick = readers.getReader(TimedTickReader.class).readReference();
		
		return GroupGroup.builder()
				.groupUniqueId(groupUniqueId)
				.timedTick(timedTick)
				.build();
	}
	
	public GroupGroup readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
