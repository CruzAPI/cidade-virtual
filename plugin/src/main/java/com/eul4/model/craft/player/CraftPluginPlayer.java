package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.model.player.craft.CraftCommonPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Getter
public abstract class CraftPluginPlayer extends CraftCommonPlayer implements PluginPlayer
{
	private static final long VERSION = 0L;
	
	protected final Main plugin;
	
	private TownPlayerData townPlayerData;
	
	protected CraftPluginPlayer(Player player, Main plugin)
	{
		super(player, plugin);
		this.plugin = plugin;
		this.townPlayerData = new TownPlayerData();
	}
	
	protected CraftPluginPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.plugin = pluginPlayer.getPlugin();
		this.townPlayerData = pluginPlayer.getTownPlayerData();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		super.writeExternal(out);
		
		out.writeLong(VERSION);
		
		plugin.getTownPlayerDataExternalizer().write(townPlayerData, out);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
		
		long version = in.readLong();
		
		if(version == 0L)
		{
			townPlayerData = plugin.getTownPlayerDataExternalizer().read(in);
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	@Override
	public PluginPlayer load()
	{
		commonPlayerData.getPlayerData().apply(player);
		return this;
	}
}
