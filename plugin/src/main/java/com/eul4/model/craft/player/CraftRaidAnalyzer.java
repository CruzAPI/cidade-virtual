package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.util.MessageUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.function.Predicate.not;

@Getter
public class CraftRaidAnalyzer extends CraftSpiritualPlayer implements RaidAnalyzer
{
	private static final Random RANDOM = new Random();
	
	private final RaidAnalyzerHotbar hotbar = new RaidAnalyzerHotbar(this);
	private final Set<Town> skippedTowns = new HashSet<>();
	
	private BukkitRunnable analysisTask;
	
	private Town analyzingTown;
	
	private Future<Optional<Town>> rerollFuture;
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	public CraftRaidAnalyzer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftRaidAnalyzer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		hotbar.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}
	
	@Override
	public void analyzeTown(Town town)
	{
		analyzingTown = town;
		
		Title title = Title.title(
				PluginMessage.TITLE_OWNER_TOWN.translate(this, town.getOwner()),
				PluginMessage.SUBTITLE_SECONDS_TO_ANALYZE.translate(this, 50),
				Title.Times.times(
						Duration.ofMillis(0L),
						Duration.ofMillis(2500L),
						Duration.ofMillis(1000L)));
		
		scheduleAnalyzeTimer();
		hotbar.reset();
		player.teleport(town.getLocation().toHighestLocation());
		player.showTitle(title);
	}
	
	@Override
	public void attack()
	{
		//TODO: convert to RaidPlayer
		Bukkit.broadcastMessage("attak!");
		cancelAnalysisTask();
	}
	
	private void cancelAnalysisTask()
	{
		Optional.ofNullable(analysisTask)
				.filter(not(BukkitRunnable::isCancelled))
				.ifPresent(BukkitRunnable::cancel);
	}
	
	private boolean isRerolling()
	{
		return rerollFuture != null && !rerollFuture.isDone();
	}
	
	private void scheduleAnalyzeTimer()
	{
		cancelAnalysisTask();
		analysisTask = new AnalysisTask();
		analysisTask.runTaskTimer(plugin, 0L, 1L);
	}
	
	@Override
	public void reroll()
	{
		if(isRerolling())
		{
			return;
		}
		
		cancelAnalysisTask();
		skippedTowns.add(analyzingTown);
		
		rerollFuture = executorService.submit(this::findRandomTown);
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
		{
			try
			{
				Optional<Town> optionalTown = rerollFuture.get();
				
				plugin.getServer().getScheduler().getMainThreadExecutor(plugin)
						.execute(() -> optionalTown.ifPresentOrElse(this::analyzeTown, () ->
						{
							cancel();
							sendMessage(PluginMessage.NO_TOWNS_FOUND);
						}));
			}
			catch(InterruptedException | ExecutionException ignore)
			{
			
			}
		});
	}
	
	@Override
	public void cancel()
	{
		if(isRerolling())
		{
			executorService.shutdownNow();
		}
		
		cancelAnalysisTask();
		player.showTitle(Title.title(Component.empty(), Component.empty()));
		load();
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		cancelAnalysisTask();
	}
	
	@Override
	public PluginPlayer load()
	{
		commonPlayerData.getPlayerData().apply(player);
		return (PluginPlayer) plugin.getPlayerManager().register(player, this, PhysicalPlayerType.TOWN_PLAYER);
	}
	
	@Override
	public boolean hasSkipped(Town town)
	{
		return skippedTowns.contains(town);
	}
	
	@SneakyThrows
	public Optional<Town> findRandomTown()
	{
		Town attackerTown = getTown();
		
		Map<Integer, List<Town>> onlineTownsLeveled = new HashMap<>();
		Map<Integer, List<Town>> offlineTownsLeveled = new HashMap<>();
		
		Collection<Town> towns = plugin.getTownManager().getTowns().values();
		int i = 0;
		int size = towns.size();
		
		for(Town town : plugin.getTownManager().getTowns().values())
		{
			int percentage = (int) ((double) ++i / size * 100.0D);
			player.showTitle(Title.title(
					PluginMessage.TITLE_SEARCHING.translate(this),
					MessageUtil.getPercentageProgressBar(percentage),
					Title.Times.times(
							Duration.ZERO,
							Duration.ofSeconds(1L),
							Duration.ZERO)));
			
			Thread.sleep(20L);
			
			if(town.isOwner(player) || hasSkipped(town) || !town.canBeAnalyzed())
			{
				continue;
			}
			
			(town.isOnline() ? onlineTownsLeveled : offlineTownsLeveled)
					.computeIfAbsent(town.getLevel(), x -> new ArrayList<>()).add(town);
		}
		
		return findNearestLeveledRandomTown(attackerTown.getLevel(), onlineTownsLeveled, offlineTownsLeveled);
	}
	
	private Optional<Town> findNearestLeveledRandomTown(final int level,
			final Map<Integer, List<Town>> onlineTownsLeveled,
			final Map<Integer, List<Town>> offlineTownsLeveled)
	{
		final int nearestOfflineLevel = findNearestLevel(level, offlineTownsLeveled.keySet());
		final int nearestOnlineLevel = findNearestLevel(level, onlineTownsLeveled.keySet());
		
		final List<Town> towns =
				(nearestOfflineLevel == nearestOnlineLevel ?
						RANDOM.nextBoolean() :
						nearestOnlineLevel > nearestOfflineLevel)
						? onlineTownsLeveled.getOrDefault(nearestOnlineLevel, Collections.emptyList())
						: offlineTownsLeveled.getOrDefault(nearestOfflineLevel, Collections.emptyList());
		
		return towns.isEmpty() ? Optional.empty() : Optional.of(towns.get(RANDOM.nextInt(towns.size())));
	}
	
	private int findNearestLevel(int townLevel, Set<Integer> availableLevels)
	{
		int nearest = -1;
		int lowestGap = Integer.MAX_VALUE;
		
		for(int availableLevel : availableLevels)
		{
			int gap = Math.abs(townLevel - availableLevel);
			
			if(gap < lowestGap)
			{
				nearest = availableLevel;
				lowestGap = gap;
			}
			else if(gap == lowestGap && availableLevel > nearest)
			{
				nearest = availableLevel;
			}
		}
		
		return nearest;
	}
	
	private class AnalysisTask extends BukkitRunnable
	{
		private int ticks = 50 * 20;
		private final int maxTicks = ticks;
		private final BossBar bossBar = BossBar.bossBar(
				PluginMessage.ANALYZING_TOWN.translate(CraftRaidAnalyzer.this, analyzingTown.getOwner()),
				1.0F,
				BossBar.Color.PURPLE,
				BossBar.Overlay.PROGRESS);
		
		@Override
		public void run()
		{
			if(ticks == 0)
			{
				cancel();
				attack();
				return;
			}
			
			updateBars();
			ticks--;
		}
		
		@Override
		public synchronized void cancel() throws IllegalStateException
		{
			super.cancel();
			clearBars();
		}
		
		private void clearBars()
		{
			player.setLevel(0);
			player.hideBossBar(bossBar);
		}
		
		private void updateBars()
		{
			player.setLevel((int) Math.ceil(ticks / 20.0D));
			bossBar.progress((float) ticks / maxTicks);
			
			player.showBossBar(bossBar);
		}
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return false;
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.RAID_ANALYZER;
	}
}
