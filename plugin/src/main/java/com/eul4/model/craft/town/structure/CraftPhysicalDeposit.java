package com.eul4.model.craft.town.structure;

import com.eul4.enums.Currency;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.PhysicalDeposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DepositAttribute;
import com.eul4.util.MessageUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;

import java.io.IOException;

@Getter
public abstract class CraftPhysicalDeposit<N extends Number> extends CraftStructure implements PhysicalDeposit<N>
{
	private transient N capacity;
	@Setter(AccessLevel.PROTECTED)
	protected transient N remainingCapacity;
	
	public CraftPhysicalDeposit(Town town)
	{
		super(town);
	}
	
	public CraftPhysicalDeposit(Town town, TownBlock centerTownBlock, boolean isBuilt)
			throws CannotConstructException, IOException
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
				teleportHologram(getLocation().toHighestLocation().getBlock().getRelative(BlockFace.UP).getLocation().toCenterLocation());
				hologram.setSize(2);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), getHolder().getBalance());
			}
			else
			{
				teleportHologramToDefaultLocation();
				hologram.setSize(4);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), getHolder().getBalance());
				hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(3).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
			}
		}
		else
		{
			teleportHologramToDefaultLocation();
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_DEPOSIT_CAPACITY_HOLOGRAM, getCapacity(), getCurrency());
		}
	}
	
	protected abstract PluginMessage getStructureBalanceMessageUnderAttack();
	
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