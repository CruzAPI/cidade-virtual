package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.TutorialTownPlayer;

import java.io.IOException;

public class TutorialTownPlayerWriter extends TownPlayerWriter<TutorialTownPlayer>
{
	public TutorialTownPlayerWriter(Writers writers)
	{
		super(writers, TutorialTownPlayer.class);
	}
	
	@Override
	protected void writeObject(TutorialTownPlayer tutorialTownPlayer) throws IOException
	{
		super.writeObject(tutorialTownPlayer);
	}
}
