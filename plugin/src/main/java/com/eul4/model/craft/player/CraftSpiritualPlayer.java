package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public non-sealed abstract class CraftSpiritualPlayer extends CraftPluginPlayer implements SpiritualPlayer
{
	private PhysicalPlayerType reincarnationType;
	
	protected CraftSpiritualPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	protected CraftSpiritualPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.reincarnationType = pluginPlayer.getReincarnationType();
	}
}
