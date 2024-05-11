package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.Collection;

public class PotionEffectCollectionWriter extends ObjectWriter<Collection<PotionEffect>>
{
	public PotionEffectCollectionWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Collection<PotionEffect> potionEffects) throws IOException
	{
		out.writeInt(potionEffects.size());
		
		for(PotionEffect potionEffect : potionEffects)
		{
			writers.getWriter(PotionEffectWriter.class).writeReference(potionEffect);
		}
	}
}
