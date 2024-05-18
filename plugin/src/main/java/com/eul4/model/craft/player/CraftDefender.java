package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Defender;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.SpiritualPlayerType;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class CraftDefender extends CraftSpiritualPlayer implements Defender
{
	public CraftDefender(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftDefender(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return false;
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.DEFENDER;
	}
}
