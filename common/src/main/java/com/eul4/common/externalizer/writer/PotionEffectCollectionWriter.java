package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.common.wrapper.PotionEffectCollection;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;

public class PotionEffectCollectionWriter extends ObjectWriter<PotionEffectCollection>
{
	public PotionEffectCollectionWriter(Writers writers)
	{
		super(writers, PotionEffectCollection.class);
	}
	
	@Override
	protected void writeObject(PotionEffectCollection potionEffectCollection) throws IOException
	{
		out.writeInt(potionEffectCollection.size());
		
		for(PotionEffect potionEffect : potionEffectCollection)
		{
			writers.getWriter(PotionEffectWriter.class).writeReference(potionEffect);
		}
	}
}
