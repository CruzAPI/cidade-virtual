package com.eul4.model.craft.player.tutorial.step;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CollectGeneratorStep;
import lombok.Getter;
import lombok.Setter;

public abstract class CraftCollectGeneratorStep extends CraftStep implements CollectGeneratorStep
{
	public CraftCollectGeneratorStep(TutorialTownPlayer tutorialTownPlayer,
			MessageArgs messageArgs,
			MessageArgs bossBarMessageArgs)
	{
		super(tutorialTownPlayer, messageArgs, bossBarMessageArgs);
	}
	
	public CraftCollectGeneratorStep(TutorialTownPlayer tutorialTownPlayer,
			MessageArgs messageArgs,
			MessageArgs bossBarMessageArgs,
			long durationTicks)
	{
		super(tutorialTownPlayer, messageArgs, bossBarMessageArgs, durationTicks);
	}
	
	@Getter
	@Setter
	private int collectedAmount;
}
