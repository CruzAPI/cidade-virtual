package com.eul4.model.craft.town.structure;

import com.eul4.enums.Currency;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Deposit;
import com.eul4.model.town.structure.Generator;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DepositAttribute;
import com.eul4.rule.attribute.GeneratorAttribute;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.util.Optional;

@Getter
public abstract class CraftDeposit extends CraftStructure implements Deposit
{
	private transient int capacity;
	
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
		}
		else
		{
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_DEPOSIT_CAPACITY_HOLOGRAM, getCapacity(), getCurrency());
		}
	}
	
	public abstract Rule<? extends DepositAttribute> getRule();
	
	public abstract Currency getCurrency();
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		capacity = getRule().getAttributeOrDefault(getLevelStatus()).getCapacity();
	}
}