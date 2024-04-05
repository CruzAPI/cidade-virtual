package com.eul4.model.craft.town.structure;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.wrapper.VectorSerializable;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Generator;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.util.Optional;

public abstract class CraftFarmStructure extends CraftStructure implements Generator
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	protected long delayInTicks = 40L;
	
	@Getter
	protected int balance;
	@Getter
	protected int maxBalance = 40;
	
	private transient BukkitRunnable generationTask;
	
	public CraftFarmStructure(Town town)
	{
		super(town);
	}
	
	public CraftFarmStructure(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, new Vector(0.5D, 5.0D, 0.5D), isBuilt);
		
		hologram.newLine(PluginMessage.HOLOGRAM_LIKE_FARM_LINE1, level);
		hologram.newLine(PluginMessage.HOLOGRAM_LIKE_FARM_LINE2, balance, maxBalance);
		
		scheduleGenerationTaskIfPossible();
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
		
		final long version = in.readLong();
		
		if(version == 1L)
		{
			delayInTicks = in.readLong();
			balance = in.readInt();
			maxBalance = in.readInt();
		}
		else
		{
			throw new RuntimeException("CraftFarmStructure serial version not found: " + version);
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		super.writeExternal(out);
		
		out.writeLong(serialVersionUID);
		
		out.writeLong(delayInTicks);
		out.writeInt(balance);
		out.writeInt(maxBalance);
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
				if(++tick % delayInTicks == 0)
				{
					generateIncome();
				}
			}
		};
		
		generationTask.runTaskTimer(town.getPlugin(), 0L, 1L);
	}
	
	private void generateIncome()
	{
		balance = Math.min(maxBalance, balance + getIncome());
		updateHologram();
		updateInventoryView();
	}
	
	private void updateInventoryView()
	{
		if(!(town.getPlugin().getPlayerManager().get(town.getOwner().getUniqueId()) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		if(!(townPlayer.getGui() instanceof StructureGui structureGui)
				|| structureGui.getStructure() != this)
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
		}
		else
		{
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.HOLOGRAM_LIKE_FARM_LINE1, level);
			hologram.getLine(1).setMessageAndArgs(PluginMessage.HOLOGRAM_LIKE_FARM_LINE2, balance, maxBalance);
		}
	}
	
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
}
