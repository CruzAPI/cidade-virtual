package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class PotionEffectCollectionReader extends ObjectReader<Collection<PotionEffect>>
{
	private final Reader<Collection<PotionEffect>> reader;
	private final Readable<Collection<PotionEffect>> readable;
	
	public PotionEffectCollectionReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = CommonObjectType.POTION_EFFECT_COLLECTION;
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
	
	private Collection<PotionEffect> readableVersion0() throws IOException, ClassNotFoundException
	{
		int length = in.readInt();
		
		Collection<PotionEffect> potionEffectCollection = new ArrayList<>();
		
		for(int i = 0; i < length; i++)
		{
			potionEffectCollection.add(readers.getReader(PotionEffectReader.class).readReference());
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
