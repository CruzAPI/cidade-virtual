package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step13;
import com.eul4.model.player.tutorial.step.Step20;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep20 extends CraftStep implements Step20
{
	public CraftStep20(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_20.withArgs(),
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
		tutorialTownPlayer.scheduleStep(new CraftStep21(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_15;
	}
}
