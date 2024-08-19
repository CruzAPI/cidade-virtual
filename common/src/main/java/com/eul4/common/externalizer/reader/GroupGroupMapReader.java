package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.GroupGroupMap;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class GroupGroupMapReader extends ObjectReader<GroupGroupMap>
{
	@Getter
	private final Reader<GroupGroupMap> reader;
	private final Readable<GroupGroupMap> readable;
	
	public GroupGroupMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, GroupGroupMap.class);
		
		final ObjectType objectType = CommonObjectType.GROUP_GROUP_MAP;
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
	
	private GroupGroupMap readableVersion0() throws IOException, ClassNotFoundException
	{
		GroupGroupReader groupGroupReader = readers.getReader(GroupGroupReader.class);
		GroupGroupMap groupGroupMap = new GroupGroupMap();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			groupGroupMap.put(groupGroupReader.readReference());
		}
		
		return groupGroupMap;
	}
	
	public GroupGroupMap readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
