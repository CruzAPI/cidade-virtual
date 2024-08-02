package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step24;

import static com.eul4.i18n.TutorialTownMessage.BOSS_BAR_CLICK_TO_FINISH_BUILD;
import static com.eul4.i18n.TutorialTownMessage.STEP_24;

public class CraftStep24 extends CraftStep implements Step24
{
	public CraftStep24(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer, STEP_24.withArgs(), BOSS_BAR_CLICK_TO_FINISH_BUILD.withArgs());
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
		tutorialTownPlayer.scheduleStep(new CraftStep25(tutorialTownPlayer));
	}
}
