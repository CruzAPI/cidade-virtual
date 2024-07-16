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
import com.eul4.util.SoundUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Optional;

import static com.eul4.i18n.PluginMessage.*;

@Getter
@Setter
public abstract class CraftGenerator extends CraftResourceStructure implements Generator
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
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), balance);
			}
			else
			{
				teleportHologramToDefaultLocation();
				hologram.setSize(4);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(getStructureBalanceMessageUnderAttack(), balance);
				hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(3).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
			}
		}
		else
		{
			teleportHologramToDefaultLocation();
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			hologram.getLine(1).setMessageAndArgs(PluginMessage.HOLOGRAM_LIKE_FARM_LINE2, balance, getCapacity());
		}
	}
	
	protected abstract PluginMessage getStructureBalanceMessageUnderAttack();
	
	@Override
	public final void collect()
	{
		final int currentBalance = this.balance;
		final boolean depositAlreadyFull = hasReachedMaxTownBalanceCapacity();
		
		int toCollect = getPossibleAmountToCollect();
		
		this.balance -= toCollect;
		
		setTownBalance(getTownBalance() + toCollect);
		
		updateHologram();
		
		final boolean depositFulled = hasReachedMaxTownBalanceCapacity();
		
		if(currentBalance == 0 && toCollect == 0)
		{
			town.findPluginPlayer().ifPresent(pluginPlayer -> pluginPlayer
					.sendMessage(GENERATOR_COLLECT_EMPTY, this));
			town.getPlayer().ifPresent(SoundUtil::playPiano);
		}
		else if(toCollect == 0 && depositAlreadyFull)
		{
			town.findPluginPlayer().ifPresent(pluginPlayer -> pluginPlayer
					.sendMessage(GENERATOR_COLLECT_DEPOSIT_FULL, this));
			town.getPlayer().ifPresent(SoundUtil::playPlong);
		}
		else
		{
			town.findPluginPlayer().ifPresent(pluginPlayer -> pluginPlayer
					.sendMessage(GENERATOR_COLLECT, this, toCollect));
			
			if(depositFulled)
			{
				town.findPluginPlayer().ifPresent(pluginPlayer -> pluginPlayer.sendMessage(
						GENERATOR_COLLECT_DEPOSIT_FULLED, this));
				town.getPlayer().ifPresent(SoundUtil::playPiano);
			}
			else
			{
				town.getPlayer().ifPresent(SoundUtil::playPling);
			}
		}
	}
	
	@Override
	public final int getPossibleAmountToCollect()
	{
		int balanceLimit = getTownBalanceLimit();
		int balance = getTownBalance();
		
		int canReceive = balanceLimit - balance;
		
		return Math.max(0, Math.min(this.balance, canReceive));
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
		
		capacity = getRule().getAttributeOrDefault(getBuiltLevel()).getCapacity();
		delay = getRule().getAttributeOrDefault(getBuiltLevel()).getDelay();
	}
	
	protected int subtract(int value)
	{
		if(value < 0)
		{
			throw new UnsupportedOperationException("subtracted value can't be negative.");
		}
		
		if(value > balance)
		{
			int currentCount = balance;
			balance = 0;
			onSubtractBalance();
			return currentCount;
		}
		
		balance -= value;
		onSubtractBalance();
		return value;
	}
	
	private void onSubtractBalance()
	{
		updateHologram();
	}
	
	public boolean isEmpty()
	{
		return balance == 0;
	}
}