package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.Group;
import com.eul4.common.model.permission.GroupUser;
import com.eul4.common.model.permission.PermissionMap;
import com.eul4.common.model.permission.TimedTick;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GroupUserReader extends ObjectReader<GroupUser>
{
	@Getter
	private final Reader<GroupUser> reader;
	private final Readable<GroupUser> readable;
	
	public GroupUserReader(Readers readers) throws InvalidVersionException
	{
		super(readers, GroupUser.class);
		
		final ObjectType objectType = CommonObjectType.GROUP_USER;
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
	
	private GroupUser readableVersion0() throws IOException, ClassNotFoundException
	{
		UUID userUniqueId = new UUID(in.readLong(), in.readLong());
		TimedTick timedTick = readers.getReader(TimedTickReader.class).readReference();
		
		return GroupUser.builder()
				.userUniqueId(userUniqueId)
				.timedTick(timedTick)
				.build();
	}
	
	public GroupUser readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
