package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.wrapper.Pitch;
import com.eul4.model.inventory.ArmoryMenuGui;
import com.eul4.model.inventory.ArmoryWeaponShopGui;
import com.eul4.model.inventory.craft.CraftArmoryWeaponShopGui;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ArmoryWeaponShopGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof ArmoryWeaponShopGui))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !(townPlayer.getGui() instanceof ArmoryWeaponShopGui armoryWeaponShopGui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null || event.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		CraftArmoryWeaponShopGui.Icon icon = armoryWeaponShopGui.getBySlot(event.getSlot());
		
		if(icon == null)
		{
			return;
		}
		
		if(armoryWeaponShopGui.isLocked(icon))
		{
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, Pitch.getPitch(3));
			return;
		}
		//TODO...
	}
}
