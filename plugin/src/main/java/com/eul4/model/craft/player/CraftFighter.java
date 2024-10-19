package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Fighter;
import com.eul4.model.player.PluginPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class CraftFighter extends CraftSpiritualPlayer implements Fighter
{
	@Setter
	@Getter
	private Location lastValidLocation;
	
	public CraftFighter(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftFighter(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
}
