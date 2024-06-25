package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Cannon;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.CannonAttribute;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class CraftCannon extends CraftStructure implements Cannon
{
	public CraftCannon(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, false);
	}
	
	public CraftCannon(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	public CraftCannon(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.CANNON;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<CannonAttribute> getRule()
	{
		return (Rule<CannonAttribute>) getStructureType().getRule(town.getPlugin());
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
