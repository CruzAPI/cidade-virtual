package com.eul4.model.town.structure;

import org.bukkit.entity.Evoker;

public interface Turret extends Structure
{
	//TODO setFields like TownTile...
	void setEvoker(Evoker evoker);
	Evoker getEvoker();
}
