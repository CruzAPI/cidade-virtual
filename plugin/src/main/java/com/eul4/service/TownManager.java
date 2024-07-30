package com.eul4.service;

import com.eul4.Main;
import com.eul4.common.util.BoundingBoxUtil;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.TownAlreadyExistsException;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.town.Town;
import com.eul4.type.PluginWorldType;
import com.eul4.util.FaweUtil;
import com.eul4.wrapper.TownMap;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
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
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class TownManager
{
	private final Main plugin;
	
	@Getter
	private TownMap towns;
	
	private final Set<UUID> waitingCreation = new HashSet<>();
	
	public void loadTowns() throws Exception
	{
		if(towns != null)
		{
			throw new Exception("Towns already loaded.");
		}
		
		this.towns = plugin.getTownsFiler().loadTownsFromDisk();
		this.towns.values().forEach(Town::load);
	}
	
	public Future<Town> createNewTownAsync(UUID uuid)
	{
		waitingCreation.add(uuid);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		return executorService.submit(() -> createNewTown(uuid));
	}
	
	private synchronized Town createNewTown(UUID uuid) throws CannotConstructException, IOException
	{
		if(towns.containsKey(uuid))
		{
			throw new TownAlreadyExistsException();
		}
		
		Location location = findNextEmptyTown();
		BlockVector3 to = BlockVector3.at(location.getX(), location.getY() + 1, location.getZ());
		
		try
		{
			File file = new File("plugins/FastAsyncWorldEdit/schematics", "basis.schem");
			
			var weWorld = FaweAPI.getWorld(PluginWorldType.TOWN_WORLD.getInstance().getWorld().getName());
			
			ClipboardFormat format = ClipboardFormats.findByFile(file);
			
			try(ClipboardReader reader = format.getReader(new FileInputStream(file));
					Clipboard clipboard = reader.read();
					EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1))
			{
				final int minX = clipboard.getMinimumPoint().x();
				final int minY = clipboard.getMinimumPoint().y();
				final int minZ = clipboard.getMinimumPoint().z();
				final int maxX = clipboard.getMaximumPoint().x();
				final int maxY = clipboard.getMaximumPoint().y();
				final int maxZ = clipboard.getMaximumPoint().z();
				
				final int originX = clipboard.getOrigin().x();
				final int originY = clipboard.getOrigin().y();
				final int originZ = clipboard.getOrigin().z();
				
				final int relativeMinX = minX - originX;
				final int relativeMinY = minY - originY;
				final int relativeMinZ = minZ - originZ;
				final int relativeMaxX = maxX - originX;
				final int relativeMaxY = maxY - originY;
				final int relativeMaxZ = maxZ - originZ;
				
				final int absolutMinX = relativeMinX + to.x();
				final int absolutMinY = relativeMinY + to.y();
				final int absolutMinZ = relativeMinZ + to.z();
				final int absolutMaxX = relativeMaxX + to.x();
				final int absolutMaxY = relativeMaxY + to.y();
				final int absolutMaxZ = relativeMaxZ + to.z();
				
				BlockVector3 min = BlockVector3.at(absolutMinX, absolutMinY, absolutMinZ);
				BlockVector3 max = BlockVector3.at(absolutMaxX, absolutMaxY, absolutMaxZ);
				
				BoundingBox boundingBox = FaweUtil.boundingBox(min, max, weWorld);
				
				plugin.getServer()
						.getScheduler()
						.getMainThreadExecutor(plugin)
						.execute(() -> BoundingBoxUtil.removeEntities(boundingBox, location.getWorld()));
				
				Operation operation = new ClipboardHolder(clipboard)
						.createPaste(editSession)
						.to(to)
						.ignoreAirBlocks(false)
						.build();
				
				Operations.complete(operation);
			}
			
			Town town = new CraftTown(plugin.getServer().getOfflinePlayer(uuid), location.getBlock(), plugin);
			
			towns.put(uuid, town);
			return town;
		}
		finally
		{
			waitingCreation.remove(uuid);
		}
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
	
	public boolean isCreating(UUID uuid)
	{
		return waitingCreation.contains(uuid);
	}
	
	public Optional<Town> findTown(UUID townUUID)
	{
		return Optional.ofNullable(getTown(townUUID));
	}
}
