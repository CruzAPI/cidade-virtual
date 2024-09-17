package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.CryptoInfo;
import com.eul4.wrapper.RawMaterial;
import com.eul4.wrapper.RawMaterialMap;
import lombok.Getter;
import org.bukkit.Material;

import java.io.IOException;

public class RawMaterialMapReader extends ObjectReader<RawMaterialMap>
{
	@Getter
	private final Reader<RawMaterialMap> reader;
	private final Readable<RawMaterialMap> readable;
	
	public RawMaterialMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, RawMaterialMap.class);
		
		final ObjectType objectType = PluginObjectType.RAW_MATERIAL_MAP;
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
	
	private RawMaterialMap readableVersion0() throws IOException, ClassNotFoundException
	{
		RawMaterialReader rawMaterialReader = readers.getReader(RawMaterialReader.class);
		
		RawMaterialMap rawMaterialMap = new RawMaterialMap();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			rawMaterialMap.put(rawMaterialReader.readReference());
		}
		
		return rawMaterialMap;
	}
	
	public RawMaterialMap readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
