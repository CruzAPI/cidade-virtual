package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step22;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep22 extends CraftStep implements Step22
{
	public CraftStep22(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_22.withArgs(),
				BOSS_BAR_UPGRADE_TOWN_HALL.withArgs());
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
		tutorialTownPlayer.scheduleStep(new CraftStep23(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_22;
	}
}
