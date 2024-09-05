package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

public class BucketRarityListener implements Listener
{
	private final Main plugin;
	
	private final BlockDataFiler blockDataFiler;
	
	public BucketRarityListener(Main plugin)
	{
		this.plugin = plugin;
		
		this.blockDataFiler = plugin.getBlockDataFiler();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBucketEmpty(PlayerBucketEmptyEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		ItemStack item = player.getEquipment().getItem(event.getHand());
		Rarity rarity = RarityUtil.getRarity(item);
		
		ItemStack resultItem = event.getItemStack();
		RarityUtil.setRarity(resultItem, rarity);
		
		BlockData blockData = blockDataFiler.loadBlockDataOrDefault(block);
		blockData.setRarity(rarity);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBucketFill(PlayerBucketFillEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		ItemStack item = player.getEquipment().getItem(event.getHand());
		Rarity itemRarity = RarityUtil.getRarity(item);
		Rarity blockRarity = RarityUtil.getRarity(plugin, block);
		
		if(itemRarity != blockRarity)
		{
			PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
			pluginPlayer.sendMessage(PluginMessage.INCOMPATIBLE_RARITY);
			event.setCancelled(true);
			return;
		}
		
		ItemStack resultItem = event.getItemStack();
		RarityUtil.setRarity(resultItem, itemRarity);
	}
}
