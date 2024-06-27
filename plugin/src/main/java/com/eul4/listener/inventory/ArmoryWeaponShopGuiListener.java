package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.common.wrapper.Pitch;
import com.eul4.model.inventory.ArmoryWeaponShopGui;
import com.eul4.model.inventory.craft.CraftArmoryWeaponShopGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.service.PurchaseV2;
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
	public void onGuiClick(GuiClickEvent guiEvent)
	{
		InventoryClickEvent event = guiEvent.getInventoryClickEvent();
		
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !(guiEvent.getGui() instanceof ArmoryWeaponShopGui armoryWeaponShopGui))
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
		
		//TODO confirm operation
		PurchaseV2 purchase = new PurchaseV2(townPlayer, icon.getCost(), () -> armoryWeaponShopGui
				.getArmory()
				.addItemToStorage(icon.getWeapon())
				.isEmpty());
		
		boolean wasExecuted = purchase.tryExecutePurchase();
		player.closeInventory();
	}
}
