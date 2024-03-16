package com.eul4.service;

import com.eul4.Main;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.command.SchematicCommands;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class TownManager
{
	private final Main plugin;
	
	private final Map<UUID, Town> towns = new HashMap<>();
	
	public Town getOrCreateNewTown(UUID uuid)
	{
		return towns.computeIfAbsent(uuid, this::createNewTown);
	}
	
	public Town createNewTown(UUID uuid)
	{
		Location location = findNextEmptyTown();
		BlockVector3 to = BlockVector3.at(location.getX(), location.getY() + 1, location.getZ());

		try {
			File file = new File("plugins/FastAsyncWorldEdit/schematics", "basis.schem");

			var world = FaweAPI.getWorld(plugin.getTownWorld().getName());

			ClipboardFormat format = ClipboardFormats.findByFile(file);
			ClipboardReader reader = null;
			try {
				reader = format.getReader(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Clipboard clipboard = null;
			try {
				clipboard = reader.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
				Operation operation = new ClipboardHolder(clipboard)
						.createPaste(editSession)
						.to(to)
						.ignoreAirBlocks(false)
						.build();
				Operations.complete(operation);
			}
		} catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
		
		return new CraftTown(plugin.getServer().getOfflinePlayer(uuid), location);
	}
	
	public Location findNextEmptyTown()
	{
		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;
		
		for(;;)
		{
			Block block = plugin.getTownWorld().getBlockAt(x * Town.TOWN_FULL_DIAMATER, Town.Y, z * Town.TOWN_FULL_DIAMATER);
			
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
}
