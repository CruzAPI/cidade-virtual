package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.PermissionMap;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class PermissionMapReader extends ObjectReader<PermissionMap>
{
	@Getter
	private final Reader<PermissionMap> reader;
	private final Readable<PermissionMap> readable;
	
	public PermissionMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, PermissionMap.class);
		
		final ObjectType objectType = CommonObjectType.PERMISSION_MAP;
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
	
	private PermissionMap readableVersion0() throws IOException, ClassNotFoundException
	{
		PermissionReader permissionReader = readers.getReader(PermissionReader.class);
		PermissionMap permissionMap = new PermissionMap();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			permissionMap.put(permissionReader.readReference());
		}
		
		return permissionMap;
	}
	
	public PermissionMap readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
