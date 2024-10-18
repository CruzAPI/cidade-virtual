package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public abstract class TownEvent extends Event
{
	protected final Town town;
}