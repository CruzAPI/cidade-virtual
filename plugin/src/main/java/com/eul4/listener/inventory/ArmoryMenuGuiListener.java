package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.model.inventory.ArmoryMenuGui;
import com.eul4.model.inventory.craft.CraftArmoryMyInventoryMenuGui;
import com.eul4.model.inventory.craft.CraftArmoryWeaponShopGui;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ArmoryMenuGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof ArmoryMenuGui))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(GuiClickEvent guiEvent)
	{
		InventoryClickEvent event = guiEvent.getInventoryClickEvent();
		
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !(guiEvent.getGui() instanceof ArmoryMenuGui armoryMenuGui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null || event.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		if(currentItem.equals(armoryMenuGui.getShopIcon()))
		{
			townPlayer.openGui(new CraftArmoryWeaponShopGui(townPlayer, armoryMenuGui.getArmory()));
		}
		else if(currentItem.equals(armoryMenuGui.getMyInventoryIcon()))
		{
			townPlayer.openGui(new CraftArmoryMyInventoryMenuGui(townPlayer, armoryMenuGui.getArmory()));
		}
	}
}
