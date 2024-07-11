package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Attacker;
import com.eul4.model.player.Fighter;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.wrapper.TownAttack;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.function.Predicate;
import java.util.logging.Level;

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
