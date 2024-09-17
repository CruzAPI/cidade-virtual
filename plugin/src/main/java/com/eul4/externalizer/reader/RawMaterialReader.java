package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BigDecimalReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.CryptoInfo;
import com.eul4.wrapper.RawMaterial;
import lombok.Getter;
import org.bukkit.Material;

import java.io.IOException;
import java.math.BigDecimal;

public class RawMaterialReader extends ObjectReader<RawMaterial>
{
	@Getter
	private final Reader<RawMaterial> reader;
	private final Readable<RawMaterial> readable;
	
	public RawMaterialReader(Readers readers) throws InvalidVersionException
	{
		super(readers, RawMaterial.class);
		
		final ObjectType objectType = PluginObjectType.RAW_MATERIAL;
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
	
	private RawMaterial readableVersion0() throws IOException, ClassNotFoundException
	{
		CryptoInfoReader cryptoInfoReader = readers.getReader(CryptoInfoReader.class);
		
		Material material = Material.getMaterial(in.readUTF());
		CryptoInfo cryptoInfo = cryptoInfoReader.readReference();
		
		return new RawMaterial(material, cryptoInfo);
	}
	
	public RawMaterial readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
