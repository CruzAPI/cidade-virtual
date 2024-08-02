package com.eul4.world.craft;

import com.eul4.common.type.player.PlayerType;
import com.eul4.type.PluginWorldType;
import com.eul4.world.TownWorld;
import org.bukkit.World;

import java.util.Collection;
import java.util.List;

import static com.eul4.type.player.PhysicalPlayerType.*;

public class CraftTownWorld extends CraftPluginWorld implements TownWorld
{
	private final List<PlayerType> acceptablePlayerTypes = List.of(TOWN_PLAYER, ADMIN, TUTORIAL_TOWN_PLAYER);
	
	public CraftTownWorld(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.TOWN_WORLD;
	}
	
	@Override
	public Collection<PlayerType> getAcceptablePlayerTypes()
	{
		return acceptablePlayerTypes;
	}
	
	@Override
	public PlayerType getDefaultPlayerType()
	{
		return TOWN_PLAYER;
	}
}
