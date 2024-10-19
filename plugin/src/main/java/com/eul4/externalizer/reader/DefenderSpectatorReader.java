package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftDefenderSpectator;
import com.eul4.model.player.spiritual.DefenderSpectator;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class DefenderSpectatorReader extends SpiritualPlayerReader<DefenderSpectator>
{
	private final Reader<DefenderSpectator> reader;
	private final BiParameterizedReadable<DefenderSpectator, Player, Main> biParameterizedReadable;
	
	public DefenderSpectatorReader(Readers readers) throws InvalidVersionException
	{
		super(readers, DefenderSpectator.class);
		
		final ObjectType objectType = PluginObjectType.DEFENDER_SPECTATOR;
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
	
	private void readerVersion0(DefenderSpectator defenderSpectator) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(defenderSpectator);
	}
	
	private Readable<DefenderSpectator> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftDefenderSpectator(player, plugin);
	}
	
	@Override
	public DefenderSpectator readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
