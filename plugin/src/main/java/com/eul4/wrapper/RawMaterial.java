package com.eul4.wrapper;

import com.eul4.Main;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;

@Getter
public class RawMaterial extends EconomicMaterial
{
	private final CryptoInfo cryptoInfo;
	
	public RawMaterial(Main plugin, Material material, CryptoInfo cryptoInfo)
	{
		super(plugin, material);
		this.cryptoInfo = Preconditions.checkNotNull(cryptoInfo);
	}
	
	public Trade createTrade(BigDecimal multiplier)
	{
		return new Trade(cryptoInfo, multiplier);
	}
}
