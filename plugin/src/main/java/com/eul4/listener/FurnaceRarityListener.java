package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class FurnaceRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(FurnaceBurnEvent event)
	{
		Block block = event.getBlock();
		
		ItemStack fuel = event.getFuel();
		Rarity fuelRarity = RarityUtil.getRarity(fuel);
		
		if(block.getState() instanceof Furnace furnace)
		{
			double cookSpeedMultiplier = Math.max(0.0D, Math.min(200.0D, fuelRarity.getScalarMultiplier(5.0D)));
			furnace.setCookSpeedMultiplier(cookSpeedMultiplier);
			furnace.update();
		}
	}
}
