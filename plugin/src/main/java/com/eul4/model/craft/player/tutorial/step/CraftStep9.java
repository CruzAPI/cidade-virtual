package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step9;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep9 extends CraftStep implements Step9
{
	public CraftStep9(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_9.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				10L * 20L);
	}
	
	@Override
	protected void onSchedule()
	{
		super.onSchedule();
		
		targetLikeGenerator(tutorialTownPlayer.getPlayer());
		targetLikeGenerator(getAssistant());
	}
	
	@Override
	public void completeStep()
	{
		cancel();
		tutorialTownPlayer.scheduleStep(new CraftStep10(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_1;
	}
}
