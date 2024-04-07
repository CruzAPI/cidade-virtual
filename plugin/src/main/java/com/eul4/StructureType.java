package com.eul4;

import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.function.StructureInstantiation;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.craft.town.structure.CraftDislikeFarm;
import com.eul4.model.craft.town.structure.CraftLikeFarm;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.inventory.craft.CraftDislikeGeneratorGui;
import com.eul4.model.inventory.craft.CraftLikeGeneratorGui;
import com.eul4.model.inventory.craft.CraftTownHallGui;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum StructureType
{
	TOWN_HALL(CraftTownHall::new, CraftTownHall::new, CraftTownHallGui::new, "prefeitura", PluginMessage.STRUCTURE_TOWN_HALL_NAME),
	LIKE_GENERATOR(CraftLikeFarm::new, CraftLikeFarm::new, CraftLikeGeneratorGui::new, "likefarm", PluginMessage.STRUCTURE_LIKE_GENERATOR_NAME),
	DISLIKE_GENERATOR(CraftDislikeFarm::new, CraftDislikeFarm::new, CraftDislikeGeneratorGui::new, "dislikefarm", PluginMessage.STRUCTURE_DISLIKE_GENERATOR_NAME),
	;
	
	private final StructureInstantiation instantiation;
	private final Function<Town, Structure> newStructureTown;
	private final BiFunction<CommonPlayer, Structure, StructureGui> newStructureGui;
	private final String name;
	private final Message nameMessage;
}
