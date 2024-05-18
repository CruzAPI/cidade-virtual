package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.VanillaPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class CraftVanillaPlayer extends CraftPhysicalPlayer implements VanillaPlayer
{
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
}
