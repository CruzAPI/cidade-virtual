package com.eul4.wrapper;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CooldownMap
{
	private final Main plugin;
	
	@ToString.Include
	private final Map<UUID, Long> cooldowns = new HashMap<>();
	@ToString.Include
	private final Map<UUID, BukkitTask> removeFromCooldownTasks = new HashMap<>();
	
	public boolean isInCooldown(CommonPlayer commonPlayer)
	{
		return isInCooldown(commonPlayer.getUniqueId());
	}
	
	public boolean isInCooldown(UUID uniqueId)
	{
		return getRemainingCooldownTicks(uniqueId) > 0L;
	}
	
	public void putInCooldown(CommonPlayer commonPlayer, long ticks)
	{
		putInCooldown(commonPlayer.getUniqueId(), ticks);
	}
	
	public void putInCooldown(UUID uniqueId, long ticks)
	{
		Preconditions.checkArgument(ticks > 0L);
		scheduleRemoveFromCooldownTask(uniqueId, ticks);
		cooldowns.put(uniqueId, plugin.getTotalTick() + ticks);
	}
	
	private void scheduleRemoveFromCooldownTask(UUID uniqueId, long delay)
	{
		BukkitTask oldTask = removeFromCooldownTasks.put(uniqueId, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(!isInCooldown(uniqueId))
				{
					removeFromCooldown(uniqueId);
				}
			}
		}.runTaskLater(plugin, delay));
		
		Optional.ofNullable(oldTask).ifPresent(BukkitTask::cancel);
	}
	
	private void removeFromCooldown(UUID uniqueId)
	{
		cooldowns.remove(uniqueId);
		removeFromCooldownTasks.remove(uniqueId);
	}
	
	public long getRemainingCooldownTicks(CommonPlayer commonPlayer)
	{
		return getRemainingCooldownTicks(commonPlayer.getUniqueId());
	}
	
	public long getRemainingCooldownTicks(UUID uniqueId)
	{
		if(!cooldowns.containsKey(uniqueId))
		{
			return 0L;
		}
		
		return Math.max(0L, cooldowns.get(uniqueId) - plugin.getTotalTick());
	}
	
	public Long getAbsolutRemainingCooldownTicks(UUID uniqueId)
	{
		if(!cooldowns.containsKey(uniqueId))
		{
			return null;
		}
		
		return cooldowns.get(uniqueId) - plugin.getTotalTick();
	}
}
