package com.eul4.model.craft.town.structure;

import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Generator;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GeneratorAttribute;
import com.eul4.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Optional;

@Getter
@Setter
public abstract class CraftGenerator extends CraftStructure implements Generator
{
	@Getter
	protected int balance;
	
	private transient BukkitRunnable generationTask;
	
	private transient int capacity;
	private transient int delay;
	
	public CraftGenerator(Town town)
	{
		super(town);
	}
	
	public CraftGenerator(Town town, TownBlock centerTownBlock, boolean isBuilt)
			throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
		
		scheduleGenerationTaskIfPossible();
	}
	
	private void scheduleGenerationTaskIfPossible()
	{
		if(town.getPlugin().isQueued(generationTask) || status != StructureStatus.BUILT)
		{
			return;
		}
		
		generationTask = new BukkitRunnable()
		{
			private long tick;
			
			@Override
			public void run()
			{
				if(delay != 0 && ++tick % delay == 0)
				{
					generateIncome();
				}
			}
		};
		
		generationTask.runTaskTimer(town.getPlugin(), 0L, 1L);
	}
	
	private void generateIncome()
	{
		if(isFull() || status != StructureStatus.BUILT || town.isUnderAttack())
		{
			return;
		}
		
		balance = Math.min(getCapacity(), balance + getIncome());
		updateHologram();
		updateInventoryView();
	}
	
	private boolean isFull()
	{
		return balance >= capacity;
	}
	
	private void updateInventoryView()
	{
		if(!(town.getPlugin().getPlayerManager().get(town.getOwner().getUniqueId()) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		if(!(townPlayer.getGui() instanceof StructureGui structureGui) || structureGui.getStructure() != this)
		{
			return;
		}
		
		structureGui.updateTitle();
	}
	
	private int getIncome()
	{
		return town.getOwner().isOnline() ? 2 : 1;
	}
	
	public void updateHologram()
	{
		town.getPlugin().getLogger().info("A"); //TODO investigate hologram inversion on rule reload
		
		if(status != StructureStatus.BUILT)
		{
			super.updateHologram();
			return;
		}
		
		if(town.isUnderAttack())
		{
			town.getPlugin().getLogger().info("B"); //TODO
			
			if(isDestroyed())
			{
				teleportHologram();
				hologram.setSize(2);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), balance);
			}
			else
			{
				hologram.setSize(4);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), balance);
				hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(3).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
			}
		}
		else
		{
			town.getPlugin().getLogger().info("C"); //TODO
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			hologram.getLine(1).setMessageAndArgs(PluginMessage.HOLOGRAM_LIKE_FARM_LINE2, balance, getCapacity());
		}
	}
	
	protected abstract PluginMessage getStructureBalanceMessageUnderAttack();
	
	@Override
	public void collect()
	{
		int balanceLimit = getTownBalanceLimit();
		int balance = getTownBalance();
		
		int canReceive = balanceLimit - balance;
		
		int toCollect = Math.min(this.balance, canReceive);
		
		this.balance -= toCollect;
		
		setTownBalance(balance + toCollect);
		updateHologram();
	}
	
	public abstract int getTownBalanceLimit();
	
	public abstract int getTownBalance();
	
	public abstract void setTownBalance(int balance);
	
	@Override
	public void load()
	{
		super.load();
		scheduleGenerationTaskIfPossible();
	}
	
	@Override
	protected void onBuildFinish()
	{
		super.onBuildFinish();
		scheduleGenerationTaskIfPossible();
	}
	
	@Override
	protected void onBuildStart()
	{
		super.onBuildStart();
		Optional.ofNullable(generationTask).ifPresent(BukkitRunnable::cancel);
	}
	
	public abstract Rule<? extends GeneratorAttribute> getRule();
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		capacity = getRule().getAttributeOrDefault(getLevelStatus()).getCapacity();
		delay = getRule().getAttributeOrDefault(getLevelStatus()).getDelay();
	}
}