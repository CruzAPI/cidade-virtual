package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.util.EntityUtil;
import com.eul4.enums.MysthicType;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.event.block.BlockBreakNaturallyEvent;
import com.eul4.i18n.PluginMessage;
import com.eul4.item.ContaintmentPickaxe;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

import static com.eul4.item.ContaintmentPickaxe.isContaintmentPickaxe;

@RequiredArgsConstructor
public class ContaintmentPickaxeListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		
		if(player.getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		ItemStack tool = player.getInventory().getItemInMainHand();
		
		ItemMeta meta = tool.getItemMeta();
		
		if(meta == null || MysthicType.getMysthicType(tool) != MysthicType.CONTAINTMENT_PICKAXE)
		{
			return;
		}
		
		Block block = event.getBlock();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(block.getType() != Material.SPAWNER)
		{
			pluginPlayer.sendMessage(PluginMessage.CONTAINTMENT_PICKAXE_CAN_BREAK_ONLY_SPAWNERS);
			event.setCancelled(true);
			return;
		}
		
		event.setCancelled(true);
		block.breakNaturally(tool, false, true);
		player.damageItemStack(EquipmentSlot.HAND, 10000);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakNaturally(BlockBreakNaturallyEvent event)
	{
		ItemStack tool = event.getTool();
		
		if(tool == null)
		{
			return;
		}
		
		ItemMeta meta = tool.getItemMeta();
		
		if(meta == null || MysthicType.getMysthicType(tool) != MysthicType.CONTAINTMENT_PICKAXE)
		{
			return;
		}
		
		PersistentDataContainer container = meta.getPersistentDataContainer();
		
		float chance = container.getOrDefault(PluginNamespacedKey.CONTAINTMENT_CHANCE, PersistentDataType.FLOAT, 0.0F);
		float random = (float) Math.random();
		
		if(chance > random)
		{
			Block block = event.getBlock();
			
			event.getItems().clear();
			event.getItems().add(ItemStack.of(Material.SPAWNER));
			
			if(block.getState() instanceof CreatureSpawner creatureSpawner)
			{
				Optional.ofNullable(creatureSpawner.getSpawnedType())
						.map(EntityUtil::getSpawnerEggByEntityType)
						.map(ItemStack::of)
						.ifPresent(event.getItems()::add);
			}
			
			event.setDropExperience(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void cancelDamageUsingContaintmentPickaxe(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof LivingEntity damager))
		{
			return;
		}
		
		ItemStack weapon = damager.getEquipment().getItemInMainHand();
		
		if(!ContaintmentPickaxe.isContaintmentPickaxe(weapon))
		{
			return;
		}
		
		Messageable messageable = plugin.getMessageableService().getMessageable(damager);
		messageable.sendMessage(PluginMessage.CONTAINTMENT_PICKAXE_CAN_BREAK_ONLY_SPAWNERS);
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onRepairing(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack firstItem = anvilInventory.getFirstItem();
		ItemStack secondItem = anvilInventory.getSecondItem();
		String renameText = anvilView.getRenameText();
		
		if(firstItem == null || renameText == null || !isContaintmentPickaxe(firstItem))
		{
			return;
		}
		
		if(!isContaintmentPickaxe(secondItem))
		{
			event.setResult(null);
			anvilView.setRepairCost(0);
			return;
		}
		
		ItemMeta firstMeta = firstItem.getItemMeta();
		ItemMeta secondMeta = secondItem == null ? null : secondItem.getItemMeta();
		
		int firstCost = firstMeta instanceof Repairable repairable ? repairable.getRepairCost() : 0;
		int secondCost = secondMeta instanceof Repairable repairable ? repairable.getRepairCost() : 0;
		
		float firstChance = ContaintmentPickaxe.getChance(firstItem);
		float secondChance = ContaintmentPickaxe.getChance(secondItem);
		
		float newChance = 1.0F - (1.0F - firstChance) * (1.0F - secondChance);
		
		anvilView.setRepairCost(firstCost + secondCost + 2);
		event.setResult(ContaintmentPickaxe.createContaintmentPickaxe(newChance));
	}
}
