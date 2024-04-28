package com.eul4.common.model.data;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Builder
public class PlayerData
{
	private Location location;
	private ItemStack[] contents;
	
	public PlayerData(Player player)
	{
		location = player.getLocation();
		contents = player.getInventory().getContents();
	}
}
