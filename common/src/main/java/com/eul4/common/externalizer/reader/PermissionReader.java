package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.permission.Permission;
import com.eul4.common.model.permission.TimedTick;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class PermissionReader extends ObjectReader<Permission>
{
	@Getter
	private final Reader<Permission> reader;
	private final Readable<Permission> readable;
	
	public PermissionReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Permission.class);
		
		final ObjectType objectType = CommonObjectType.PERMISSION;
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
	
	private Permission readableVersion0() throws IOException, ClassNotFoundException
	{
		String name = in.readUTF();
		TimedTick timedTick = readers.getReader(TimedTickReader.class).readReference();
		
		return Permission.builder()
				.name(name)
				.timedTick(timedTick)
				.build();
	}
	
	public Permission readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
