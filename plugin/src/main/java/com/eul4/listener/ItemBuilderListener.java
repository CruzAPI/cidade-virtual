package com.eul4.listener;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.enums.ItemBuilder;
import com.eul4.model.inventory.craft.CraftConfirmationGui;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ItemBuilderListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Block clickedBlock = event.getClickedBlock();
		ItemStack item = event.getItem();
		
		if(clickedBlock == null || item == null)
		{
			return;
		}
		
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		Runnable cancelRunnable = () -> player.getInventory().remove(item);
		
		ItemBuilder.findItemBuilder(item)
				.map(ItemBuilder::getStructureType)
				.map(structureType -> getConfirmRunnable(townPlayer, clickedBlock, structureType, item))
				.ifPresent(confirmRunnable -> townPlayer.openGui(new CraftConfirmationGui(townPlayer, confirmRunnable, cancelRunnable)));
	}
	
	private Runnable getConfirmRunnable(TownPlayer townPlayer, Block clickedBlock, StructureType structureType, ItemStack item)
	{
		return () ->
		{
			if(plugin.getBuyStructureCommand().executeBuy(townPlayer, clickedBlock, structureType))
			{
				townPlayer.getPlayer().getInventory().remove(item);
			}
		};
	}
}
