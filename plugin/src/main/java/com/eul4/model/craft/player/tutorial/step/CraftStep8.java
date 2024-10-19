package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step8;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep8 extends CraftStep implements Step8
{
	public CraftStep8(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_8.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				30L * 20L);
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
		tutorialTownPlayer.scheduleStep(new CraftStep9(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_1;
	}
}
