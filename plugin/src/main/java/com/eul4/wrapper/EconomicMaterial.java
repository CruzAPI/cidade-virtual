package com.eul4.wrapper;

import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;

public abstract class EconomicMaterial
{
	@Getter
	protected final Material material;
	
	protected EconomicMaterial(Material material)
	{
		this.material = material;
	}
	
	public EconomicMaterialMultiplier withMultiplier(double multiplier)
	{
		return withMultiplier(BigDecimal.valueOf(multiplier));
	}
	
	public EconomicMaterialMultiplier withMultiplier(BigDecimal multiplier)
	{
		return new EconomicMaterialMultiplier(this, multiplier);
	}
}
