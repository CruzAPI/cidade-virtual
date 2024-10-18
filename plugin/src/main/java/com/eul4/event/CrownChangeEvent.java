package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

@Getter
public class CrownChangeEvent extends TownEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final BigDecimal oldCrowns;
	private final BigDecimal newCrowns;
	
	public CrownChangeEvent(Town town, BigDecimal oldCrowns, BigDecimal newCrowns)
	{
		super(town);
		this.oldCrowns = oldCrowns;
		this.newCrowns = newCrowns;
	}
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLER;
	}
}