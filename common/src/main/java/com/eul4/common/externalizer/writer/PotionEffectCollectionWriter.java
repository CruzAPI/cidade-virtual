package com.eul4.common.externalizer.writer;

import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Collection;

public class PotionEffectCollectionWriter extends ObjectWriter<Collection<PotionEffect>>
{
	private final PotionEffectWriter potionEffectWriter;
	
	public PotionEffectCollectionWriter(ObjectOutput out)
	{
		super(out);
		
		this.potionEffectWriter = new PotionEffectWriter(out);
	}
	
	@Override
	protected void writeObject(Collection<PotionEffect> potionEffects) throws IOException
	{
		out.writeInt(potionEffects.size());
		
		for(PotionEffect potionEffect : potionEffects)
		{
			potionEffectWriter.writeReference(potionEffect);
		}
	}
}
