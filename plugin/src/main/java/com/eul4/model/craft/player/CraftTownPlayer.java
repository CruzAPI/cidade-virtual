package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.type.player.CommonPlayerTypeEnum;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.type.player.PluginCommonPlayerType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CraftTownPlayer extends CraftPluginPlayer implements TownPlayer
{
	private Structure movingStructure;
	
	public CraftTownPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftTownPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		player.setGameMode(GameMode.SURVIVAL);
	}
	
	@Override
	public Town getTown()
	{
		return plugin.getTownManager().getTown(player.getUniqueId());
	}
	
	@Override
	public boolean hasTown()
	{
		return getTown() != null;
	}
	
	@Override
	public Structure getMovingStructure()
	{
		return movingStructure;
	}
	
	@Override
	public void setMovingStructure(Structure structure)
	{
		this.movingStructure = structure;
	}
	
	@Override
	public void analyzeTown(Town town)
	{
		plugin.getPlayerManager();
	}
	
	@Override
	public boolean test()
	{
		return getTownPlayerData().isTest();
	}
	
	@Override
	public void test(boolean test)
	{
		getTownPlayerData().setTest(test);
	}
	
	@Override
	public PluginCommonPlayerType.Type getCommonPlayerTypeEnum()
	{
		return PluginCommonPlayerType.Type.TOWN_PLAYER;
	}
}
