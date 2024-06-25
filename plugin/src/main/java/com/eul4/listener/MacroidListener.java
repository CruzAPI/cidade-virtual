package com.eul4.listener;

import com.eul4.Main;
import com.eul4.wrapper.Macroid;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
public class MacroidListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerDropItemEvent event)
	{
		ItemStack item = event.getItemDrop().getItemStack();
		UUID macroidUUID = plugin.getMacroidService().getMacroidWandUUID(item);
		
		if(macroidUUID == null)
		{
			return;
		}
		
		event.setCancelled(false);
		event.getItemDrop().remove();
		
		Player player = event.getPlayer();
		
		if(plugin.getMacroidService().cancel(macroidUUID))
		{
			player.sendMessage("Macroid cancelled.");
		}
		else
		{
			player.sendMessage("Macroid could not be cancelled.");
		}
	}
	
	@EventHandler
	public void on(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		ItemStack item = event.getItem();
		
		if(item == null)
		{
			return;
		}
		
		UUID macroidUUID = plugin.getMacroidService().getMacroidWandUUID(item);
		
		if(macroidUUID == null)
		{
			return;
		}
		
		Macroid macroid = plugin.getMacroidService().getMacroid(macroidUUID);
		
		if(macroid == null)
		{
			return;
		}
		
		if(action == Action.LEFT_CLICK_AIR)
		{
			macroid.saveSelector();
			return;
		}
		
		if(action == Action.RIGHT_CLICK_AIR)
		{
			plugin.getMacroidService().finish(macroidUUID, macroid);
			return;
		}
		
		Block clickedBlock = event.getClickedBlock();
		
		if(clickedBlock == null)
		{
			return;
		}
		
		Player player = event.getPlayer();
		
		if(action == Action.LEFT_CLICK_BLOCK)
		{
			macroid.getSelector()
					.selectPrimary(BlockVector3.at(
							clickedBlock.getX(),
							clickedBlock.getY(),
							clickedBlock.getZ()), null);
			player.sendMessage("pos1 set!");
			return;
		}
		
		if(action == Action.RIGHT_CLICK_BLOCK)
		{
			macroid.getSelector()
					.selectSecondary(BlockVector3.at(
							clickedBlock.getX(),
							clickedBlock.getY(),
							clickedBlock.getZ()), null);
			player.sendMessage("pos2 set!");
			return;
		}
	}
}
