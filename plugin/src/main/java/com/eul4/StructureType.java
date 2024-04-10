package com.eul4;

import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.function.StructureInstantiation;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.GenericAttribute;
import com.eul4.rule.Rule;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface StructureType<S extends Structure, A extends GenericAttribute>
{
	StructureInstantiation getInstantiation();
	Function<Town, S> getNewStructureTown();
	BiFunction<CommonPlayer, Structure, StructureGui> getNewStructureGui();
	String name();
	Message getNameMessage();
	Rule<A> getRule(Main plugin);
	
	int ordinal();
	
	static List<StructureType<?, ?>> values()
	{
		return StructureTypeEnum.values();
	}
	
	static StructureType<?, ?> valueOf(String name)
	{
		return StructureTypeEnum.valueOf(name);
	}
}
