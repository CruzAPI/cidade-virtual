package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.model.inventory.ArmoryGui;
import com.eul4.model.inventory.ArmoryMenuGui;
import com.eul4.model.inventory.craft.CraftArmoryMenuGui;
import com.eul4.model.inventory.craft.CraftArmoryWeaponShopGui;
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
public class ArmoryGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !(townPlayer.getGui() instanceof ArmoryGui armoryGui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null || event.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		if(currentItem.equals(armoryGui.getMenuIcon()))
		{
			townPlayer.openGui(new CraftArmoryMenuGui(townPlayer));
		}
	}
}
