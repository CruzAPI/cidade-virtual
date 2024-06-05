package com.eul4.service;

import com.eul4.Main;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.town.Town;
import com.eul4.type.PluginWorldType;
import com.eul4.wrapper.TownMap;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class TownManager
{
	private final Main plugin;
	
	@Getter
	private TownMap towns;
	
	public void loadTowns() throws Exception
	{
		if(towns != null)
		{
			throw new Exception("Towns already loaded.");
		}
		
		this.towns = plugin.getTownsFiler().loadTownsFromDisk();
		this.towns.values().forEach(Town::load);
	}
	
	public Town getOrCreateNewTown(UUID uuid) throws CannotConstructException, IOException
	{
		if(towns.containsKey(uuid))
		{
			return towns.get(uuid);
		}
		
		Town town = createNewTown(uuid);
		towns.put(uuid, town);
		return town;
	}
	
	private Town createNewTown(UUID uuid) throws CannotConstructException, IOException
	{
		Location location = findNextEmptyTown();
		BlockVector3 to = BlockVector3.at(location.getX(), location.getY() + 1, location.getZ());
		
		try
		{
			File file = new File("plugins/FastAsyncWorldEdit/schematics", "basis.schem");
			
			var world = FaweAPI.getWorld(PluginWorldType.TOWN_WORLD.getInstance().getWorld().getName());
			
			ClipboardFormat format = ClipboardFormats.findByFile(file);
			ClipboardReader reader = null;
			try
			{
				reader = format.getReader(new FileInputStream(file));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			Clipboard clipboard = null;
			
			try
			{
				clipboard = reader.read();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			try(EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1))
			{
				Operation operation = new ClipboardHolder(clipboard)
						.createPaste(editSession)
						.to(to)
						.ignoreAirBlocks(false)
						.build();
				
				Operations.complete(operation);
			}
		}
		catch(WorldEditException e)
		{
			throw new RuntimeException(e);
		}
		
		return new CraftTown(plugin.getServer().getOfflinePlayer(uuid), location.getBlock(), plugin);
	}
	
	public Location findNextEmptyTown()
	{
		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;
		
		for(;;)
		{
			Block block = PluginWorldType.TOWN_WORLD.getInstance().getWorld()
					.getBlockAt(x * Town.TOWN_FULL_DIAMATER, Town.Y, z * Town.TOWN_FULL_DIAMATER);
			
			if(Town.findStaticTownBlock(block).isEmpty())
			{
				return block.getLocation();
			}
			
			if(x == z || x < 0 && x == -z || x > 0 && x == 1 - z)
			{
				int temp = dx;
				dx = -dz;
				dz = temp;
			}
			
			x += dx;
			z += dz;
		}
	}
	
	public Town getTown(UUID uuid)
	{
		return this.towns.get(uuid);
	}
	
	public void reloadTowns()
	{
		if(towns == null)
		{
			return;
		}
		
		towns.values().forEach(Town::reloadAllStructureAttributes);
	}
}
