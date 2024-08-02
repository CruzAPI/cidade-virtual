package com.eul4.model.craft.player.tutorial.step;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CollectLikesStep;

public abstract class CraftCollectLikesStep extends CraftCollectGeneratorStep implements CollectLikesStep
{
	public CraftCollectLikesStep(
			TutorialTownPlayer tutorialTownPlayer,
			MessageArgs messageArgs,
			MessageArgs bossBarMessageArgs)
	{
		super(tutorialTownPlayer, messageArgs, bossBarMessageArgs);
	}
	
	public CraftCollectLikesStep(
			TutorialTownPlayer tutorialTownPlayer,
			MessageArgs messageArgs,
			MessageArgs bossBarMessageArgs,
			long durationTicks)
	{
		super(tutorialTownPlayer, messageArgs, bossBarMessageArgs, durationTicks);
	}
}
