package com.eul4.model.craft.town.structure;

import com.eul4.enums.Currency;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Deposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DepositAttribute;
import com.eul4.util.MessageUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;

import java.io.IOException;

@Getter
public abstract class CraftDeposit extends CraftResourceStructure implements Deposit
{
	private transient int capacity;
	@Setter(AccessLevel.PROTECTED)
	protected transient int remainingCapacity;
	
	public CraftDeposit(Town town)
	{
		super(town);
	}
	
	public CraftDeposit(Town town, TownBlock centerTownBlock, boolean isBuilt)
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
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), getVirtualBalance());
			}
			else
			{
				teleportHologramToDefaultLocation();
				hologram.setSize(4);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), getVirtualBalance());
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
	
	public abstract Rule<? extends DepositAttribute> getRule();
	
	public abstract Currency getCurrency();
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		capacity = getRule().getAttributeOrDefault(getBuiltLevel()).getCapacity();
	}
	
	@Override
	public int getVirtualBalance()
	{
		return Math.min(remainingCapacity, getTotalTownBalance());
	}
	
	protected abstract int getTotalTownBalance();
	
	protected abstract int subtract(int balance);
	
	@Override
	public void onStartAttack()
	{
		super.onStartAttack();
		remainingCapacity = capacity;
	}
	
	protected int getVirtualCapacity()
	{
		return Math.min(getTotalTownBalance(), remainingCapacity);
	}
	
	public boolean isEmpty()
	{
		return getVirtualCapacity() == 0;
	}
}