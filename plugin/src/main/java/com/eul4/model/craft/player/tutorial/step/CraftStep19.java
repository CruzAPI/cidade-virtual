package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step13;
import com.eul4.model.player.tutorial.step.Step19;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep19 extends CraftStep implements Step19
{
	public CraftStep19(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_19.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				25L * 20L);
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
		tutorialTownPlayer.scheduleStep(new CraftStep20(tutorialTownPlayer));
	}
}
