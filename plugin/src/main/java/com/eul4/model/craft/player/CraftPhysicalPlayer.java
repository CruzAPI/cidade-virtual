package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.PhysicalPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.PluginPlayerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public non-sealed abstract class CraftPhysicalPlayer extends CraftPluginPlayer implements PhysicalPlayer
{
	protected CraftPhysicalPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	protected CraftPhysicalPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public final PhysicalPlayerType getReincarnationType()
	{
		return getPlayerType();
	}
}
