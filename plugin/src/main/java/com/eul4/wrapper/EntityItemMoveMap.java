package com.eul4.wrapper;

import com.eul4.common.util.ContainerUtil;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class EntityItemMoveMap extends HashMap<UUID, EntityItemMove>
{
	public EntityItemMove get(ItemStack item)
	{
		return ContainerUtil.findUUID(item).map(this::get).orElse(null);
	}
	
	public EntityItemMove remove(ItemStack item)
	{
		return ContainerUtil.findUUID(item).map(this::remove).orElse(null);
	}
}
