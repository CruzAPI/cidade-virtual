package com.eul4.model.craft.player.tutorial.step;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CollectDislikesStep;

public abstract class CraftCollectDislikesStep extends CraftCollectGeneratorStep implements CollectDislikesStep
{
	public CraftCollectDislikesStep(
			TutorialTownPlayer tutorialTownPlayer,
			MessageArgs messageArgs,
			MessageArgs bossBarMessageArgs)
	{
		super(tutorialTownPlayer, messageArgs, bossBarMessageArgs);
	}
	
	public CraftCollectDislikesStep(
			TutorialTownPlayer tutorialTownPlayer,
			MessageArgs messageArgs,
			MessageArgs bossBarMessageArgs,
			long durationTicks)
	{
		super(tutorialTownPlayer, messageArgs, bossBarMessageArgs, durationTicks);
	}
}
