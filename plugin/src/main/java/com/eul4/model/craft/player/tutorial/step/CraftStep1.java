package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step1;

import static com.eul4.i18n.TutorialTownMessage.BOSS_BAR_GO_TALK_WITH_ASSISTANT;
import static com.eul4.i18n.TutorialTownMessage.STEP_1;

public class CraftStep1 extends CraftStep implements Step1
{
	public CraftStep1(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_1.withArgs(tutorialTownPlayer.getPlayer().displayName()),
				BOSS_BAR_GO_TALK_WITH_ASSISTANT.withArgs());
	}
	
	@Override
	protected void onRun()
	{
		assistantTargetPlayer();
	}
	
	@Override
	public void completeStep()
	{
		cancel();
		tutorialTownPlayer.scheduleStep(new CraftStep2(tutorialTownPlayer));
	}
}
