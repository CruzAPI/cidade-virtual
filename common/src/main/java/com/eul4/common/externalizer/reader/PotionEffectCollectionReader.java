package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.PotionEffectCollection;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;

public class PotionEffectCollectionReader extends ObjectReader<PotionEffectCollection>
{
	@Getter
	private final Reader<PotionEffectCollection> reader;
	private final Readable<PotionEffectCollection> readable;
	
	public PotionEffectCollectionReader(Readers readers) throws InvalidVersionException
	{
		super(readers, PotionEffectCollection.class);
		
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
	
	private PotionEffectCollection readableVersion0() throws IOException, ClassNotFoundException
	{
		int length = in.readInt();
		
		PotionEffectCollection potionEffectCollection = new PotionEffectCollection();
		
		for(int i = 0; i < length; i++)
		{
			potionEffectCollection.add(readers.getReader(PotionEffectReader.class).readReference());
		}
		
		return potionEffectCollection;
	}
	
	public PotionEffectCollection readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
