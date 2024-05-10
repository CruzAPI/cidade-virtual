package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Map;

public class PotionEffectReader extends ObjectReader<PotionEffect>
{
	private final Reader<PotionEffect> reader;
	private final Readable<PotionEffect> readable;
	
	public PotionEffectReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getPotionEffectVersion() == 0)
		{
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid PotionEffect version: " + commonVersions.getPotionEffectVersion());
		}
	}
	
	@SuppressWarnings("unchecked")
	private PotionEffect readableVersion0() throws IOException, ClassNotFoundException
	{
		return new PotionEffect((Map<String, Object>) in.readObject());
	}
	
	public PotionEffect readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	@Override
	protected PotionEffect readObject(PotionEffect potionEffect) throws IOException, ClassNotFoundException
	{
		return reader.readObject(potionEffect);
	}
}
