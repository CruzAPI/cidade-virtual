package com.eul4.common.externalizer.filer;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class Filer
{
	protected final Common plugin;
	private final byte version;
	
	protected Map<ObjectType, Byte> readVersions(DataInput in) throws IOException, InvalidVersionException
	{
		Map<ObjectType, Byte> versions = new HashMap<>();
		
		for(ObjectType objectType : getObjectTypes(in.readByte()))
		{
			versions.put(objectType, in.readByte());
		}
		
		return versions;
	}
	
	protected ObjectType[] writeVersions(DataOutput out) throws IOException, InvalidVersionException
	{
		out.writeByte(version);
		
		ObjectType[] objectTypes = getObjectTypes(version);
		
		for(ObjectType objectType : objectTypes)
		{
			out.writeByte(objectType.getVersion());
		}
		
		return objectTypes;
	}
	
	protected boolean isObjectStream(byte[] header)
	{
		return header.length >= 2 && header[0] == (byte) 0xAC && header[1] == (byte) 0xED;
	}
	
	protected abstract ObjectType[] getObjectTypes(byte version) throws InvalidVersionException;
}
