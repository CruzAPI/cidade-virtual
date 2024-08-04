package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftTutorialTownPlayer;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class TutorialTownPlayerReader extends TownPlayerReader<TutorialTownPlayer>
{
	private final Reader<TutorialTownPlayer> reader;
	private final BiParameterizedReadable<TutorialTownPlayer, Player, Main> biParameterizedReadable;
	
	public TutorialTownPlayerReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TutorialTownPlayer.class);
		
		final ObjectType objectType = PluginObjectType.TUTORIAL_TOWN_PLAYER;
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
	
	private Readable<TutorialTownPlayer> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftTutorialTownPlayer(player, plugin);
	}
	
	private void readerVersion0(TutorialTownPlayer tutorialTownPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(tutorialTownPlayer);
	}
	
	@Override
	public TutorialTownPlayer readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
