package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step12;

import static com.eul4.i18n.TutorialTownMessage.BOSS_BAR_TALK_WITH_ASSISTANT;
import static com.eul4.i18n.TutorialTownMessage.STEP_12;

public class CraftStep12 extends CraftStep implements Step12
{
	public CraftStep12(TutorialTownPlayer tutorialTownPlayer, int collectedAmount)
	{
		super(tutorialTownPlayer,
				STEP_12.withArgs(collectedAmount),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				15L * 20L);
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
		tutorialTownPlayer.scheduleStep(new CraftStep13(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_14;
	}
}
