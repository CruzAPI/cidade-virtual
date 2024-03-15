package com.eul4.service;

import com.eul4.Main;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;

@RequiredArgsConstructor
public class TownManager
{
	private final Main plugin;
	
	public void createNewTown(Player player)
	{
		Location location = findNextEmptyTown();
		
		for(int x = -Town.TOWN_FULL_RADIUS; x <= Town.TOWN_FULL_RADIUS; x++)
		{
			for(int z = -Town.TOWN_FULL_RADIUS; z <= Town.TOWN_FULL_RADIUS; z++)
			{
				Material type = Math.abs(x) <= Town.TOWN_RADIUS && Math.abs(z) <= Town.TOWN_RADIUS
						? Material.WHITE_WOOL
						: Material.BLACK_WOOL;
				
				location.getBlock().getRelative(x, 0, z).setType(type);
			}
		}
		
		player.teleport(location.add(0.0D, 1.0D, 0.0D));
	}
	
	public Location findNextEmptyTown()
	{
		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;
		
		for(;;)
		{
			Block block = plugin.getTownWorld().getBlockAt(x * Town.TOWN_FULL_DIAMATER, 50, z * Town.TOWN_FULL_DIAMATER);
			
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
}
