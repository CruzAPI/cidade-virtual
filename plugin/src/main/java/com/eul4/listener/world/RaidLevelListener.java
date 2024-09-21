package com.eul4.listener.world;

import com.eul4.Main;
import com.eul4.event.BlockDataLoadEvent;
import com.eul4.service.BlockData;
import com.eul4.type.PluginWorldType;
import com.eul4.util.OreVeinUtil;
import com.eul4.world.RaidLevel;
import com.eul4.world.VanillaLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

@RequiredArgsConstructor
public class RaidLevelListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void rarifyVeinOnBlockDataLoad(BlockDataLoadEvent event)
	{
		Block block = event.getBlock();
		
		if(!(plugin.getWorldManager().get(block.getWorld()) instanceof RaidLevel))
		{
			return;
		}
		
		if(event.getCause() == BlockDataLoadEvent.Cause.ORE_VEIN)
		{
			return;
		}
		
		OreVeinUtil.rarifyVein(plugin, block);
		BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
		
		if(blockData != null)
		{
			event.setBlockData(blockData);
		}
	}
}
