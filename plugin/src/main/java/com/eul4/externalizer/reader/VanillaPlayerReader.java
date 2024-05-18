package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftVanillaPlayer;
import com.eul4.model.player.VanillaPlayer;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class VanillaPlayerReader extends PhysicalPlayerReader<VanillaPlayer>
{
	private final Reader<VanillaPlayer> reader;
	private final BiParameterizedReadable<VanillaPlayer, Player, Main> biParameterizedReadable;
	
	public VanillaPlayerReader(Readers readers) throws InvalidVersionException
	{
		super(readers, VanillaPlayer.class);
		
		final ObjectType objectType = PluginObjectType.VANILLA_PLAYER;
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
	
	private void readerVersion0(VanillaPlayer vanillaPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(vanillaPlayer);
	}
	
	private Readable<VanillaPlayer> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftVanillaPlayer(player, plugin);
	}
	
	@Override
	public VanillaPlayer readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
