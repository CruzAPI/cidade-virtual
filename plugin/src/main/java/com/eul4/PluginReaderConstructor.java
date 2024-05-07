package com.eul4;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.externalizer.reader.PluginPlayerReader;
import com.eul4.model.player.PluginPlayer;
import org.bukkit.entity.Player;

import java.io.ObjectInput;

@FunctionalInterface
public interface PluginReaderConstructor
{
	PluginPlayerReader<? extends PluginPlayer> newInstance(ObjectInput in, Versions versions, Player player, Main plugin)
			throws InvalidVersionException;
}
