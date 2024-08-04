package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step3;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep3 extends CraftStep implements Step3
{
	public CraftStep3(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_3.withArgs(tutorialTownPlayer.getPlayer().displayName()),
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
		tutorialTownPlayer.scheduleStep(new CraftStep4(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_1;
	}
}
