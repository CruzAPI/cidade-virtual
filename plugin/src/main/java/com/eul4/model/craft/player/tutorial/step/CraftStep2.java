package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step2;

import static com.eul4.i18n.TutorialTownMessage.BOSS_BAR_TALK_WITH_ASSISTANT;
import static com.eul4.i18n.TutorialTownMessage.STEP_2;

public class CraftStep2 extends CraftStep implements Step2
{
	public CraftStep2(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_2.withArgs(tutorialTownPlayer.getPlayer().displayName()),
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
		tutorialTownPlayer.scheduleStep(new CraftStep3(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_1;
	}
}
