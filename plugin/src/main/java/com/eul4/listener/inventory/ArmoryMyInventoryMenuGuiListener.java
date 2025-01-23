package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.common.wrapper.Pitch;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.craft.player.CraftInventoryOrganizerPlayer;
import com.eul4.model.inventory.ArmoryMyInventoryMenuGui;
import com.eul4.model.inventory.craft.CraftArmorySelectOrStorageItemsGui;
import com.eul4.model.player.spiritual.InventoryOrganizerPlayer;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.structure.Armory;
import com.eul4.util.SoundUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.Duration;
import java.util.function.BiConsumer;

import static com.eul4.i18n.PluginMessage.*;

@RequiredArgsConstructor
public class ArmoryMyInventoryMenuGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof ArmoryMyInventoryMenuGui))
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
				|| !(guiEvent.getGui() instanceof ArmoryMyInventoryMenuGui armoryMyInventoryMenuGui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null || event.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		Armory armory = armoryMyInventoryMenuGui.getArmory();
		
		if(currentItem.equals(armoryMyInventoryMenuGui.getOrganizeBattleInventoryIcon()))
		{
			if(armory.isBattleInventoryEmpty())
			{
				player.closeInventory();
				SoundUtil.playPlong(player);
				townPlayer.sendMessage(PluginMessage.ORGANIZE_EMPTY_BATTLE_INVENTORY);
				return;
			}
			
			player.closeInventory();
			ItemStack[] contents = armory.getBattleInventoryContents();
			
			BiConsumer<InventoryOrganizerPlayer, PlayerInventory> closeAction = (commonPlayer, playerInventory) ->
			{
				armory.setBattleInventory(playerInventory);
				Player localPlayer = commonPlayer.getPlayer();
				commonPlayer.sendMessage(PluginMessage.BATTLE_INVENTORY_UPDATED);
				localPlayer.playSound(localPlayer, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, Pitch.max());
			};
			
			InventoryOrganizerPlayer newInstnace = new CraftInventoryOrganizerPlayer(townPlayer, contents, closeAction)
			{
				@Override
				public void onSneak()
				{
					SoundUtil.playPiano(this.player);
					this.sendMessage(BATTLE_INVENTORY_UNCHANGED);
				}
			};
			
			if(plugin.getPlayerManager().register(newInstnace) instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer)
			{
				Component mainTitle = PluginMessage.TITLE_OPEN_YOUR_INVENTORY.translate(inventoryOrganizerPlayer);
				Component subTitle = PluginMessage.SUBTITLE_OPEN_YOUR_INVENTORY.translate(inventoryOrganizerPlayer);
				
				Title.Times times = Title.Times.times(
						Duration.ZERO,
						Duration.ofDays(1L),
						Duration.ZERO);
				
				player.showTitle(Title.title(mainTitle, subTitle, times));
			}
		}
		else if(currentItem.equals(armoryMyInventoryMenuGui.getTransferBlocksIcon()))
		{
			//TODO
			player.closeInventory();
			player.sendMessage("//TODO transfer blocks");
		}
		else if(currentItem.equals(armoryMyInventoryMenuGui.getSelectOrStorageItemsIcon()))
		{
			player.closeInventory();
			
			if(armory.isEmpty())
			{
				SoundUtil.playPlong(player);
				townPlayer.sendMessage(PluginMessage.EMPTY_STORAGE_AND_BATTLE_INVENTORY);
				return;
			}
			
			if(armory.isEmptyExcludingExtraSlots())
			{
				SoundUtil.playPlong(player);
				townPlayer.sendMessage(PluginMessage.FAILED_TO_OPEN_STORAGE_AND_BATTLE_INVENTORY);
				return;
			}
			
			ItemStack[] contents = armory.getBattleInventoryContents();
			
			InventoryOrganizerPlayer newInstance = new CraftInventoryOrganizerPlayer(townPlayer,
					contents,
					(commonPlayer, x) -> commonPlayer.sendMessage(STORAGE_AND_BATTLE_INVENTORY_UPDATED));
			
			if(plugin.getPlayerManager().register(newInstance) instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer)
			{
				inventoryOrganizerPlayer.openGui(new CraftArmorySelectOrStorageItemsGui(
						inventoryOrganizerPlayer,
						armory));
			}
		}
	}
}
