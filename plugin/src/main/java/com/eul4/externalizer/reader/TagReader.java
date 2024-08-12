package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.Tag;
import lombok.Getter;

import java.io.IOException;

@Getter
public class TagReader extends ObjectReader<Tag>
{
	private final Reader<Tag> reader;
	private final Readable<Tag> readable;
	
	public TagReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Tag.class);
		
		final ObjectType objectType = PluginObjectType.TAG;
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
	
	private Tag readableVersion0() throws IOException
	{
		return Tag.valueOf(in.readUTF());
	}
	
	public Tag readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
