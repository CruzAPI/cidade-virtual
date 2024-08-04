package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.playerdata.TutorialTownPlayerData;

import java.io.IOException;

public class TutorialTownPlayerDataWriter extends ObjectWriter<TutorialTownPlayerData>
{
	public TutorialTownPlayerDataWriter(Writers writers)
	{
		super(writers, TutorialTownPlayerData.class);
	}
	
	@Override
	protected void writeObject(TutorialTownPlayerData tutorialTownPlayerData) throws IOException
	{
		writers.getWriter(CheckpointStepEnumWriter.class).writeReference(tutorialTownPlayerData.getCheckpointStep());
	}
}
