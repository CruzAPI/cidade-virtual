package com.eul4.common.wrapper;

import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

public class PotionEffectCollection extends ArrayList<PotionEffect>
{
	public PotionEffectCollection()
	{
		super();
	}
	
	public PotionEffectCollection(Collection<PotionEffect> collection)
	{
		super(collection);
	}
}
