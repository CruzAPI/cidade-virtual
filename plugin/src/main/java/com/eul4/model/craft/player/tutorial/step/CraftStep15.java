package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step15;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep15 extends CraftStep implements Step15
{
	public CraftStep15(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_15.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				20L * 20L);
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
		tutorialTownPlayer.scheduleStep(new CraftStep16(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_15;
	}
}
