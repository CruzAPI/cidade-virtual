package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.VanillaPlayer;
import com.eul4.scoreboard.CraftInitialScoreboard;
import com.eul4.scoreboard.CraftTownScoreboard;
import com.eul4.scoreboard.TownScoreboard;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.world.VanillaWorld;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Getter
public class CraftVanillaPlayer extends CraftPhysicalPlayer implements VanillaPlayer
{
	private final CommonScoreboard scoreboard = hasTown()
			? new CraftTownScoreboard(this)
			: new CraftInitialScoreboard(this);
	
	public CraftVanillaPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftVanillaPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return true;
	}
	
	@Override
	public PhysicalPlayerType getPlayerType()
	{
		return PhysicalPlayerType.VANILLA_PLAYER;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		
		if(!(getCommonWorld() instanceof VanillaWorld))
		{
			player.teleport(PluginWorldType.OVER_WORLD.getInstance().getSpawnLocation());
		}
	}
	
	@Override
	public PluginPlayer load()
	{
		return super.load();
	}

	@Override
	public PluginPlayer reload()
	{
		return load(); //TODO is it?
	}
}
