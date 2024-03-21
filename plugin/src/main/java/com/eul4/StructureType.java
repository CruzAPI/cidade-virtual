package com.eul4;

import com.eul4.model.craft.town.structure.CraftDislikeFarm;
import com.eul4.model.craft.town.structure.CraftLikeFarm;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StructureType
{
	TOWN_HALL(CraftTownHall::new, "prefeitura"),
	LIKE_FARM(CraftLikeFarm::new, "likefarm"),
	DISLIKE_FARM(CraftDislikeFarm::new, "dislikefarm"),
	;
	
	private final StructureInstantiation instantiation;
	private final String name;
}
