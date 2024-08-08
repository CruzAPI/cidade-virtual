package com.eul4.model.playerdata;

import com.eul4.Main;
import com.eul4.wrapper.HomeMap;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
public class VanillaPlayerData
{
	private final HomeMap homeMap;
	
	@Builder
	public VanillaPlayerData(HomeMap homeMap)
	{
		this.homeMap = Objects.requireNonNull(homeMap);
	}
	
	public VanillaPlayerData(Main plugin)
	{
		this.homeMap = new HomeMap(plugin);
	}
}
