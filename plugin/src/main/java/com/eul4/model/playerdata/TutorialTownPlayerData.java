package com.eul4.model.playerdata;

import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialTownPlayerData
{
	private CheckpointStepEnum checkpointStep;
}
