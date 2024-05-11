package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.type.player.PluginObjectType;
import org.bukkit.entity.Player;

import java.io.IOException;

public class RaidAnalyzerReader extends PluginPlayerReader<RaidAnalyzer>
{
	private final Reader<RaidAnalyzer> reader;
	private final BiParameterizedReadable<RaidAnalyzer, Player, Main> biParameterizedReadable;
	
	public RaidAnalyzerReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.RAID_ANALYZER;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
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
