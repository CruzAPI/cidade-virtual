package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.model.inventory.GeneratorGui;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.TownPlayer;
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
public class GeneratorGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof StructureGui structureGui))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onGuiClick(GuiClickEvent event)
	{
		if(!(event.getGui() instanceof GeneratorGui generatorGui)
				|| !(generatorGui.getCommonPlayer() instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		Player player = townPlayer.getPlayer();
		
		InventoryClickEvent inventoryClickEvent = event.getInventoryClickEvent();
		
		final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
		
		if(currentItem == null)
		{
			return;
		}
		
		if(inventoryClickEvent.getClick() == ClickType.LEFT
				&& currentItem.equals(generatorGui.getCollectIcon()))
		{
			generatorGui.getStructure().collect();
			player.closeInventory();
		}
	}
}
