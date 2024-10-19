package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.model.inventory.ArmoryWeaponShopGui;
import com.eul4.model.inventory.craft.CraftArmoryWeaponShopGui;
import com.eul4.model.inventory.craft.CraftConfirmationGui;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.service.PurchaseV2;
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

import static com.eul4.i18n.PluginMessage.WEAPON_PURCHASE;
import static com.eul4.i18n.PluginMessage.WEAPON_PURCHASE_FAILED_STORAGE_FULL;
import static net.kyori.adventure.text.Component.translatable;

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
			SoundUtil.playPlong(player);
			return;
		}
		
		PurchaseV2 purchase = new PurchaseV2(townPlayer, icon.getCost(), () ->
		{
			if(armoryWeaponShopGui
					.getArmory()
					.addItemToStorage(icon.getWeapon())
					.isEmpty())
			{
				return true;
			}
			
			SoundUtil.playPlong(player);
			townPlayer.sendMessage(WEAPON_PURCHASE_FAILED_STORAGE_FULL);
			return false;
		});
		
		if(!purchase.isAffordable(true))
		{
			player.closeInventory();
			return;
		}
		
		townPlayer.openGui(new CraftConfirmationGui(townPlayer)
		{
			@Override
			public void confirm()
			{
				boolean wasExecuted = purchase.tryExecutePurchase();
				
				if(wasExecuted)
				{
					townPlayer.sendMessage(WEAPON_PURCHASE, translatable(icon.getType().translationKey()));
					SoundUtil.playPlingPlong(player, plugin);
				}
			}
			
			@Override
			public void cancel()
			{
				SoundUtil.playPlong(player);
				townPlayer.openGui(armoryWeaponShopGui);
			}
		});
	}
}
