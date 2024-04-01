package com.eul4;

import com.eul4.function.StructureInstantiation;
import com.eul4.model.craft.town.structure.CraftDislikeFarm;
import com.eul4.model.craft.town.structure.CraftLikeFarm;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum StructureType
{
	TOWN_HALL(CraftTownHall::new, CraftTownHall::new, "prefeitura"),
	LIKE_FARM(CraftLikeFarm::new, CraftLikeFarm::new, "likefarm"),
	DISLIKE_FARM(CraftDislikeFarm::new, CraftDislikeFarm::new, "dislikefarm"),
	;
	
	private final StructureInstantiation instantiation;
	private final Function<Town, Structure> newStructureTown;
	private final String name;
}
