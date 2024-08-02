package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step6;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep6 extends CraftStep implements Step6
{
	public CraftStep6(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_6.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				10L * 20L);
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
		tutorialTownPlayer.scheduleStep(new CraftStep7(tutorialTownPlayer));
	}
}
