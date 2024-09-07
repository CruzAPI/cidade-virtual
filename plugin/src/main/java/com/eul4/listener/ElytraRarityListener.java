package com.eul4.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.eul4.Main;
import com.eul4.common.wrapper.Pitch;
import com.eul4.enums.Rarity;
import com.eul4.util.AttributeModifierUtil;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class ElytraRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onEquipElytra(PlayerArmorChangeEvent event)
	{
		if(event.getSlotType() != PlayerArmorChangeEvent.SlotType.CHEST)
		{
			return;
		}
		
		Player player = event.getPlayer();
		ItemStack chest = player.getEquipment().getChestplate();
		
		if(chest == null || chest.getType() != Material.ELYTRA)
		{
			return;
		}
		
		adjustElytraGravity(chest, player.isGliding());
	}
	
	@EventHandler
	public void onToggleGlide(EntityToggleGlideEvent event)
	{
		if(!(event.getEntity() instanceof Player player))
		{
			return;
		}
		
		adjustElytraGravity(player.getEquipment().getChestplate(), event.isGliding());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerElytraBoostEvent event)
	{
		event.setCancelled(true);
		
		Player player = event.getPlayer();
		ItemStack elytra = player.getEquipment().getChestplate();

		if(elytra.getType() != Material.ELYTRA)
		{
			return;
		}
		
		ItemStack fireworkItem = event.getItemStack();
		Rarity fireworkRarity = RarityUtil.getRarity(fireworkItem);
		Rarity elytraRarity = RarityUtil.getRarity(elytra);
		Firework firework = event.getFirework();
		
		RarityUtil.setRarity(firework, fireworkRarity);
		
		player.getEquipment().getItem(event.getHand()).subtract(1);
		scheduleFireworkTask(player, event.getFirework());
	}
	
	private void scheduleFireworkTask(Player player, Firework firework)
	{
		Rarity fireworkRarity = RarityUtil.getRarity(firework);
		player.getWorld().playSound(firework.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0F, Pitch.getPitch(16 - fireworkRarity.ordinal() * 3));
		
		new BukkitRunnable()
		{
			int ticks = (int) (firework.getTicksToDetonate() * fireworkRarity.getFireworkElytraDurationMultiplier());
			
			@Override
			public void run()
			{
				if(ticks < 0)
				{
					cancel();
					return;
				}

				player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation(), 0);
				
				if(player.isGliding())
				{
					player.setVelocity
					(
						player.getEyeLocation()
								.getDirection()
								.multiply(fireworkRarity.getFireworkElytraBoostMultiplier())
					);
				}
				
				ticks--;
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public static void adjustElytraGravity(ItemStack elytra, boolean gliding)
	{
		if(elytra == null || elytra.getType() != Material.ELYTRA)
		{
			return;
		}
		
		Rarity rarity = RarityUtil.getRarity(elytra);
		ItemMeta meta = elytra.getItemMeta();
		
		meta.removeAttributeModifier(Attribute.GENERIC_GRAVITY);
		
		if(gliding)
		{
			meta.addAttributeModifier
			(
				Attribute.GENERIC_GRAVITY,
				AttributeModifierUtil.of(rarity.getElytraGravityDebuff(), EquipmentSlot.CHEST)
			);
		}
		
		elytra.setItemMeta(meta);
	}
}
