package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class TownPlayerReader<TP extends TownPlayer> extends PhysicalPlayerReader<TP>
{
	private final Reader<TP> reader;
	private final BiParameterizedReadable<TP, Player, Main> biParameterizedReadable;
	
	public TownPlayerReader(Readers readers) throws InvalidVersionException
	{
		this(readers, (Class<TP>) TownPlayer.class);
	}
	
	public TownPlayerReader(Readers readers, Class<TP> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = PluginObjectType.TOWN_PLAYER;
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
	
	private Readable<TP> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> type.cast(new CraftTownPlayer(player, plugin));
	}
	
	private void readerVersion0(TP townPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(townPlayer);
	}
	
	@Override
	public TP readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
