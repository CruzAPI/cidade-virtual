package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.playerdata.TutorialTownPlayerData;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class TutorialTownPlayerDataReader extends ObjectReader<TutorialTownPlayerData>
{
	private final Reader<TutorialTownPlayerData> reader;
	private final Readable<TutorialTownPlayerData> readable;
	
	public TutorialTownPlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TutorialTownPlayerData.class);
		
		final ObjectType objectType = PluginObjectType.TUTORIAL_TOWN_PLAYER_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private TutorialTownPlayerData readableVersion0() throws IOException, ClassNotFoundException
	{
		CheckpointStepEnum checkpointStep = readers.getReader(CheckpointStepEnumReader.class).readReference();
		
		return TutorialTownPlayerData.builder()
				.checkpointStep(checkpointStep)
				.build();
	}
	
	public TutorialTownPlayerData readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
