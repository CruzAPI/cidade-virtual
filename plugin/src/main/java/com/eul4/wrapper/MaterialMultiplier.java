package com.eul4.wrapper;

import com.eul4.service.MarketDataManager;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;

@Getter
public class MaterialMultiplier
{
	private final Material material;
	private final BigDecimal multiplier;
	
	public MaterialMultiplier(Material material)
	{
		this(material, 1.0F);
	}
	
	public MaterialMultiplier(Material material, float multiplier)
	{
		if(!MarketDataManager.RAW_DATA.containsKey(material))
		{
			throw new IllegalArgumentException("RAW_DATA doesn't contain " + material);
		}
		
		this.material = material;
		this.multiplier = BigDecimal.valueOf(multiplier);
	}
}