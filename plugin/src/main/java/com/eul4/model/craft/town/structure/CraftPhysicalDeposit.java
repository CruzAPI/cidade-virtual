package com.eul4.model.craft.town.structure;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.enums.Currency;
import com.eul4.enums.StructureStatus;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.PhysicalDeposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DepositAttribute;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;

import static com.eul4.util.MessageUtil.getPercentageProgressBar;

@Getter
public abstract class CraftPhysicalDeposit<N extends Number & Comparable<N>> extends CraftStructure implements PhysicalDeposit<N>
{
	private transient N capacity;
	@Setter(AccessLevel.PROTECTED)
	protected transient N remainingCapacity;
	
	public CraftPhysicalDeposit(Town town)
	{
		super(town);
	}
	
	public CraftPhysicalDeposit(Town town, TownBlock centerTownBlock, boolean isBuilt)
	{
		super(town, centerTownBlock, isBuilt);
	}
	
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
				hologram.setSize(2);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE,
						getStructureType(),
						level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack());
			}
			else
			{
				teleportHologramToDefaultLocation();
				hologram.setSize(4);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE,
						getStructureType(),
						level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack());
				hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS,
						getHealth(),
						getMaxHealth());
				hologram.getLine(3).setCustomName(getPercentageProgressBar(getHealthPercentage()));
			}
		}
		else
		{
			teleportHologramToDefaultLocation();
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE,
					getStructureType(),
					level);
			hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessage());
		}
	}
	
	protected abstract MessageArgs getStructureBalanceMessageUnderAttack();
	protected abstract MessageArgs getStructureBalanceMessage();
	
	public abstract Rule<? extends DepositAttribute<N>> getRule();
	
	public abstract Currency getCurrency();
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		capacity = getRule().getAttributeOrDefault(getBuiltLevel()).getCapacity();
	}
	
	@Override
	public void onStartAttack()
	{
		super.onStartAttack();
		remainingCapacity = capacity;
	}
}