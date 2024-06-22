package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Armory;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.ArmoryAttribute;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CraftArmory extends CraftStructure implements Armory
{
	public CraftArmory(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, false);
	}
	
	public CraftArmory(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	public CraftArmory(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.ARMORY;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<ArmoryAttribute> getRule()
	{
		return (Rule<ArmoryAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	public void updateHologram()
	{
		if(status != StructureStatus.BUILT)
		{
			super.updateHologram();
			return;
		}
	}
}
