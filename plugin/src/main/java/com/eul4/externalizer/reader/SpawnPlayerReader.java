package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftSpawnPlayer;
import com.eul4.model.player.physical.SpawnPlayer;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class SpawnPlayerReader extends PhysicalPlayerReader<SpawnPlayer>
{
	private final Reader<SpawnPlayer> reader;
	private final BiParameterizedReadable<SpawnPlayer, Player, Main> biParameterizedReadable;
	
	public SpawnPlayerReader(Readers readers) throws InvalidVersionException
	{
		super(readers, SpawnPlayer.class);
		
		final ObjectType objectType = PluginObjectType.SPAWN_PLAYER;
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
	
	private void readerVersion0(SpawnPlayer spawnPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(spawnPlayer);
	}
	
	private Readable<SpawnPlayer> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftSpawnPlayer(player, plugin);
	}
	
	@Override
	public SpawnPlayer readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
