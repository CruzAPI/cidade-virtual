package com.eul4.model.playerdata;

import com.eul4.common.model.data.ExternalObject;
import com.eul4.model.town.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TownPlayerData implements ExternalObject
{
	public static final byte VERSION = 0;
	
	private boolean test;
}
