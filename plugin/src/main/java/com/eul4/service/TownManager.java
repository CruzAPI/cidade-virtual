package com.eul4.service;

import com.eul4.Main;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.town.Town;
import com.fastasyncworldedit.core.FaweAPI;
import com.google.common.io.ByteStreams;
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

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class TownManager
{
	private final Main plugin;
	
	@Getter
	private Map<UUID, Town> towns;
	
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
	
	private void loadTowns() throws IOException, ClassNotFoundException
	{
		if(towns != null)
		{
			return;
		}
		
		final File file = plugin.getDataFileManager().getTownsFile();
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getServer().getLogger().warning("No towns found to load!");
			towns = new HashMap<>();
			return;
		}
		
		plugin.getLogger().info("towns.dat length: " + file.length());
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileInputStream));
				ObjectInputStream in = new ObjectInputStream(byteArrayInputStream))
		{
			plugin.getLogger().warning("read size: " + byteArrayInputStream.available());
			towns = plugin.getTownSerializer().readTowns(in);
			plugin.getServer().getLogger().warning("Towns loaded: " + towns.size());
		}
	}
	
	public void loadTownsOrElse(Runnable runnable)
	{
		try
		{
			loadTowns();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			runnable.run();
		}
	}
	
	public void saveTowns()
	{
		try(FileOutputStream fileOutputStream = new FileOutputStream(plugin.getDataFileManager().createTownsFileIfNotExists());
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
		{
			plugin.getTownSerializer().writeTowns(out);
			out.flush();
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
			plugin.getLogger().info("Towns saved!");
			plugin.getLogger().warning("write size: " + byteArrayOutputStream.toByteArray().length);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Town createNewTown(UUID uuid) throws CannotConstructException, IOException
	{
		Location location = findNextEmptyTown();
		BlockVector3 to = BlockVector3.at(location.getX(), location.getY() + 1, location.getZ());
		
		try
		{
			File file = new File("plugins/FastAsyncWorldEdit/schematics", "basis.schem");
			
			var world = FaweAPI.getWorld(plugin.getTownWorld().getName());
			
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
				Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(to)
						.ignoreAirBlocks(false).build();
				Operations.complete(operation);
			}
		}
		catch(WorldEditException e)
		{
			throw new RuntimeException(e);
		}
		
		return new CraftTown(plugin.getServer().getOfflinePlayer(uuid), location, plugin);
	}
	
	public Location findNextEmptyTown()
	{
		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;
		
		for(; ; )
		{
			Block block = plugin.getTownWorld()
					.getBlockAt(x * Town.TOWN_FULL_DIAMATER, Town.Y, z * Town.TOWN_FULL_DIAMATER);
			
			if(block.getType().isAir())
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
		
		towns.values().forEach(Town::reloadAttributes);
	}
}
