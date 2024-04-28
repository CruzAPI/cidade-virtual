package com.eul4.common.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Builder
@AllArgsConstructor
public class PlayerData
{
	private Location location;
	private ItemStack[] contents;
	
	public PlayerData(Player player)
	{
		location = player.getLocation();
		contents = player.getInventory().getContents();
	}
	
	public void apply(Player player)
	{
		player.teleport(location);
		player.getInventory().setContents(contents);
	}
}
