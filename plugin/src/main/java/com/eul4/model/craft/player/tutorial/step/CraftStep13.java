package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step13;

import static com.eul4.i18n.TutorialTownMessage.BOSS_BAR_TALK_WITH_ASSISTANT;
import static com.eul4.i18n.TutorialTownMessage.STEP_13;

public class CraftStep13 extends CraftStep implements Step13
{
	public CraftStep13(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_13.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				10L * 20L);
	}
	
	@Override
	public void onRun()
	{
		assistantTargetPlayer();
	}
	
	@Override
	public void completeStep()
	{
		cancel();
		tutorialTownPlayer.scheduleStep(new CraftStep14(tutorialTownPlayer));
	}
}
