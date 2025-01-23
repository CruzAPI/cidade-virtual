package com.eul4.model.player.tutorial.step;

import com.eul4.model.craft.player.tutorial.step.*;
import com.eul4.model.player.physical.TutorialTownPlayer;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum CheckpointStepEnum
{
	STEP_1(CraftStep1::new),
	STEP_14(CraftStep14::new),
	STEP_15(CraftStep15::new),
	STEP_22(CraftStep22::new),
	STEP_23(CraftStep23::new),
	STEP_24(CraftStep24::new),
	STEP_25(CraftStep25::new),
	;
	
	private final Function<TutorialTownPlayer, Step> stepSupplier;
	
	public Step newStep(TutorialTownPlayer tutorialTownPlayer)
	{
		return stepSupplier.apply(tutorialTownPlayer);
	}
}
