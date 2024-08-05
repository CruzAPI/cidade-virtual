package com.eul4.model.town.structure;

import org.bukkit.entity.Evoker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface Turret extends Structure
{
	Map<UUID, Turret> EVOKER_UUID_MAP = new HashMap<>();
	
	//TODO setFields like TownTile...
	void setEvoker(Evoker evoker);
	Evoker getEvoker();
}
