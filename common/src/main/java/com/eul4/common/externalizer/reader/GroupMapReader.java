package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.GroupMap;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class GroupMapReader extends ObjectReader<GroupMap>
{
	@Getter
	private final Reader<GroupMap> reader;
	private final Readable<GroupMap> readable;
	
	public GroupMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, GroupMap.class);
		
		final ObjectType objectType = CommonObjectType.GROUP_MAP;
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
	
	private GroupMap readableVersion0() throws IOException, ClassNotFoundException
	{
		GroupReader groupReader = readers.getReader(GroupReader.class);
		GroupMap groupMap = new GroupMap();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			groupMap.put(groupReader.readReference());
		}
		
		return groupMap;
	}
	
	public GroupMap readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
