package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.TownHall;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.TownHallAttribute;
import com.eul4.util.MessageUtil;
import lombok.Getter;

import java.io.IOException;
import java.util.Map;

@Getter
public class CraftTownHall extends CraftStructure implements TownHall
{
	private int likeCapacity;
	private int dislikeCapacity;
	private Map<StructureType, Integer> structureLimitMap;
	
	public CraftTownHall(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, false);
	}
	
	public CraftTownHall(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	public CraftTownHall(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.TOWN_HALL;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<TownHallAttribute> getRule()
	{
		return (Rule<TownHallAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		likeCapacity = getRule().getAttribute(getLevelStatus()).getLikeCapacity();
		dislikeCapacity = getRule().getAttribute(getLevelStatus()).getDislikeCapacity();
		structureLimitMap = getRule().getAttribute(getLevelStatus()).getStructureLimit();
	}
	
	@Override
	public void updateHologram()
	{
		if(status != StructureStatus.BUILT)
		{
			super.updateHologram();
			return;
		}
		
		if(town.isUnderAttack())
		{
			hologram.setSize(5);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_TOWN_HALL_VIRTUAL_LIKES, getVirtualLikes());
			hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_TOWN_HALL_VIRTUAL_DISLIKES, getVirtualDislikes());
			hologram.getLine(3).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
			hologram.getLine(4).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
		}
		else
		{
			hologram.remove();
		}
	}
	
	private int getVirtualLikes()
	{
		return Math.min(likeCapacity, town.getLikes());
	}
	
	private int getVirtualDislikes()
	{
		return Math.min(likeCapacity, town.getLikes());
	}
}
