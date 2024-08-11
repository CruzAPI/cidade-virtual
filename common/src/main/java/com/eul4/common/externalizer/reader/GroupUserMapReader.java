package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.GroupUserMap;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class GroupUserMapReader extends ObjectReader<GroupUserMap>
{
	@Getter
	private final Reader<GroupUserMap> reader;
	private final Readable<GroupUserMap> readable;
	
	public GroupUserMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, GroupUserMap.class);
		
		final ObjectType objectType = CommonObjectType.GROUP_USER_MAP;
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
	
	private GroupUserMap readableVersion0() throws IOException, ClassNotFoundException
	{
		GroupUserReader groupUserReader = readers.getReader(GroupUserReader.class);
		GroupUserMap groupUserMap = new GroupUserMap();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			groupUserMap.put(groupUserReader.readReference());
		}
		
		return groupUserMap;
	}
	
	public GroupUserMap readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
