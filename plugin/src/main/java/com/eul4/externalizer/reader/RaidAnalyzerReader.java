package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.player.RaidAnalyzer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class RaidAnalyzerReader extends PluginPlayerReader<RaidAnalyzer>
{
	private final Reader<RaidAnalyzer> reader;
	private final BiParameterizedReadable<RaidAnalyzer, Player, Main> biParameterizedReadable;
	
	public RaidAnalyzerReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getRaidAnalyzerVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid RaidAnalyzer version: " + versions.getRaidAnalyzerVersion());
		}
	}
	
	private Readable<RaidAnalyzer> biParameterizedReadableVersion0(Player player, Main plugin) throws IOException, ClassNotFoundException
	{
		return () -> new CraftRaidAnalyzer(player, plugin);
	}
	
	private RaidAnalyzer readerVersion0(RaidAnalyzer raidAnalyzer) throws IOException, ClassNotFoundException
	{
		//TODO: read RaidAnalyzer fields...
		return raidAnalyzer;
	}
	
	@Override
	public RaidAnalyzer readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
	
	@Override
	protected RaidAnalyzer readObject(RaidAnalyzer raidAnalyzer) throws IOException, ClassNotFoundException
	{
		super.readObject(raidAnalyzer);
		
		return reader.readObject(raidAnalyzer);
	}
}
