package com.eul4.model.craft.town.structure;

import com.eul4.common.hologram.Hologram;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

import static org.bukkit.entity.EntityType.ARMOR_STAND;
import static org.bukkit.entity.EntityType.BAT;

public abstract class CraftFarmStructure extends CraftStructure
{
	protected long delayInTicks = 40L;
	protected int income = 1;
	protected int balance;
	protected int maxBalance = 40;
	
//	private long sleepTickTime;
	
	private BukkitRunnable generationTask;
	private Hologram hologram;
	
	public CraftFarmStructure(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock);
		
		final Block block = centerTownBlock.getBlock();
		final Location hologramLocation = block.getLocation().add(0.5, 5.0D, 0.5);
		hologram = new Hologram(town.getPlugin(), hologramLocation);
		hologram.newLine(PluginMessage.HOLOGRAM_LIKE_FARM_LINE1, level);
		hologram.newLine(PluginMessage.HOLOGRAM_LIKE_FARM_LINE2, balance, maxBalance);
		
		scheduleGenerationTaskIfNotScheduledYet();
	}
	
	private void scheduleGenerationTaskIfNotScheduledYet()
	{
		if(generationTask != null &&
				town.getPlugin().getServer().getScheduler().isQueued(generationTask.getTaskId()))
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
		balance = Math.min(maxBalance, balance + income);
		hologram.getLine(1).setMessageAndArgs(PluginMessage.HOLOGRAM_LIKE_FARM_LINE2, balance, maxBalance);
	}
}
