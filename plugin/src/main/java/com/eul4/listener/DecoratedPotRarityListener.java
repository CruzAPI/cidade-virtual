package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.enums.Rarity;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DecoratedPotRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelGreaterRarityItemStorageThanDecoratedPot(PlayerInteractEvent event)
	{
		Block clickedBlock = event.getClickedBlock();
		ItemStack item = event.getItem();
		Player player = event.getPlayer();
		
		if(event.getHand() != EquipmentSlot.HAND
				|| event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| clickedBlock == null
				|| clickedBlock.getType() != Material.DECORATED_POT
				|| item == null
				|| player.isSneaking())
		{
			return;
		}
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		Rarity potRarity = RarityUtil.getRarity(plugin, clickedBlock);
		
		if(itemRarity.compareTo(potRarity) > 0)
		{
			event.setCancelled(true);
			PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
			MessageArgs messageArgs = itemRarity
					.getContainerIncompatibilityMessage()
					.withArgs(Component.translatable(clickedBlock.getType().translationKey()));
			pluginPlayer.sendMessage(messageArgs);
		}
	}
}
