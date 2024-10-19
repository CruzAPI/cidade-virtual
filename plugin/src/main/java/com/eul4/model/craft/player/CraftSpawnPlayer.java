package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.SpawnPlayer;
import com.eul4.scoreboard.CraftInitialScoreboard;
import com.eul4.scoreboard.CraftTownScoreboard;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.world.SpawnProtectedLevel;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Getter
public class CraftSpawnPlayer extends CraftPhysicalPlayer implements SpawnPlayer
{
	private final CommonScoreboard scoreboard = hasTown()
			? new CraftTownScoreboard(this)
			: new CraftInitialScoreboard(this);
	
	public CraftSpawnPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftSpawnPlayer(Player player, PluginPlayer pluginPlayer)
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
		return PhysicalPlayerType.SPAWN_PLAYER;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		sendMessage(PluginMessage.SPAWN_PROTECTION_ENABLED);
		sendMessage(PluginMessage.SPAWN_PROTECTION_WARN);
		
		if(!(getCommonWorld() instanceof SpawnProtectedLevel))
		{
			player.teleport(PluginWorldType.NEWBIE_WORLD.getInstance().getSpawnLocation());
		}
		
		scoreboard.registerIfNotRegistered();
	}
	
	@Override
	public PluginPlayer load()
	{
		return super.load();
	}

	@Override
	public PluginPlayer reload()
	{
		return load();
	}
	
	@Override
	public PluginPlayer removeProtection()
	{
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(this, PhysicalPlayerType.VANILLA_PLAYER);
		pluginPlayer.sendMessage(PluginMessage.SPAWN_PROTECTION_DISABLED);
		return pluginPlayer;
	}
}
