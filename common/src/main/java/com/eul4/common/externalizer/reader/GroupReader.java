package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.GroupUserMap;
import com.eul4.common.model.permission.PermissionMap;
import com.eul4.common.model.permission.Group;
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

public class GroupReader extends ObjectReader<Group>
{
	@Getter
	private final Reader<Group> reader;
	private final Readable<Group> readable;
	
	public GroupReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Group.class);
		
		final ObjectType objectType = CommonObjectType.GROUP;
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
	
	private Group readableVersion0() throws IOException, ClassNotFoundException
	{
		String name = in.readUTF();
		PermissionMap permissionMap = readers.getReader(PermissionMapReader.class).readReference();
		GroupUserMap groupUserMap = readers.getReader(GroupUserMapReader.class).readReference();
		int order = in.readInt();
		
		return Group.builder()
				.name(name)
				.permissionMap(permissionMap)
				.groupUserMap(groupUserMap)
				.order(order)
				.build();
	}
	
	public Group readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
