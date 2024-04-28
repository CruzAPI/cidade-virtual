package com.eul4.externalizer;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@RequiredArgsConstructor
public class PluginPlayerExternalizer
{
	private final Main plugin;
	
	private static final long VERSION = 0L;
	
	public PluginPlayer read(Player player, ObjectInput in) throws IOException, ClassNotFoundException
	{
		return read(in.readInt(), player, in);
	}
	
	public PluginPlayer read(long version, Player player, ObjectInput in) throws IOException, ClassNotFoundException
	{
		if(version == 0L)
		{
			PluginPlayerType.Type type = PluginPlayerType.Type.values()[in.readInt()];
			PluginPlayer pluginPlayer = type.getPluginPlayerType().getNewInstanceBiFunction().apply(player, plugin);
			
			pluginPlayer.readExternal(in);
			
			return pluginPlayer;
		}
		
		throw new RuntimeException();
	}
	
	public void write(PluginPlayer pluginPlayer, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		out.writeInt(pluginPlayer.getLastLoadableCommonPlayerType().ordinal());
		pluginPlayer.writeExternal(out);
	}
}
