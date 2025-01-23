package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftInventoryOrganizerPlayer;
import com.eul4.model.player.spiritual.InventoryOrganizerPlayer;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class InventoryOrganizerPlayerReader extends SpiritualPlayerReader<InventoryOrganizerPlayer>
{
	private final Reader<InventoryOrganizerPlayer> reader;
	private final BiParameterizedReadable<InventoryOrganizerPlayer, Player, Main> biParameterizedReadable;
	
	public InventoryOrganizerPlayerReader(Readers readers) throws InvalidVersionException
	{
		super(readers, InventoryOrganizerPlayer.class);
		
		final ObjectType objectType = PluginObjectType.INVENTORY_ORGANIZER_PLAYER;
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
	
	private void readerVersion0(InventoryOrganizerPlayer inventoryOrganizerPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(inventoryOrganizerPlayer);
	}
	
	private Readable<InventoryOrganizerPlayer> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftInventoryOrganizerPlayer(player, plugin);
	}
	
	@Override
	public InventoryOrganizerPlayer readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
