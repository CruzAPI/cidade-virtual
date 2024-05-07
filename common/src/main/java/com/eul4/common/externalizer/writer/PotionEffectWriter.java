package com.eul4.common.externalizer.writer;

import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectOutput;

public class PotionEffectWriter extends ObjectWriter<PotionEffect>
{
	public PotionEffectWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(PotionEffect potionEffect) throws IOException
	{
		out.writeObject(potionEffect.serialize());
	}
}
