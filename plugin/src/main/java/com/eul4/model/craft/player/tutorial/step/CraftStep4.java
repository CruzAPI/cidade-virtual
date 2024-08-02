package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step4;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep4 extends CraftStep implements Step4
{
	public CraftStep4(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_4.withArgs(),
				BOSS_BAR_TALK_WITH_ASSISTANT.withArgs(),
				30L * 20L);
	}
	
	@Override
	protected void onSchedule()
	{
		super.onSchedule();
		
		targetTownHall(tutorialTownPlayer.getPlayer());
		targetTownHall(getAssistant());
	}
	
	@Override
	public void completeStep()
	{
		cancel();
		tutorialTownPlayer.scheduleStep(new CraftStep5(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_1;
	}
}
