package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.Collection;

public class PotionEffectCollectionReader extends ObjectReader<Collection<PotionEffect>>
{
	private final Reader<Collection<PotionEffect>> reader;
	private final Readable<Collection<PotionEffect>> readable;
	
	private final PotionEffectReader potionEffectReader;
	
	public PotionEffectCollectionReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.potionEffectReader = new PotionEffectReader(in, commonVersions);
		
		if(commonVersions.getPotionEffectCollectionVersion() == 0)
		{
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid PotionEffectCollection version: " + commonVersions.getPotionEffectCollectionVersion());
		}
	}
	
	private Collection<PotionEffect> readableVersion0() throws IOException, ClassNotFoundException
	{
		int length = in.readInt();
		
		Collection<PotionEffect> potionEffectCollection = new ArrayList<>();
		
		for(int i = 0; i < length; i++)
		{
			potionEffectCollection.add(potionEffectReader.readReference());
		}
		
		return potionEffectCollection;
	}
	
	public Collection<PotionEffect> readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	@Override
	protected Collection<PotionEffect> readObject(Collection<PotionEffect> potionEffectCollection) throws IOException, ClassNotFoundException
	{
		return reader.readObject(potionEffectCollection);
	}
}
