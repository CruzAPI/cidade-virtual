package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.type.player.CommonWorldType;
import com.eul4.common.world.CommonWorld;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class WorldManager
{
	private final Common plugin;
	
	private final Map<UUID, CommonWorld> commonWorlds = new HashMap<>();
	
	public void register(CommonWorldType worldType)
	{
		CommonWorld commonWorld = worldType.getInstance();
		commonWorlds.put(commonWorld.getWorld().getUID(), commonWorld);
	}
	
	public CommonWorld get(World world)
	{
		return get(world.getUID());
	}
	
	public CommonWorld get(UUID uuid)
	{
		return commonWorlds.get(uuid);
	}
}
