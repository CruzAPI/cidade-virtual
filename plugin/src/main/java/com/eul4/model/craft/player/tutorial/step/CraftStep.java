package com.eul4.model.craft.player.tutorial.step;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step;
import com.eul4.util.FaweUtil;
import com.sk89q.worldedit.math.Vector3;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import static com.eul4.common.wrapper.Pitch.getPitch;
import static org.bukkit.Sound.ENTITY_VILLAGER_TRADE;

public abstract class CraftStep extends BukkitRunnable implements Step
{
	@Getter
	protected final TutorialTownPlayer tutorialTownPlayer;
	
	private final MessageArgs messageArgs;
	private final long maxTicks;
	private final BossBar bossBar;
	
	private long ticks;
	
	public CraftStep(TutorialTownPlayer tutorialTownPlayer, MessageArgs messageArgs, MessageArgs bossBarMessageArgs)
	{
		this(tutorialTownPlayer, messageArgs, bossBarMessageArgs, Long.MAX_VALUE);
	}
	
	public CraftStep(TutorialTownPlayer tutorialTownPlayer, MessageArgs messageArgs, MessageArgs bossBarMessageArgs, long durationTicks)
	{
		this.tutorialTownPlayer = tutorialTownPlayer;
		this.messageArgs = messageArgs;
		this.ticks = durationTicks;
		this.maxTicks = durationTicks;
		this.bossBar = BossBar.bossBar(bossBarMessageArgs.translate(tutorialTownPlayer),
				1.0F,
				BossBar.Color.GREEN,
				BossBar.Overlay.PROGRESS);
	}
	
	@Override
	public void run()
	{
		if(ticks < 0L)
		{
			completeStep();
			return;
		}
		
		if(ticks % (5L * 20L) == 0L)
		{
			tutorialTownPlayer.clearChat(20);
			tutorialTownPlayer.sendMessage(messageArgs);
		}
		
		updateBossBarProgress();
		onRun();
		
		ticks--;
	}
	
	protected void onRun()
	{
	
	}
	
	@Override
	public synchronized @NotNull BukkitTask runTaskTimer(@NotNull Plugin plugin, long delay, long period)
			throws IllegalArgumentException, IllegalStateException
	{
		BukkitTask task = super.runTaskTimer(plugin, delay, period);
		onSchedule();
		return task;
	}
	
	protected void onSchedule()
	{
		tutorialTownPlayer.getPlayer()
				.playSound(getAssistant().getLocation(), ENTITY_VILLAGER_TRADE, 1.0F, getPitch(9));
		tutorialTownPlayer.getPlayer().showBossBar(bossBar);
	}
	
	public boolean isInfinite()
	{
		return maxTicks == Long.MAX_VALUE;
	}
	
	protected void updateBossBarProgress()
	{
		bossBar.progress(Math.max(0.0F, Math.min(1.0F, (float) ticks / maxTicks)));
	}
	
	protected void assistantTargetPlayer()
	{
		target(tutorialTownPlayer.getTown().getAssistant(), tutorialTownPlayer.getPlayer());
	}
	
	protected void targetTownHall(Entity entity)
	{
		target(entity, tutorialTownPlayer.getTown().getTownHall().getCenterPosition());
	}
	
	protected void targetLikeGenerator(Entity entity)
	{
		tutorialTownPlayer.getTown().findFirstLikeGenerator()
				.ifPresent(likeGenerator ->
				{
					target(entity, likeGenerator.getCenterPosition());
				});
	}
	
	protected void targetDislikeGenerator(Entity entity)
	{
		tutorialTownPlayer.getTown().findFirstDislikeGenerator()
				.ifPresent(dislikeGenerator ->
				{
					target(entity, dislikeGenerator.getCenterPosition());
				});
	}
	
	protected void target(Entity entity, Entity target)
	{
		target(entity, FaweUtil.toFaweVector(target.getLocation().toVector()));
	}
	
	protected void target(Entity entity, Vector3 targetPosition)
	{
		final Vector3 entityPosition = FaweUtil.toFaweVector(entity.getLocation().toVector());
		final Vector3 direction = targetPosition.subtract(entityPosition);
		
		final float yaw = (float) Math.toDegrees(Math.atan2(direction.z(), direction.x())) - 90.0F;
		//final float pitch = (float) Math.toDegrees(Math.atan2(-direction.y(), direction.length()));
		
		entity.setRotation(yaw, 0.0F);
	}
	
	@Override
	public Villager getAssistant()
	{
		return tutorialTownPlayer.getTown().getAssistant();
	}
	
	@Override
	public synchronized void cancel() throws IllegalStateException
	{
		super.cancel();
		tutorialTownPlayer.getPlayer().hideBossBar(bossBar);
	}
}
