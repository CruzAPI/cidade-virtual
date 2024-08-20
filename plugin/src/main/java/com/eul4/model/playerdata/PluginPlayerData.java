package com.eul4.model.playerdata;

import com.eul4.wrapper.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PluginPlayerData
{
	private Tag tag;
	private boolean tagHidden;
	private boolean newCombat;
	
	public PluginPlayerData(Tag tag)
	{
		this.tag = tag;
	}
}
