package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class Filer
{
	protected final Main plugin;
	private final byte version;
	
	protected Map<ObjectType, Byte> readVersions(ObjectInput in) throws IOException, InvalidVersionException
	{
		Map<ObjectType, Byte> versions = new HashMap<>();
		
		for(ObjectType objectType : getObjectTypes(in.readByte()))
		{
			versions.put(objectType, in.readByte());
		}
		
		return versions;
	}
	
	protected ObjectType[] writeVersions(ObjectOutput out) throws IOException, InvalidVersionException
	{
		out.writeByte(version);
		
		ObjectType[] objectTypes = getObjectTypes(version);
		
		for(ObjectType objectType : objectTypes)
		{
			out.writeByte(objectType.getVersion());
		}
		
		return objectTypes;
	}
	
	protected abstract ObjectType[] getObjectTypes(byte version) throws InvalidVersionException;
}
