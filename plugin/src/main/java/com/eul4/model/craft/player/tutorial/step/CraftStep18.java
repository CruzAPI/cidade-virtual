package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step18;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep18 extends CraftStep implements Step18
{
	public CraftStep18(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_18.withArgs(),
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
		tutorialTownPlayer.scheduleStep(new CraftStep19(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_15;
	}
}
