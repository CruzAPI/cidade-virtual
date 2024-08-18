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
import java.util.UUID;

public class UUIDReader extends ObjectReader<UUID>
{
	@Getter
	private final Reader<UUID> reader;
	private final Readable<UUID> readable;
	
	public UUIDReader(Readers readers) throws InvalidVersionException
	{
		super(readers, UUID.class);
		
		final ObjectType objectType = CommonObjectType.UUID;
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
	
	private UUID readableVersion0() throws IOException, ClassNotFoundException
	{
		return new UUID(in.readLong(), in.readLong());
	}
	
	public UUID readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
