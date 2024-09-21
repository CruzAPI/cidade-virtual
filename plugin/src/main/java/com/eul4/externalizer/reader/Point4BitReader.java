package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;

public class Point4BitReader extends ObjectReader<Point>
{
	@Getter
	private final Reader<Point> reader;
	private final Readable<Point> readable;
	
	public Point4BitReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Point.class);
		
		final ObjectType objectType = PluginObjectType.POINT_4_BIT;
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
	
	private Point readableVersion0() throws IOException
	{
		byte compact = in.readByte();
		return new Point((compact & 0xF0) >> 4, (compact & 0x0F));
	}
	
	public Point readObject() throws IOException, ClassNotFoundException
	{
		Point point = readable.read();
		reader.readObject(point);
		return point;
	}
	
	public Point readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
