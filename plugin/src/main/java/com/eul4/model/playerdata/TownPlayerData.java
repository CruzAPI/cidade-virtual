package com.eul4.model.playerdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
public class TownPlayerData
{
	private boolean test;
	private boolean firstTimeJoiningTown;
	
	public TownPlayerData()
	{
		firstTimeJoiningTown = true;
	}
}
