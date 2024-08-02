package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step23;
import com.eul4.model.player.tutorial.step.Step25;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep25 extends CraftStep implements Step25
{
	public CraftStep25(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_25.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				60L * 20L);
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
		tutorialTownPlayer.finishTutorial();
	}
}
