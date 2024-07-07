package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Turret;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.TurretAttribute;
import com.eul4.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;

import java.io.IOException;

@Getter
@Setter
public class CraftTurret extends CraftStructure implements Turret
{
	public CraftTurret(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, false);
	}
	
	public CraftTurret(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	public CraftTurret(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.TURRET;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<TurretAttribute> getRule()
	{
		return (Rule<TurretAttribute>) getStructureType().getRule(town.getPlugin());
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
			if(isDestroyed())
			{
				teleportHologram(getLocation()
						.toHighestLocation()
						.getBlock()
						.getRelative(BlockFace.UP)
						.getLocation()
						.toCenterLocation());
				hologram.setSize(1);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			}
			else
			{
				hologram.setSize(3);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(2).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
				teleportHologramToDefaultLocation();
			}
		}
		else
		{
			hologram.setSize(1);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			teleportHologramToDefaultLocation();
		}
	}
}
