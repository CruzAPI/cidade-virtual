package com.eul4.model.craft.player.tutorial.step;

import com.eul4.enums.StructureStatus;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step13;
import com.eul4.model.player.tutorial.step.Step23;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep23 extends CraftStep implements Step23
{
	public CraftStep23(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_23.withArgs(),
				BOSS_BAR_WAIT_BUILD_FINISH.withArgs());
	}
	
	@Override
	protected void onSchedule()
	{
		super.onSchedule();
		
		if(tutorialTownPlayer.getTown().getTownHall().getStatus() != StructureStatus.UNREADY)
		{
			completeStep();
		}
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
		tutorialTownPlayer.scheduleStep(new CraftStep24(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_23;
	}
}
