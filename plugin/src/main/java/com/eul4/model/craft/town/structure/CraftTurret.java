package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.wrapper.Pitch;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Turret;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.TurretAttribute;
import com.eul4.util.MessageUtil;
import com.eul4.wrapper.TownAttack;
import io.papermc.paper.entity.TeleportFlag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eul4.common.constant.CommonNamespacedKey.FAWE_IGNORE;
import static com.eul4.common.wrapper.Pitch.F1;
import static org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST;
import static org.bukkit.Sound.ENTITY_SHULKER_SHOOT;
import static org.bukkit.entity.EntityType.*;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CUSTOM;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@Getter
@Setter
public class CraftTurret extends CraftStructure implements Turret
{
	private transient double attackDamage;
	private transient int attackSpeed;
	private transient double missileSpeed;
	private transient double range;
	
	private transient Player target;
	
	private transient TurretTask turretTask;
	private transient List<MissileTask> missileTasks = new ArrayList<>();
	
	private Evoker evoker;
	
	public CraftTurret(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftTurret(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
		this.evoker = (Evoker) centerTownBlock.getBlock()
				.getWorld()
				.spawnEntity(getDefaultEvokerLocation(), EVOKER, CUSTOM, this::setupEvoker);
	}
	
	public CraftTurret(Town town)
	{
		super(town);
	}
	
	private void setupEvoker(Entity evoker)
	{
		setupEvoker((Evoker) evoker);
	}
	
	private void setupEvoker(Evoker evoker)
	{
		evoker.setAI(false);
		evoker.setGravity(false);
		evoker.setInvulnerable(true);
		evoker.setSilent(true);
		evoker.setPersistent(true);
		evoker.setCanJoinRaid(false);
		evoker.setRaid(null);
		evoker.setRemoveWhenFarAway(false);
		evoker.getEquipment().clear();
		
		var container = evoker.getPersistentDataContainer();
		container.set(FAWE_IGNORE, BOOLEAN, true);
	}
	
	private Location getDefaultEvokerLocation()
	{
		Location pillagerLocation = getCenterTownBlock().getBlock()
				.getLocation()
				.toHighestLocation()
				.getBlock()
				.getRelative(BlockFace.UP)
				.getLocation()
				.toCenterLocation();
		
		pillagerLocation.setY(pillagerLocation.getBlockY());
		
		return pillagerLocation;
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.TURRET;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<TurretAttribute> getRule()
	{
		return (Rule<TurretAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
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
				teleportHologram(getLocation()
						.toHighestLocation()
						.getBlock()
						.getRelative(BlockFace.UP)
						.getLocation()
						.toCenterLocation());
				hologram.setSize(1);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			}
			else
			{
				hologram.setSize(3);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(2).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
				teleportHologramToDefaultLocation();
			}
		}
		else
		{
			hologram.setSize(1);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			teleportHologramToDefaultLocation();
		}
	}
	
	@Override
	public void onStartAttack()
	{
		super.onStartAttack();
		
		TownAttack townAttack = town.getCurrentAttack();
		target = townAttack == null ? null : townAttack.getAttacker().getPlayer();
		
		Optional.ofNullable(turretTask).ifPresent(BukkitRunnable::cancel);
		turretTask = new TurretTask();
		turretTask.runTaskTimer(getTown().getPlugin(), 0L, 1L);
	}
	
	@Override
	public void onFinishAttack()
	{
		super.onFinishAttack();
		
		Optional.ofNullable(turretTask).ifPresent(BukkitRunnable::cancel);
		cancelAndClearMissileTasks();
		
		resetEvoker();
	}
	
	@Override
	public void onBuildCorruptedTown()
	{
		super.onBuildCorruptedTown();
		
		resetEvoker();
	}
	
	@Override
	public void onStartMove()
	{
		super.onStartMove();
		
		hideEvoker();
	}
	
	@Override
	public void onFinishMove()
	{
		super.onFinishMove();
		
		resetEvoker();
	}
	
	@Override
	public void onCancelMove()
	{
		super.onCancelMove();
		
		resetEvoker();
	}
	
	private void resetEvoker()
	{
		evoker.setCelebrating(false);
		evoker.teleport(getDefaultEvokerLocation());
		evoker.setInvisible(false);
		evoker.getEquipment().clear();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		Optional.ofNullable(turretTask).ifPresent(BukkitRunnable::cancel);
		
		Evoker copy = (Evoker) evoker.copy(evoker.getLocation());
		
		hideEvoker();
		
		var container = copy.getPersistentDataContainer();
		
		container.set(CommonNamespacedKey.CANCEL_DROP, BOOLEAN, true);
		
		copy.setSilent(false);
		copy.setHealth(0.0D);
	}
	
	private void hideEvoker()
	{
		evoker.setInvisible(true);
		this.evoker.teleport(this.evoker.getLocation().add(0.0D, 1000.0D, 0.0D));
	}
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		TurretAttribute attribute = getRule().getAttributeOrDefault(getBuiltLevel());
		
		attackDamage = attribute.getAttackDamage();
		attackSpeed = attribute.getAttackSpeed();
		missileSpeed = attribute.getMissileSpeed();
		range = attribute.getRange();
	}
	
	private void cancelAndClearMissileTasks()
	{
		missileTasks.forEach(MissileTask::cancel);
		missileTasks.clear();
	}
	
	private void alertTurretRange()
	{
		if(target == null)
		{
			return;
		}
		
		target.playSound(getMissileSpawnLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, Pitch.getPitch(17));
		target.playSound(getMissileSpawnLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, Pitch.getPitch(24));
	}
	
	private class TurretTask extends BukkitRunnable
	{
		private int ticks;
		private int lastShotTick;
		
		private boolean isInRange;
		
		@Override
		public void run()
		{
			ticks++;
			
			if(target == null || target.isDead())
			{
				return;
			}
			
			boolean isInRange = isInRange(target);
			
			if(!this.isInRange && isInRange)
			{
				alertTurretRange();
			}
			
			this.isInRange = isInRange;
			
			evoker.setCelebrating(isInRange);
			
			if(isInRange)
			{
				Location evokerLocation = evoker.getLocation();
				Location targetLocation = target.getLocation();
				
				Vector direction = targetLocation.toVector().subtract(evokerLocation.toVector());
				
				float yaw = (float) Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
				float pitch = (float) Math.toDegrees(Math.atan2(-direction.getY(), direction.length()));
				
				evoker.setRotation(yaw, pitch);
			}
			
			if(isInCooldown() || !isInRange)
			{
				return;
			}
			
			shot();
		}
		
		private void shot()
		{
			lastShotTick = ticks;
			
			MissileTask missileTask = new MissileTask();
			
			missileTask.runTaskTimer(getTown().getPlugin(), 0L, 1L);
			missileTasks.add(missileTask);
			
			missileTask.missileSpawnLocation.getWorld()
					.playSound(missileTask.missileSpawnLocation, ENTITY_SHULKER_SHOOT, 1.0F, F1);
		}
		
		private boolean isInCooldown()
		{
			return lastShotTick != 0 && ticks - lastShotTick < attackSpeed;
		}
		
		private boolean isInRange(Entity target)
		{
			Location targetLocation = target.getLocation();
			Location turretLocation = getLocation();
			
			targetLocation.setY(0.0D);
			turretLocation.setY(0.0D);
			
			return turretLocation.distance(targetLocation) <= range;
		}
	}
	
	public Location getMissileSpawnLocation()
	{
		return evoker.getLocation().add(0.0D, 2.1D, 0.0D);
	}
	
	private List<Block> getCollidingBlocks(Entity entity)
	{
		World world = entity.getWorld();
		BoundingBox boundingBox = entity.getBoundingBox();
		
		int minX = (int) Math.floor(boundingBox.getMinX());
		int maxX = (int) Math.floor(boundingBox.getMaxX());
		int minY = (int) Math.floor(boundingBox.getMinY());
		int maxY = (int) Math.floor(boundingBox.getMaxY());
		int minZ = (int) Math.floor(boundingBox.getMinZ());
		int maxZ = (int) Math.floor(boundingBox.getMaxZ());
		
		List<Block> collidingBlocks = new ArrayList<>();
		
		for(int x = minX; x <= maxX; x++)
		{
			for(int y = minY; y <= maxY; y++)
			{
				for(int z = minZ; z <= maxZ; z++)
				{
					final Block block = world.getBlockAt(x, y, z);
					final TownBlock townBlock = town.getTownBlock(block);
					
					if(block.isEmpty() || !block.getType().isCollidable()
							|| townBlock != null && townBlock.hasStructure())
					{
						continue;
					}
					
					collidingBlocks.add(block);
				}
			}
		}
		
		return collidingBlocks;
	}
	
	private class MissileTask extends BukkitRunnable
	{
		private final Location missileSpawnLocation = getMissileSpawnLocation();
		private final ArmorStand marker = (ArmorStand) missileSpawnLocation
				.getWorld()
				.spawnEntity(missileSpawnLocation, ARMOR_STAND, CUSTOM, this::setupMarker);
		private final ShulkerBullet missile = (ShulkerBullet) missileSpawnLocation
				.getWorld()
				.spawnEntity(missileSpawnLocation, SHULKER_BULLET, CUSTOM, this::setupMissile);
		
		private int ticks;
		
		@Override
		public void run()
		{
			if(marker.isDead() || missile.isDead())
			{
				cancel();
				return;
			}
			
			final Location currentMissileLocation = marker.getLocation();
			final Location targetLoc = target.getEyeLocation();
			boolean isMissileInsideTarget = target.getBoundingBox().contains(currentMissileLocation.toVector());
			
			if(isMissileInsideTarget)
			{
				caught();
				return;
			}
			
			List<Block> collidingBlocks = getCollidingBlocks(missile);
			
			if(!collidingBlocks.isEmpty())
			{
				explode(collidingBlocks);
				return;
			}
			
			final Location subtract = targetLoc.clone().subtract(currentMissileLocation);
			final Vector direction = subtract.toVector().normalize();
			
			direction.multiply(missileSpeed);
			
			double currentDistance = currentMissileLocation.distance(targetLoc);
			final Location nextMissileLocation = currentMissileLocation.clone().add(direction);
			double nextDistance = nextMissileLocation.distance(targetLoc);
			
			if(nextDistance > currentDistance)
			{
				caught();
				return;
			}
			
			marker.teleport(nextMissileLocation, TeleportFlag.EntityState.RETAIN_PASSENGERS);
			ticks++;
		}
		
		@Override
		public synchronized void cancel() throws IllegalStateException
		{
			super.cancel();
			marker.remove();
			missile.remove();
		}
		
		private void cancelAndRemoveFromList()
		{
			cancel();
			missileTasks.remove(this);
		}
		
		private void setupMissile(Entity shulkerBullet)
		{
			setupMissile((ShulkerBullet) shulkerBullet);
		}
		
		private void setupMarker(Entity missile)
		{
			setupMarker((ArmorStand) missile);
		}
		
		private void setupMissile(ShulkerBullet shulkerBullet)
		{
			shulkerBullet.setTarget(null);
			shulkerBullet.setGravity(false);
			shulkerBullet.setPersistent(false);
			
			var container = shulkerBullet.getPersistentDataContainer();
			container.set(PluginNamespacedKey.FAKE_SHULKER_BULLET, BOOLEAN, true);
			container.set(CommonNamespacedKey.REMOVE_ON_CHUNK_LOAD, BOOLEAN, true);
			
			marker.addPassenger(shulkerBullet);
		}
		
		private void setupMarker(ArmorStand marker)
		{
			marker.setGravity(false);
			marker.setPersistent(false);
			marker.setInvulnerable(true);
			marker.setInvisible(true);
			marker.setMarker(true);
			
			var container = marker.getPersistentDataContainer();
			container.set(CommonNamespacedKey.REMOVE_ON_CHUNK_LOAD, BOOLEAN, true);
		}
		
		private void caught()
		{
			cancelAndRemoveFromList();
			
			target.setNoDamageTicks(0);
			target.damage(attackDamage);
			
			displayDetonationEffects();
		}
		
		private void explode(List<Block> collidingBlocks)
		{
			cancelAndRemoveFromList();
			
			TownAttack townAttack = getTown().getCurrentAttack();
			
			if(townAttack != null)
			{
				townAttack.damageBlocks(collidingBlocks, (float) attackDamage);
			}
			
			displayDetonationEffects();
		}
		
		private void displayDetonationEffects()
		{
			playDetonationParticle();
			playDetonationSound();
		}
		
		private void playDetonationParticle()
		{
			missile.getWorld().spawnParticle(Particle.EXPLOSION, missile.getLocation(), 0);
		}
		
		private void playDetonationSound()
		{
			target.getWorld().playSound(missile.getLocation(), ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, F1);
		}
	}
}
