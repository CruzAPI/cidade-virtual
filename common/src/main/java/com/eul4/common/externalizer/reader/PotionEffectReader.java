package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Map;

public class PotionEffectReader extends ObjectReader<PotionEffect>
{
	private final Reader<PotionEffect> reader;
	
	public PotionEffectReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getPotionEffectVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid PotionEffect version: " + commonVersions.getPotionEffectVersion());
		}
	}
	
	@SuppressWarnings("unchecked")
	private PotionEffect readerVersion0() throws IOException, ClassNotFoundException
	{
		return new PotionEffect((Map<String, Object>) in.readObject());
	}
	
	@Override
	protected PotionEffect readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
