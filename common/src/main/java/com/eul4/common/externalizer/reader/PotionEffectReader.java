package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Map;

public class PotionEffectReader extends ObjectReader<PotionEffect>
{
	@Getter
	private final Reader<PotionEffect> reader;
	private final Readable<PotionEffect> readable;
	
	public PotionEffectReader(Readers readers) throws InvalidVersionException
	{
		super(readers, PotionEffect.class);
		
		final ObjectType objectType = CommonObjectType.POTION_EFFECT;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.readable = this::readableVersion1;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	@SuppressWarnings("unchecked")
	private PotionEffect readableVersion0() throws IOException, ClassNotFoundException
	{
		return new PotionEffect((Map<String, Object>) ((ObjectInput) in).readObject());
	}
	
	private PotionEffect readableVersion1() throws IOException, ClassNotFoundException
	{
		return new PotionEffect
		(
			PotionEffectType.getById(in.readInt()),
			in.readInt(),
			in.readInt(),
			in.readBoolean(),
			in.readBoolean(),
			in.readBoolean(),
			this.readReference()
		);
	}
	
	public PotionEffect readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
