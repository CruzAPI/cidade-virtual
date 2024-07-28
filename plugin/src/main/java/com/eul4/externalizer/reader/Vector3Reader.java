package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.sk89q.worldedit.math.Vector3;
import lombok.Getter;

import java.io.IOException;

public class Vector3Reader extends ObjectReader<Vector3>
{
	@Getter
	private final Reader<Vector3> reader;
	private final Readable<Vector3> readable;
	
	public Vector3Reader(Readers readers) throws InvalidVersionException
	{
		super(readers, Vector3.class);
		
		final ObjectType objectType = PluginObjectType.VECTOR_3;
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
	
	private Vector3 readableVersion0() throws IOException
	{
		return Vector3.at(in.readDouble(), in.readDouble(), in.readDouble());
	}
	
	public Vector3 readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
