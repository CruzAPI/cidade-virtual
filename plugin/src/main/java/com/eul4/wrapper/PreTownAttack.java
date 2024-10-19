package com.eul4.wrapper;

import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.spiritual.RaidAnalyzer;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.type.player.SpiritualPlayerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;

public class PreTownAttack
{
	private final Town town;
	private final RaidAnalyzer raidAnalyzer;
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	private boolean started;
	private LoadingTask loadingTask;
	
	public PreTownAttack(RaidAnalyzer raidAnalyzer)
	{
		this.town = Objects.requireNonNull(raidAnalyzer.getAnalyzingTown());
		this.raidAnalyzer = raidAnalyzer;
	}
	
	public void start()
	{
		if(started)
		{
			return;
		}
		
		started = true;
		town.getStructureMap().values().forEach(Structure::onStartPreAttack);
		onStart();
	}
	
	private void onStart()
	{
		Future<Void> future = town.copyAndSaveTownSchematic(executorService);
		(loadingTask = new LoadingTask()).runTaskTimer(town.getPlugin(), 0L, 1L);
		
		town.getPlugin()
				.getServer()
				.getScheduler()
				.runTaskAsynchronously(town.getPlugin(), () -> whenComplete(future));
	}
	
	private void whenComplete(Future<?> future)
	{
		Executor mainExecutor = town.getPlugin().getServer().getScheduler().getMainThreadExecutor(town.getPlugin());
		
		try
		{
			future.get();
			mainExecutor.execute(this::onSuccessfullyComplete);
		}
		catch(ExecutionException e)
		{
			mainExecutor.execute(this::onInterrupt);
			town.getPlugin().getLogger().log(Level.WARNING, "ExecutionException", e.getCause());
		}
		catch(InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void onSuccessfullyComplete()
	{
		Optional.ofNullable(loadingTask).ifPresent(LoadingTask::successfullyCancel);
		
		if(raidAnalyzer.isValid())
		{
			town.getPlugin().getPlayerManager().register(raidAnalyzer, SpiritualPlayerType.ATTACKER);
		}
	}
	
	private void onInterrupt()
	{
		Optional.ofNullable(loadingTask).ifPresent(LoadingTask::interruptCancel);
	}
	
	public void cancel()
	{
		executorService.shutdownNow();
		Optional.ofNullable(loadingTask).filter(Predicate.not(LoadingTask::isCancelled)).ifPresent(LoadingTask::interruptCancel);
	}
	
	private class LoadingTask extends BukkitRunnable
	{
		@Override
		public void run()
		{
			if(executorService.isShutdown())
			{
				interruptCancel();
				return;
			}
			
			forInvolvedPlayers(this::sendTitle);
		}
		
		public synchronized void interruptCancel()
		{
			forInvolvedPlayers(this::sendTitleCancelled);
			super.cancel();
		}
		
		public synchronized void successfullyCancel()
		{
			forInvolvedPlayers(this::sendTitleCompleted);
			super.cancel();
		}
		
		private void sendTitle(PluginPlayer pluginPlayer)
		{
			pluginPlayer.getPlayer().showTitle(Title.title(
					Component.text("Loading..."),
					Component.empty(),
					Title.Times.times(
							Duration.ZERO,
							Duration.ofMillis(1000L),
							Duration.ZERO)));
		}
		
		private void sendTitleCancelled(PluginPlayer pluginPlayer)
		{
			pluginPlayer.getPlayer().showTitle(Title.title(
					Component.text("Cancelled!"),
					Component.empty(),
					Title.Times.times(
							Duration.ZERO,
							Duration.ofMillis(1000L),
							Duration.ofMillis(200L))));
		}
		
		private void sendTitleCompleted(PluginPlayer pluginPlayer)
		{
			pluginPlayer.getPlayer().showTitle(Title.title(
					Component.text("Completed!"),
					Component.empty(),
					Title.Times.times(
							Duration.ZERO,
							Duration.ofMillis(1000L),
							Duration.ofMillis(200L))));
		}
		
		private void forInvolvedPlayers(Consumer<PluginPlayer> action)
		{
			for(PluginPlayer pluginPlayer : getInvolvedPlayers())
			{
				if(pluginPlayer.isValid())
				{
					action.accept(pluginPlayer);
				}
			}
		}
		
		private PluginPlayer[] getInvolvedPlayers()
		{
			if(town.getPluginPlayer() instanceof TownPlayer townPlayer)
			{
				return new PluginPlayer[]
				{
					raidAnalyzer,
					townPlayer,
				};
			}
			
			return new PluginPlayer[] { raidAnalyzer };
		}
	}
}
