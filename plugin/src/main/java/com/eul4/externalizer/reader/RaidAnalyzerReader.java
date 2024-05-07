package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.player.RaidAnalyzer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class RaidAnalyzerReader extends PluginPlayerReader<RaidAnalyzer>
{
	private final Reader<RaidAnalyzer> reader;
	
	public RaidAnalyzerReader(ObjectInput in, Versions versions, Player player, Main plugin) throws InvalidVersionException
	{
		super(in, versions, plugin, () -> new CraftRaidAnalyzer(player, plugin));
		
		if(versions.getRaidAnalyzerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid RaidAnalyzer version: " + versions.getRaidAnalyzerVersion());
		}
	}
	
	private RaidAnalyzer readerVersion0() throws IOException, ClassNotFoundException
	{
		RaidAnalyzer raidAnalyzer = getInstance();
		//TODO: ...?
		return raidAnalyzer;
	}
	
	@Override
	protected RaidAnalyzer readObject() throws IOException, ClassNotFoundException
	{
		super.readObject();
		
		return reader.readObject();
	}
}
