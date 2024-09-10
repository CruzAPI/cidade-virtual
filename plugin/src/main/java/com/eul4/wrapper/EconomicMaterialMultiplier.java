package com.eul4.wrapper;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

public class EconomicMaterialMultiplier
{
	protected final EconomicMaterial economicMaterial;
	protected final BigDecimal multiplier;
	
	public EconomicMaterialMultiplier(EconomicMaterial economicMaterial, BigDecimal multiplier)
	{
		Preconditions.checkArgument(multiplier.compareTo(BigDecimal.ZERO) > 0);
		
		this.economicMaterial = economicMaterial;
		this.multiplier = multiplier;
	}
}
