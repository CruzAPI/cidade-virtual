package com.eul4.wrapper;

import com.eul4.Main;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DerivativeMaterial extends EconomicMaterial
{
	private final List<EconomicMaterialMultiplier> economicMaterialMultipliers;
	
	protected DerivativeMaterial(Main plugin, Material material, List<EconomicMaterialMultiplier> economicMaterialMultipliers)
	{
		super(plugin, material);
		
		Preconditions.checkArgument(!economicMaterialMultipliers.isEmpty());
		
		this.economicMaterialMultipliers = economicMaterialMultipliers;
	}
	
	private List<Trade> createTrades(BigDecimal multiplier)
	{
		return createTrades(new ArrayList<>(), multiplier);
	}
	
	private List<Trade> createTrades(List<Trade> trades, BigDecimal baseMultiplier)
	{
		for(EconomicMaterialMultiplier economicMaterialMultiplier : economicMaterialMultipliers)
		{
			EconomicMaterial economicMaterial = economicMaterialMultiplier.economicMaterial;
			BigDecimal multiplier = baseMultiplier.multiply(economicMaterialMultiplier.multiplier, CryptoInfo.MATH_CONTEXT);
			
			if(economicMaterial instanceof DerivativeMaterial derivativeMaterial)
			{
				derivativeMaterial.createTrades(trades, multiplier);
			}
			else if(economicMaterial instanceof RawMaterial rawMaterial)
			{
				trades.add(rawMaterial.createTrade(multiplier));
			}
		}
		
		return trades;
	}
}
