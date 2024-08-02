package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step13;
import com.eul4.model.player.tutorial.step.Step23;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep23 extends CraftStep implements Step23
{
	public CraftStep23(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_23.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs());
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
		tutorialTownPlayer.scheduleStep(new CraftStep24(tutorialTownPlayer));
	}
}
