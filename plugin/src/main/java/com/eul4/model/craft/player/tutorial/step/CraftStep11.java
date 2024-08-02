package com.eul4.model.craft.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step11;

import static com.eul4.i18n.TutorialTownMessage.*;

public class CraftStep11 extends CraftCollectLikesStep implements Step11
{
	public CraftStep11(TutorialTownPlayer tutorialTownPlayer)
	{
		super(tutorialTownPlayer,
				STEP_11.withArgs(),
				BOSS_BAR_COLLECT_LIKES.withArgs());
	}
	
	@Override
	protected void onSchedule()
	{
		super.onSchedule();
		
		targetLikeGenerator(getAssistant());
		targetLikeGenerator(tutorialTownPlayer.getPlayer());
	}
	
	@Override
	public void completeStep()
	{
		cancel();
		tutorialTownPlayer.scheduleStep(new CraftStep12(tutorialTownPlayer, getCollectedAmount()));
	}
	
	@Override
	public CheckpointStepEnum getCheckpointStep()
	{
		return CheckpointStepEnum.STEP_1;
	}
}
