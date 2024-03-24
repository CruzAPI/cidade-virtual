package com.eul4.model.craft.town.structure;

import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

import static org.bukkit.entity.EntityType.ARMOR_STAND;

public abstract class CraftFarmStructure extends CraftStructure
{
	protected long delayInTicks = 40L;
	protected long income = 1L;
	protected long balance;
	protected long maxBalance = 40L;
	
//	private long sleepTickTime;
	
	private BukkitRunnable generationTask;
	private ArmorStand hologram;
	
	public CraftFarmStructure(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock);
		
		scheduleGenerationTaskIfNotScheduledYet();
		final Block block = centerTownBlock.getBlock();
		final Location hologramLocation = block.getLocation().add(0.5, 5.0D, 0.5);
		
		hologram = (ArmorStand) block.getWorld().spawnEntity(hologramLocation, ARMOR_STAND);
		hologram.setGravity(false);
		hologram.setVisible(false);
		hologram.setMarker(true);
		hologram.setInvulnerable(true);
		hologram.setCustomNameVisible(true);
		hologram.setCustomName(balance + "/" + maxBalance);
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
		hologram.setCustomName(balance + "/" + maxBalance);
	}
}
