package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FishingRarityListener implements Listener
{
	private final Main plugin;
	
	public FishingRarityListener(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerFishEvent(PlayerFishEvent event)
	{
		Player player = event.getPlayer();
		EquipmentSlot hand = event.getHand();
		
		if(hand == null || !(event.getCaught() instanceof Item item))
		{
			return;
		}
		
		Block block = event.getHook().getLocation().getBlock();
		ItemStack fishingRod = player.getEquipment().getItem(event.getHand());
		
		Rarity blockRarity = plugin.getBlockDataFiler().loadBlockDataOrDefault(block).getRarity();
		Rarity fishingRodRarity = RarityUtil.getRarity(fishingRod);
		Rarity minRarity = Rarity.getMinRarity(blockRarity, fishingRodRarity);
		
		ItemStack itemStack = item.getItemStack();
		RarityUtil.setRarity(itemStack, minRarity);
		itemStack = EnchantmentListener.reenchant(itemStack);
		item.setItemStack(itemStack);
	}
}
