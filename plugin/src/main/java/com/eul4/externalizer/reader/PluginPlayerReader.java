package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.CommonPlayerReader;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.function.Supplier;

public abstract class PluginPlayerReader<P extends PluginPlayer> extends CommonPlayerReader<P>
{
	private final TownPlayerDataReader townPlayerDataReader;
	
	private final Reader<P> reader;
	
	public PluginPlayerReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.townPlayerDataReader = new TownPlayerDataReader(in, versions);
		
		if(versions.getPluginPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid PluginPlayer version: " + versions.getPluginPlayerVersion());
		}
	}
	
	private P readerVersion0(P pluginPlayer) throws IOException, ClassNotFoundException
	{
		TownPlayerData townPlayerData = townPlayerDataReader.readReference();
		
		pluginPlayer.setTownPlayerData(townPlayerData);
		
		return pluginPlayer;
	}
	
	@Override
	protected P readObject(P pluginPlayer) throws IOException, ClassNotFoundException
	{
		super.readObject(pluginPlayer);
		
		return reader.readObject(pluginPlayer);
	}
}
