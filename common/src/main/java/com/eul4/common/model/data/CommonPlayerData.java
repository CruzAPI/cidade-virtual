package com.eul4.common.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonPlayerData
{
	private PlayerData playerData;
	
	@Builder.Default
	private boolean scoreboardEnabled = true;
}
