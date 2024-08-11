package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.PermissionMap;
import com.eul4.common.model.permission.User;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;

public class UserReader extends ObjectReader<User>
{
	@Getter
	private final Reader<User> reader;
	private final Readable<User> readable;
	
	public UserReader(Readers readers) throws InvalidVersionException
	{
		super(readers, User.class);
		
		final ObjectType objectType = CommonObjectType.USER;
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
	
	private User readableVersion0() throws IOException, ClassNotFoundException
	{
		UUID uuid = new UUID(in.readLong(), in.readLong());
		PermissionMap permissionMap = readers.getReader(PermissionMapReader.class).readReference();
		
		return User.builder()
				.uuid(uuid)
				.permissionMap(permissionMap)
				.build();
	}
	
	public User readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
