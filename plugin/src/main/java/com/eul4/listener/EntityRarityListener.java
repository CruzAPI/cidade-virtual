package com.eul4.listener;

import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.eul4.Main;
import com.eul4.common.util.EntityUtil;
import com.eul4.enums.Rarity;
import com.eul4.event.entity.EntityDamageItemOnHitEvent;
import com.eul4.event.entity.EquipmentHurtEvent;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.Mth;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class EntityRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(SpawnerSpawnEvent event)
	{
		CreatureSpawner creatureSpawner = event.getSpawner();
		
		if(creatureSpawner == null)
		{
			return;
		}
		
		Entity entity = event.getEntity();
		Block block = creatureSpawner.getBlock();
		
		Rarity blockRarity = RarityUtil.getRarity(plugin, block);
		RarityUtil.setRarity(entity, blockRarity);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEquipmentHurtEvent(EquipmentHurtEvent event)
	{
		DamageSource damageSource = event.getDamageSource();
		Rarity damageRarity = RarityUtil.getRarity(plugin, damageSource);
		event.setAmount(event.getAmount() * damageRarity.getArmorDamageMultiplier());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setTridentRarityOnSpawn(EntitySpawnEvent event)
	{
		if(event.getEntity() instanceof Trident trident)
		{
			ItemStack item = trident.getItemStack();
			Rarity rarity = RarityUtil.getRarity(item);
			RarityUtil.setRarity(trident, rarity);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTridentHit(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Trident trident) || !(event.getEntity() instanceof LivingEntity entity))
		{
			return;
		}
		
		Rarity tridentRarity = RarityUtil.getRarity(trident);
		Rarity entityRarity = RarityUtil.getRarity(entity);
		Rarity damageRarity = Rarity.getMinRarity(tridentRarity, entityRarity);
		
		int damage = damageRarity.getItemDamageMultiplier() - 1;
		
		if(damage > 0)
		{
			ItemStack itemStack = trident.getItemStack();
			ItemMeta meta = itemStack.getItemMeta();
			
			if(meta instanceof Damageable damageable && damageable.hasMaxDamage())
			{
				int currentDamage = damageable.hasDamage() ? damageable.getDamage() : 0;
				damageable.setDamage(Mth.clamp(currentDamage + damage, 0, damageable.getMaxDamage()));
			}
			
			itemStack.setItemMeta(meta);
			trident.setItemStack(itemStack);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageItemOnHit(EntityDamageItemOnHitEvent event)
	{
		Entity target = event.getTarget();
		ItemStack item = event.getItem();
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		Rarity targetRarity = RarityUtil.getRarityOrIfPlayer(target, Rarity.MAX_RARITY);
		
		Rarity minRarity = Rarity.getMinRarity(itemRarity, targetRarity);
		event.setAmount(event.getAmount() * minRarity.getItemDamageMultiplier());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void setMobProjectileRarity(ProjectileLaunchEvent event)
	{
		Projectile projectile = event.getEntity();
		ProjectileSource shooter = projectile.getShooter();
		
		if(projectile.getShooter() instanceof Player || !(shooter instanceof Entity entity))
		{
			return;
		}
		
		Rarity shooterRarity = RarityUtil.getRarity(entity);
		RarityUtil.setRarity(projectile, shooterRarity);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void damageToMob(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		DamageSource damageSource = event.getDamageSource();
		
		if(!(entity instanceof LivingEntity) || entity instanceof Player)
		{
			return;
		}
		
		Rarity entityRarity = RarityUtil.getRarity(entity);
		Rarity damageRarity = RarityUtil.getRarity(plugin, damageSource);
		
		damageRarity = Rarity.getMinRarity(entityRarity, damageRarity);
		
		event.setDamage(event.getDamage() * damageRarity.getDamageMultiplier());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChangeSpawnerType(PlayerInteractEvent event)
	{
		Block clickedBlock = event.getClickedBlock();
		Player player = event.getPlayer();
		EquipmentSlot handType = event.getHand();
		
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| clickedBlock == null
				|| handType == null
				|| player.isSneaking()
				|| !(clickedBlock.getState() instanceof CreatureSpawner creatureSpawner))
		{
			return;
		}
		
		ItemStack item = player.getEquipment().getItem(handType);
		EntityType entityType = EntityUtil.getEntityTypeBySpawnerEgg(item.getType());
		
		if(entityType == null)
		{
			return;
		}
		
		Rarity eggRarity = RarityUtil.getRarity(item);
		Rarity spawnerRarity = RarityUtil.getRarity(plugin, clickedBlock);
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(eggRarity != spawnerRarity)
		{
			pluginPlayer.sendMessage(PluginMessage.INCOMPATIBLE_RARITY);
			event.setCancelled(true);
			return;
		}
		
		creatureSpawner.setSpawnedType(entityType);
		
		if(!creatureSpawner.update())
		{
			event.setCancelled(true);
			return;
		}
		
		if(player.getGameMode() != GameMode.CREATIVE)
		{
			item.subtract(1);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void fakeSpawnEgg(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		ItemStack item = event.getItem();
		Block clickedBlock = event.getClickedBlock();
		
		if(!action.isRightClick() || item == null || clickedBlock == null)
		{
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(!(meta instanceof SpawnEggMeta spawnEggMeta))
		{
			return;
		}
		
		Player player = event.getPlayer();
		
		if(clickedBlock.getType().isInteractable() && !player.isSneaking())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		EntityType entityType = EntityUtil.getEntityTypeBySpawnerEgg(item.getType());
		
		Block relative = clickedBlock.getRelative(event.getBlockFace());
		Block spawnBlock = relative.isEmpty() ? relative : relative.getRelative(BlockFace.UP);
		Location spawnLocation = spawnBlock.getLocation().toCenterLocation();
		
		if(player.getGameMode() != GameMode.CREATIVE)
		{
			if(event.getHand() == EquipmentSlot.HAND)
			{
				player.getInventory().setItemInMainHand(item.subtract());
			}
			else
			{
				player.getInventory().setItemInOffHand(item.subtract());
			}
		}
		
		Entity entity = spawnLocation.getWorld().spawnEntity(spawnLocation, entityType, true);
		RarityUtil.setRarity(entity, itemRarity);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(ThrownEggHatchEvent event)
	{
		if(!event.isHatching())
		{
			return;
		}
		
		event.setHatching(false);
		
		Egg egg = event.getEgg();
		Rarity eggRarity = RarityUtil.getRarity(egg);
		
		egg.getWorld().spawnEntity
		(
			egg.getLocation(),
			EntityType.CHICKEN,
			CreatureSpawnEvent.SpawnReason.EGG,
			entity -> setupLittleChicken((Chicken) entity, eggRarity)
		);
	}
	
	private void setupLittleChicken(Chicken chicken, Rarity rarity)
	{
		RarityUtil.setRarity(chicken, rarity);
		chicken.setBaby();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void fakeEggThrow(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		ItemStack item = event.getItem();
		
		if(!action.isRightClick() || item == null || item.getType() != Material.EGG)
		{
			return;
		}
		
		Player player = event.getPlayer();
		Block clickedBlock = event.getClickedBlock();
		
		if(clickedBlock != null && clickedBlock.getType().isInteractable() && !player.isSneaking())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		
		if(event.getHand() == EquipmentSlot.HAND)
		{
			player.getInventory().setItemInMainHand(item.subtract());
		}
		else
		{
			player.getInventory().setItemInOffHand(item.subtract());
		}
		
		player.getWorld().spawnEntity
		(
			player.getEyeLocation(),
			EntityType.EGG,
			CreatureSpawnEvent.SpawnReason.DEFAULT,
			entity -> setupThrownEgg((Egg) entity, player, itemRarity)
		);
	}
	
	private void setupThrownEgg(Egg egg, Player shooter, Rarity rarity)
	{
		RarityUtil.setRarity(egg, rarity);
		Vector direction = shooter.getEyeLocation().getDirection();
		direction.multiply(1.25D);
		egg.setVelocity(direction);
	}
	
	@EventHandler
	public void on(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		
		if(entity instanceof Player)
		{
			return;
		}
		
		Rarity entityRarity = RarityUtil.getRarity(entity);
		
		for(ItemStack drop : event.getDrops())
		{
			RarityUtil.setRarity(drop, entityRarity);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityDropItemEvent event)
	{
		Entity entity = event.getEntity();
		Rarity entityRarity = RarityUtil.getRarity(entity);
		ItemStack itemStack = event.getItemDrop().getItemStack();
		RarityUtil.setRarity(itemStack, entityRarity);
		event.getItemDrop().setItemStack(itemStack);
	}
}
