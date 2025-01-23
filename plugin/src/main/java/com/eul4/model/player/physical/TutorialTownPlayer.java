package com.eul4.model.player.physical;

import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.player.tutorial.step.Step;

public interface TutorialTownPlayer extends TownPlayer
{
	void scheduleStep(Step step);
	Step getCurrentStep();
	PluginPlayer finishTutorial();
}
