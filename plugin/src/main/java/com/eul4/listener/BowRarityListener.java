package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class BowRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(EntityShootBowEvent event)
	{
		ItemStack consumable = event.getConsumable();
		ItemStack bow = event.getBow();
		
		if(consumable == null || bow == null || !(event.getEntity() instanceof Player player))
		{
			return;
		}
		
		Rarity constumableRarity = RarityUtil.getRarity(consumable);
		Rarity bowRarity = RarityUtil.getRarity(bow);
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(constumableRarity.compareTo(bowRarity) > 0)
		{
			pluginPlayer.sendMessage(PluginMessage.ARROW_RARITY_HIGHER_THAN_BOW);
			
			if(!consumable.getItemMeta().hasEnchant(Enchantment.INFINITY))
			{
				player.getInventory().addItem(consumable);
			}
			
			event.setCancelled(true);
			return;
		}
		
		int damage = constumableRarity.getItemDamageMultiplier() - 1;
		
		Entity projectile = event.getProjectile();
		RarityUtil.setRarity(projectile, constumableRarity);
		
		if(damage > 0)
		{
			player.damageItemStack(event.getHand(), damage);
		}
	}
}
