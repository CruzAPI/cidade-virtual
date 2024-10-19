package com.eul4.listener;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.ItemBuilder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureShopGui;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.util.SoundUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class StructureShopGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof StructureShopGui))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!(commonPlayer.getGui() instanceof StructureShopGui gui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		if(!(commonPlayer instanceof TownPlayer townPlayer) || !townPlayer.hasTown())
		{
			return;
		}
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null || event.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		if(gui.hasItemBuilder(currentItem))
		{
			player.closeInventory();
			
			ItemBuilder itemBuilder = gui.getItemBuilder(currentItem);
			StructureType structureType = itemBuilder.getStructureType();
			Town town = townPlayer.getTown();
			
			if(town.hasReachedStructureLimit(structureType))
			{
				SoundUtil.playPlong(player);
				townPlayer.sendMessage(PluginMessage.STRUCTURE_LIMIT_REACHED, itemBuilder.getStructureType(),
						town.countStructures(structureType), town.getStructureLimit(structureType));
				return;
			}
			
			player.getInventory().addItem(itemBuilder.getItem(townPlayer));
		}
	}
}
