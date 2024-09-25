package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;

public class PotionEffectWriter extends ObjectWriter<PotionEffect>
{
	public PotionEffectWriter(Writers writers)
	{
		super(writers, PotionEffect.class);
	}
	
	@Override
	protected void writeObject(PotionEffect potionEffect) throws IOException
	{
		out.writeInt(potionEffect.getType().getId());
		out.writeInt(potionEffect.getDuration());
		out.writeInt(potionEffect.getAmplifier());
		out.writeBoolean(potionEffect.isAmbient());
		out.writeBoolean(potionEffect.hasParticles());
		out.writeBoolean(potionEffect.hasIcon());
		this.writeReference(potionEffect.getHiddenPotionEffect());
	}
}
