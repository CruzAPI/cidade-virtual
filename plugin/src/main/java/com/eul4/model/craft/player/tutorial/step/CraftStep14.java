package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step14;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep14 extends CraftCollectDislikesStep implements Step14
{
	public CraftStep14(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_14.withArgs(),
				BOSS_BAR_COLLECT_DISLIKES.withArgs());
	}
	
	@Override
	protected void onSchedule()
	{
		super.onSchedule();
		
		targetDislikeGenerator(getAssistant());
		targetDislikeGenerator(tutorialTownPlayer.getPlayer());
	}
	
	@Override
	public void completeStep()
	{
		cancel();
		tutorialTownPlayer.scheduleStep(new CraftStep15(tutorialTownPlayer));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_14;
	}
}
