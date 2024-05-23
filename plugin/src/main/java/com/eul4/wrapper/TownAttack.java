package com.eul4.wrapper;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.*;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class TownAttack
{
	private final Town town;
	private final Attacker attacker;
	
	private boolean started;
	private boolean starting;
	
	private TownAttackTask townAttackTask;
	private DefenderCooldownTask defenderCooldownTask;
	private boolean onFinishCalled;
	private int defenderDeathCount;
	
	public void start()
	{
		if(started)
		{
			return;
		}
		
		starting = true;
		town.setCurrentAttack(this);
		
		townAttackTask = new TownAttackTask();
		townAttackTask.runTaskTimer(town.getPlugin(), 0L, 1L);
		
		starting = false;
		started = true;
		onStartAttack();
	}
	
	private void onStartAttack()
	{
		town.getPlayer().map(town.getPlugin().getPlayerManager()::get)
				.map(PluginPlayer.class::cast)
				.ifPresent(PluginPlayer::onStartingTownAttack);
	}
	
	private void onFinishAttack()
	{
		if(onFinishCalled)
		{
			return;
		}
		
		onFinishCalled = true;
		
		Optional.ofNullable(defenderCooldownTask).ifPresent(BukkitRunnable::cancel);
		
		forInvolvedPlayers(pluginPlayer -> pluginPlayer.sendMessage(PluginMessage.ATTACK_IS_OVER));
		
		town.getPlayer().map(town.getPlugin().getPlayerManager()::get)
				.map(PluginPlayer.class::cast)
				.ifPresent(PluginPlayer::onFinishingTownAttack);
		
		if(attacker.isValid() && !attacker.getPlayer().isDead())
		{
			attacker.reincarnate();
		}
	}
	
	public boolean isFinished()
	{
		return started && townAttackTask.isCancelled();
	}
	
	public boolean canDefenderRespawn()
	{
		return defenderCooldownTask == null || defenderCooldownTask.isCancelled();
	}
	
	public void onDefenderDeath()
	{
		defenderDeathCount++;
		Optional.ofNullable(defenderCooldownTask).ifPresent(BukkitRunnable::cancel);
		
		defenderCooldownTask = new DefenderCooldownTask();
		defenderCooldownTask.runTaskTimer(town.getPlugin(), 0L, 1L);
	}
	
	public void onAttackerDeath()
	{
		townAttackTask.cancel();
	}
	
	private void forInvolvedPlayers(Consumer<PluginPlayer> action)
	{
		for(PluginPlayer pluginPlayer : getInvolvedPlayers())
		{
			action.accept(pluginPlayer);
		}
	}
	
	private PluginPlayer[] getInvolvedPlayers()
	{
		PluginPlayer defender = town.getPluginPlayer();
		
		if(defender != null)
		{
			return new PluginPlayer[]
			{
				attacker,
				defender,
			};
		}

		return new PluginPlayer[] { attacker };
	}
	
	private class TownAttackTask extends BukkitRunnable
	{
		private int ticks = 1 * 60 * 20; //TODO maybe configurable?
		private final int maxTicks = ticks;
		
		private final BossBar bossBar = BossBar.bossBar(
				Component.text("attak"), //TODO message
				1.0F,
				BossBar.Color.RED,
				BossBar.Overlay.PROGRESS);
		
		@Override
		public void run()
		{
			updateBars();
			
			if(ticks-- <= 0 || !attacker.isValid())
			{
				cancel();
				return;
			}
		}
		
		private void updateBars()
		{
			bossBar.progress((float) ticks / maxTicks);
			
			CommonPlayer commonPlayer = town.getPlugin().getPlayerManager().get(town.getOwnerUUID());
			
			if(commonPlayer instanceof Defender
					|| commonPlayer instanceof DefenderSpectator
					|| commonPlayer instanceof RaidSpectator)
			{
				commonPlayer.getPlayer().showBossBar(bossBar);
			}
			else if(commonPlayer != null)
			{
				commonPlayer.getPlayer().hideBossBar(bossBar);
			}
		}
		
		private void clearBars()
		{
			town.getPlayer().ifPresent(player -> player.hideBossBar(bossBar));
			attacker.getPlayer().hideBossBar(bossBar);
		}
		
		@Override
		public synchronized void cancel() throws IllegalStateException
		{
			super.cancel();
			clearBars();
			onFinishAttack();
		}
	}
	
	private class DefenderCooldownTask extends BukkitRunnable
	{
		private final int maxTicks = 10 * 20;
		private int ticks = maxTicks;
		
		@Override
		public void run()
		{
			action(this::sendTitle);
			
			if(ticks-- <= 0 || !attacker.isValid())
			{
				cancel();
				action(DefenderSpectator::respawn);
			}
		}
		
		private void sendTitle(DefenderSpectator defenderSpectator)
		{
			Component componentTitle = PluginMessage.TITLE_RESPAWNING_IN.translate(defenderSpectator, ticks / 20);
			
			defenderSpectator.getPlayer().showTitle(Title.title(
					componentTitle,
					Component.empty(),
					Title.Times.times(
							Duration.ofMillis(0L),
							Duration.ofMillis(1000L),
							Duration.ofMillis(0L))));
		}
		
		private void action(Consumer<DefenderSpectator> action)
		{
			if(town.getPluginPlayer() instanceof DefenderSpectator defenderSpectator)
			{
				action.accept(defenderSpectator);
			}
		}
	}
}
