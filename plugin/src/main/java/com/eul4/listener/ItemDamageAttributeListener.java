package com.eul4.listener;

import com.eul4.Main;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

import static com.eul4.util.AttributeModifierUtil.of;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE;
import static org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED;
import static org.bukkit.inventory.EquipmentSlot.HAND;

@RequiredArgsConstructor
public class ItemDamageAttributeListener implements Listener
{
	private static final Map<Material, Multimap<Attribute, AttributeModifier>> TYPE_DAMAGES = new HashMap<>();
	
	static
	{
		TYPE_DAMAGES.put(Material.WOODEN_HOE, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.GOLDEN_HOE, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.STONE_HOE, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.IRON_HOE, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.DIAMOND_HOE, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.NETHERITE_HOE, attackDamage(0.0D));
		
		TYPE_DAMAGES.put(Material.WOODEN_SHOVEL, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.GOLDEN_SHOVEL, attackDamage(0.0D));
		TYPE_DAMAGES.put(Material.STONE_SHOVEL, attackDamage(1.0D));
		TYPE_DAMAGES.put(Material.IRON_SHOVEL, attackDamage(2.0D));
		TYPE_DAMAGES.put(Material.DIAMOND_SHOVEL, attackDamage(3.0D));
		TYPE_DAMAGES.put(Material.NETHERITE_SHOVEL, attackDamage(4.0D));
		
		TYPE_DAMAGES.put(Material.WOODEN_PICKAXE, attackDamage(1.0D));
		TYPE_DAMAGES.put(Material.GOLDEN_PICKAXE, attackDamage(1.0D));
		TYPE_DAMAGES.put(Material.STONE_PICKAXE, attackDamage(2.0D));
		TYPE_DAMAGES.put(Material.IRON_PICKAXE, attackDamage(3.0D));
		TYPE_DAMAGES.put(Material.DIAMOND_PICKAXE, attackDamage(4.0D));
		TYPE_DAMAGES.put(Material.NETHERITE_PICKAXE, attackDamage(5.0D));
		
		TYPE_DAMAGES.put(Material.WOODEN_AXE, attackDamage(2.0D));
		TYPE_DAMAGES.put(Material.GOLDEN_AXE, attackDamage(2.0D));
		TYPE_DAMAGES.put(Material.STONE_AXE, attackDamage(3.0D));
		TYPE_DAMAGES.put(Material.IRON_AXE, attackDamage(4.0D));
		TYPE_DAMAGES.put(Material.DIAMOND_AXE, attackDamage(5.0D));
		TYPE_DAMAGES.put(Material.NETHERITE_AXE, attackDamage(6.0D));
		
		TYPE_DAMAGES.put(Material.WOODEN_SWORD, attackDamage(3.0D));
		TYPE_DAMAGES.put(Material.GOLDEN_SWORD, attackDamage(3.0D));
		TYPE_DAMAGES.put(Material.STONE_SWORD, attackDamage(4.0D));
		TYPE_DAMAGES.put(Material.IRON_SWORD, attackDamage(5.0D));
		TYPE_DAMAGES.put(Material.DIAMOND_SWORD, attackDamage(6.0D));
		TYPE_DAMAGES.put(Material.NETHERITE_SWORD, attackDamage(7.0D));
		
		TYPE_DAMAGES.put(Material.TRIDENT, attackDamage(8.0D));
		TYPE_DAMAGES.put(Material.MACE, attackDamageAndAttackSpeed(5.0D, -3.4D));
	}
	
	private final Main plugin;
	
	private static Multimap<Attribute, AttributeModifier> attackDamageAndAttackSpeed(double attackDamage, double attackSpeed)
	{
		return from
		(
			Map.ofEntries
			(
				Map.entry(GENERIC_ATTACK_DAMAGE, of(attackDamage, HAND)),
				Map.entry(GENERIC_ATTACK_SPEED, of(attackSpeed, HAND))
			)
		);
	}
	
	private static Multimap<Attribute, AttributeModifier> attackDamage(double attackDamage)
	{
		return from(Map.of(GENERIC_ATTACK_DAMAGE, of(attackDamage, HAND)));
	}
	
	private static Multimap<Attribute, AttributeModifier> from(Map<Attribute, AttributeModifier> map)
	{
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		
		for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet())
		{
			multimap.put(entry.getKey(), entry.getValue());
		}
		
		return multimap;
	}
	
	@EventHandler
	public void on(InventoryOpenEvent event)
	{
		fixInventory(event.getInventory());
	}
	
	@EventHandler
	public void on(ItemSpawnEvent event)
	{
		event.getEntity().setItemStack(fixDamage(event.getEntity().getItemStack()));
	}
	
	@EventHandler
	public void on(EntityPickupItemEvent event)
	{
		event.getItem().setItemStack(fixDamage(event.getItem().getItemStack()));
	}
	
	@EventHandler(ignoreCancelled = true)
	public void on(PrepareItemCraftEvent event)
	{
		CraftingInventory craftingInventory = event.getInventory();
		craftingInventory.setResult(fixDamage(craftingInventory.getResult()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		fixInventory(player.getInventory());
	}
	
	private void fixInventory(Inventory inventory)
	{
		ItemStack[] contents = inventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			inventory.setItem(i, fixDamage(contents[i]));
		}
	}
	
	private ItemStack fixDamage(ItemStack itemStack)
	{
		if(itemStack != null && TYPE_DAMAGES.containsKey(itemStack.getType()))
		{
			ItemMeta meta = itemStack.getItemMeta();
			meta.setAttributeModifiers(TYPE_DAMAGES.get(itemStack.getType()));
			itemStack.setItemMeta(meta);
		}
		
		return itemStack;
	}
}
