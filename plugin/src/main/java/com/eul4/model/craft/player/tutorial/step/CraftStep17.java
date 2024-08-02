package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step13;
import com.eul4.model.player.tutorial.step.Step17;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep17 extends CraftStep implements Step17
{
	public CraftStep17(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_17.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				50L * 20L);
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
		tutorialTownPlayer.scheduleStep(new CraftStep18(tutorialTownPlayer));
	}
}
