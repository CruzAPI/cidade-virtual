package com.eul4.wrapper;

import com.eul4.Main;
import com.eul4.service.MarketDataManager;
import org.bukkit.Material;

public abstract class EconomicMaterial
{
	protected final Main plugin;
	protected final MarketDataManager marketDataManager;
	protected final Material material;
	
	protected EconomicMaterial(Main plugin, Material material)
	{
		this.plugin = plugin;
		this.marketDataManager = plugin.getMarketDataManager();
		this.material = material;
	}
}
