package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.externalizer.reader.PluginPlayerReader;
import com.eul4.externalizer.writer.PluginPlayerWriter;
import com.eul4.model.player.PluginPlayer;
import org.bukkit.entity.Player;

public interface PluginPlayerType extends PlayerType
{
	Class<? extends PluginPlayer> getInterfaceType();
	Class<? extends PluginPlayerWriter> getWriterClass();
	Class<? extends PluginPlayerReader> getReaderClass();
	PluginPlayer newInstance(Player player, PluginPlayer pluginPlayer);
	PluginPlayer newInstance(Player player, Main plugin);
	PlayerCategory getCategory();
	
	@Override
	default PluginPlayer newInstance(Player player, Common plugin)
	{
		return newInstance(player, (Main) plugin);
	}
	
	@Override
	default PluginPlayer newInstance(Player player, CommonPlayer commonPlayer)
	{
		return newInstance(player, (PluginPlayer) commonPlayer);
	}
}
