package com.eul4.model.player;

import com.eul4.model.player.tutorial.step.Step;

public interface TutorialTownPlayer extends
		TownPlayer
{
	void scheduleStep(Step step);
	Step getCurrentStep();
	PluginPlayer finishTutorial();
}
