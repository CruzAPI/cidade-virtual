package com.eul4.model.inventory.craft;

import com.eul4.Price;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ArmoryWeaponShopGui;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.structure.Armory;
import com.eul4.util.AttributeModifierUtil;
import com.eul4.util.MessageUtil;
import com.eul4.wrapper.Cost;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class CraftArmoryWeaponShopGui extends CraftGui implements ArmoryWeaponShopGui
{
	private static final ItemStack GLASS;
	
	static
	{
		ItemMeta meta;
		
		GLASS = new ItemStack(Material.GLASS_PANE);
		meta = GLASS.getItemMeta();
		meta.displayName(Component.empty());
		GLASS.setItemMeta(meta);
	}
	
	@Getter
	private final Armory armory;
	
	public enum ItemAttribute
	{
		LEATHER_HELMET(100, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(1, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
		LEATHER_CHESTPLATE(160, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.CHEST));
			return attributeModifiers;
		}),
		
		LEATHER_LEGGINGS(150, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(2, EquipmentSlot.LEGS));
			return attributeModifiers;
		}),
		
		LEATHER_BOOTS(65, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(1, EquipmentSlot.FEET));
			return attributeModifiers;
		}),
		
		WOODEN_SWORD(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(4, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		WOODEN_AXE(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(7, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		WOODEN_PICKAXE(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(2, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		WOODEN_SHOVEL(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(2.5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		CHAINMAIL_HELMET(150, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(2, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
		CHAINMAIL_CHESTPLATE(220, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(5, EquipmentSlot.CHEST));
			return attributeModifiers;
		}),
		
		CHAINMAIL_LEGGINGS(200, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(4, EquipmentSlot.LEGS));
			return attributeModifiers;
		}),
		
		CHAINMAIL_BOOTS(80, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(1, EquipmentSlot.FEET));
			return attributeModifiers;
		}),
		
		STONE_SWORD(131, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		STONE_AXE(131, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(9, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		STONE_PICKAXE(131, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(3, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		STONE_SHOVEL(131, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(3.5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		IRON_HELMET(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
		IRON_CHESTPLATE(400, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(6, EquipmentSlot.CHEST));
			return attributeModifiers;
		}),
		
		IRON_LEGGINGS(300, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(5, EquipmentSlot.LEGS));
			return attributeModifiers;
		}),
		
		IRON_BOOTS(150, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(2, EquipmentSlot.FEET));
			return attributeModifiers;
		}),
		
		IRON_SWORD(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(6, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		IRON_AXE(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(9, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		IRON_PICKAXE(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(4, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		IRON_SHOVEL(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(4.5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		GOLD_HELMET(100, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(2, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
		GOLD_CHESTPLATE(160, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(5, EquipmentSlot.CHEST));
			return attributeModifiers;
		}),
		
		GOLD_LEGGINGS(150, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.LEGS));
			return attributeModifiers;
		}),
		
		GOLD_BOOTS(65, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(1, EquipmentSlot.FEET));
			return attributeModifiers;
		}),
		
		GOLD_SWORD(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(4, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		GOLD_AXE(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(7, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		GOLD_PICKAXE(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(2, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		GOLD_SHOVEL(59, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(2.5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		DIAMOND_HELMET(363, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
		DIAMOND_CHESTPLATE(528, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(8, EquipmentSlot.CHEST));
			return attributeModifiers;
		}),
		
		DIAMOND_LEGGINGS(495, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(6, EquipmentSlot.LEGS));
			return attributeModifiers;
		}),
		
		DIAMOND_BOOTS(195, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.FEET));
			return attributeModifiers;
		}),
		
		DIAMOND_SWORD(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(7, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		DIAMOND_AXE(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(9, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		DIAMOND_PICKAXE(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		DIAMOND_SHOVEL(250, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(5.5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		NETHERITE_HELMET(407, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
		NETHERITE_CHESTPLATE(592, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(8, EquipmentSlot.CHEST));
			return attributeModifiers;
		}),
		
		NETHERITE_LEGGINGS(555, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(6, EquipmentSlot.LEGS));
			return attributeModifiers;
		}),
		
		NETHERITE_BOOTS(222, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(3, EquipmentSlot.FEET));
			return attributeModifiers;
		}),
		
		NETHERITE_SWORD(290, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(8, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		NETHERITE_AXE(290, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(10, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		NETHERITE_PICKAXE(290, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(6, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		NETHERITE_SHOVEL(290, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifierUtil.of(6.5, EquipmentSlot.HAND));
			return attributeModifiers;
		}),
		
		;
		
		private final int durability;
		private final Multimap<Attribute, AttributeModifier> attributeModifiers;
		
		ItemAttribute(int durability, Supplier<Multimap<Attribute, AttributeModifier>> attributeModifiers)
		{
			this.durability = durability;
			this.attributeModifiers = attributeModifiers.get();
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	public enum Icon
	{
		LEATHER_HELMET(ItemAttribute.LEATHER_HELMET,
				Material.LEATHER_HELMET,
				0,
				new Cost(Price.builder().dislikes(100).build(), ofEntries(entry(Material.LEATHER, 20 * 5)))),
		
		LEATHER_CHESTPLATE(ItemAttribute.LEATHER_CHESTPLATE,
				Material.LEATHER_CHESTPLATE,
				1,
				new Cost(Price.builder().dislikes(160).build(), ofEntries(entry(Material.LEATHER, 20 * 8)))),
		
		LEATHER_LEGGINGS(ItemAttribute.LEATHER_LEGGINGS,
				Material.LEATHER_LEGGINGS,
				2,
				new Cost(Price.builder().dislikes(150).build(), ofEntries(entry(Material.LEATHER, 20 * 7)))),
		
		LEATHER_BOOTS(ItemAttribute.LEATHER_BOOTS,
				Material.LEATHER_BOOTS,
				3,
				new Cost(Price.builder().dislikes(65).build(), ofEntries(entry(Material.LEATHER, 20 * 4)))),
		
		WOODEN_SWORD(ItemAttribute.WOODEN_SWORD,
				Material.WOODEN_SWORD,
				5,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.OAK_LOG, 20 * 2)))),
		
		WOODEN_AXE(ItemAttribute.WOODEN_AXE,
				Material.WOODEN_AXE,
				6,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.OAK_LOG, 20 * 3)))),
		
		WOODEN_PICKAXE(ItemAttribute.WOODEN_PICKAXE,
				Material.WOODEN_PICKAXE,
				7,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.OAK_LOG, 20 * 3)))),
		
		WOODEN_SHOVEL(ItemAttribute.WOODEN_SHOVEL,
				Material.WOODEN_SHOVEL,
				8,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.OAK_LOG, 20 * 1)))),
		
		CHAINMAIL_HELMET(ItemAttribute.CHAINMAIL_HELMET,
				Material.CHAINMAIL_HELMET,
				9,
				new Cost(Price.builder().dislikes(150).build(), ofEntries(entry(Material.STRING, 20 * 5)))),
		
		CHAINMAIL_CHESTPLATE(ItemAttribute.CHAINMAIL_CHESTPLATE,
				Material.CHAINMAIL_CHESTPLATE,
				10,
				new Cost(Price.builder().dislikes(220).build(), ofEntries(entry(Material.STRING, 20 * 8)))),
		
		CHAINMAIL_LEGGINGS(ItemAttribute.CHAINMAIL_LEGGINGS,
				Material.CHAINMAIL_LEGGINGS,
				11,
				new Cost(Price.builder().dislikes(200).build(), ofEntries(entry(Material.STRING, 20 * 7)))),
		
		CHAINMAIL_BOOTS(ItemAttribute.CHAINMAIL_BOOTS,
				Material.CHAINMAIL_BOOTS,
				12,
				new Cost(Price.builder().dislikes(80).build(), ofEntries(entry(Material.STRING, 20 * 4)))),
		
		STONE_SWORD(ItemAttribute.STONE_SWORD,
				Material.STONE_SWORD,
				14,
				new Cost(Price.builder().dislikes(131).build(), ofEntries(entry(Material.STONE, 20 * 2)))),
		
		STONE_AXE(ItemAttribute.STONE_AXE,
				Material.STONE_AXE,
				15,
				new Cost(Price.builder().dislikes(131).build(), ofEntries(entry(Material.STONE, 20 * 3)))),
		
		STONE_PICKAXE(ItemAttribute.STONE_PICKAXE,
				Material.STONE_PICKAXE,
				16,
				new Cost(Price.builder().dislikes(131).build(), ofEntries(entry(Material.STONE, 20 * 3)))),
		
		STONE_SHOVEL(ItemAttribute.STONE_SHOVEL,
				Material.STONE_SHOVEL,
				17,
				new Cost(Price.builder().dislikes(131).build(), ofEntries(entry(Material.STONE, 20 * 1)))),
		
		IRON_HELMET(ItemAttribute.IRON_HELMET,
				Material.IRON_HELMET,
				18,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 5)))),
		
		IRON_CHESTPLATE(ItemAttribute.IRON_CHESTPLATE,
				Material.IRON_CHESTPLATE,
				19,
				new Cost(Price.builder().dislikes(400).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 8)))),
		
		IRON_LEGGINGS(ItemAttribute.IRON_LEGGINGS,
				Material.IRON_LEGGINGS,
				20,
				new Cost(Price.builder().dislikes(300).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 7)))),
		
		IRON_BOOTS(ItemAttribute.IRON_BOOTS,
				Material.IRON_BOOTS,
				21,
				new Cost(Price.builder().dislikes(150).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 4)))),
		
		IRON_SWORD(ItemAttribute.IRON_SWORD,
				Material.IRON_SWORD,
				23,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 2)))),
		
		IRON_AXE(ItemAttribute.IRON_AXE,
				Material.IRON_AXE,
				24,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 3)))),
		
		IRON_PICKAXE(ItemAttribute.IRON_PICKAXE,
				Material.IRON_PICKAXE,
				25,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 3)))),
		
		IRON_SHOVEL(ItemAttribute.IRON_SHOVEL,
				Material.IRON_SHOVEL,
				26,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.IRON_INGOT, 20 * 1)))),
		
		GOLD_HELMET(ItemAttribute.GOLD_HELMET,
				Material.GOLDEN_HELMET,
				27,
				new Cost(Price.builder().dislikes(100).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 5)))),
		
		GOLD_CHESTPLATE(ItemAttribute.GOLD_CHESTPLATE,
				Material.GOLDEN_CHESTPLATE,
				28,
				new Cost(Price.builder().dislikes(160).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 8)))),
		
		GOLD_LEGGINGS(ItemAttribute.GOLD_LEGGINGS,
				Material.GOLDEN_LEGGINGS,
				29,
				new Cost(Price.builder().dislikes(150).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 7)))),
		
		GOLD_BOOTS(ItemAttribute.GOLD_BOOTS,
				Material.GOLDEN_BOOTS,
				30,
				new Cost(Price.builder().dislikes(65).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 4)))),
		
		GOLD_SWORD(ItemAttribute.GOLD_SWORD,
				Material.GOLDEN_SWORD,
				32,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 2)))),
		
		GOLD_AXE(ItemAttribute.GOLD_AXE,
				Material.GOLDEN_AXE,
				33,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 3)))),
		
		GOLD_PICKAXE(ItemAttribute.GOLD_PICKAXE,
				Material.GOLDEN_PICKAXE,
				34,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 3)))),
		
		GOLD_SHOVEL(ItemAttribute.GOLD_SHOVEL,
				Material.GOLDEN_SHOVEL,
				35,
				new Cost(Price.builder().dislikes(59).build(), ofEntries(entry(Material.GOLD_INGOT, 20 * 1)))),
		
		DIAMOND_HELMET(ItemAttribute.DIAMOND_HELMET,
				Material.DIAMOND_HELMET,
				36,
				new Cost(Price.builder().dislikes(363).build(), ofEntries(entry(Material.DIAMOND, 20 * 5)))),
		
		DIAMOND_CHESTPLATE(ItemAttribute.DIAMOND_CHESTPLATE,
				Material.DIAMOND_CHESTPLATE,
				37,
				new Cost(Price.builder().dislikes(528).build(), ofEntries(entry(Material.DIAMOND, 20 * 8)))),
		
		DIAMOND_LEGGINGS(ItemAttribute.DIAMOND_LEGGINGS,
				Material.DIAMOND_LEGGINGS,
				38,
				new Cost(Price.builder().dislikes(495).build(), ofEntries(entry(Material.DIAMOND, 20 * 7)))),
		
		DIAMOND_BOOTS(ItemAttribute.DIAMOND_BOOTS,
				Material.DIAMOND_BOOTS,
				39,
				new Cost(Price.builder().dislikes(195).build(), ofEntries(entry(Material.DIAMOND, 20 * 4)))),
		
		DIAMOND_SWORD(ItemAttribute.DIAMOND_SWORD,
				Material.DIAMOND_SWORD,
				41,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.DIAMOND, 20 * 2)))),
		
		DIAMOND_AXE(ItemAttribute.DIAMOND_AXE,
				Material.DIAMOND_AXE,
				42,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.DIAMOND, 20 * 3)))),
		
		DIAMOND_PICKAXE(ItemAttribute.DIAMOND_PICKAXE,
				Material.DIAMOND_PICKAXE,
				43,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.DIAMOND, 20 * 3)))),
		
		DIAMOND_SHOVEL(ItemAttribute.DIAMOND_SHOVEL,
				Material.DIAMOND_SHOVEL,
				44,
				new Cost(Price.builder().dislikes(250).build(), ofEntries(entry(Material.DIAMOND, 20 * 1)))),
		
		NETHERITE_HELMET(ItemAttribute.NETHERITE_HELMET,
				Material.NETHERITE_HELMET,
				45,
				new Cost(Price.builder().dislikes(407).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 5)))),
		
		NETHERITE_CHESTPLATE(ItemAttribute.NETHERITE_CHESTPLATE,
				Material.NETHERITE_CHESTPLATE,
				46,
				new Cost(Price.builder().dislikes(592).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 8)))),
		
		NETHERITE_LEGGINGS(ItemAttribute.NETHERITE_LEGGINGS,
				Material.NETHERITE_LEGGINGS,
				47,
				new Cost(Price.builder().dislikes(555).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 7)))),
		
		NETHERITE_BOOTS(ItemAttribute.NETHERITE_BOOTS,
				Material.NETHERITE_BOOTS,
				48,
				new Cost(Price.builder().dislikes(222).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 4)))),
		
		NETHERITE_SWORD(ItemAttribute.NETHERITE_SWORD,
				Material.NETHERITE_SWORD,
				50,
				new Cost(Price.builder().dislikes(290).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 2)))),
		
		NETHERITE_AXE(ItemAttribute.NETHERITE_AXE,
				Material.NETHERITE_AXE,
				51,
				new Cost(Price.builder().dislikes(290).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 3)))),
		
		NETHERITE_PICKAXE(ItemAttribute.NETHERITE_PICKAXE,
				Material.NETHERITE_PICKAXE,
				52,
				new Cost(Price.builder().dislikes(290).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 3)))),
		
		NETHERITE_SHOVEL(ItemAttribute.NETHERITE_SHOVEL,
				Material.NETHERITE_SHOVEL,
				53,
				new Cost(Price.builder().dislikes(290).build(), ofEntries(entry(Material.NETHERITE_INGOT, 20 * 1)))),
		
		;
		
		private static final Map<Integer, Icon> ICONS_BY_SLOT = new HashMap<>();
		
		static
		{
			for(Icon icon : values())
			{
				ICONS_BY_SLOT.put(icon.slot, icon);
			}
		}
		
		private final ItemAttribute itemAttribute;
		private final Material type;
		private final int slot;
		private final Cost cost;
		
		public ItemStack getIcon(PluginPlayer pluginPlayer)
		{
			ItemStack icon = new ItemStack(type);
			ItemMeta meta = icon.getItemMeta();
			meta.displayName(Component.translatable(type.translationKey())
					.color(NamedTextColor.GREEN)
					.decoration(TextDecoration.ITALIC, false));
			
			List<Component> lore = new ArrayList<>(10);
			
			lore.addAll(MessageUtil.getCostLore(cost, pluginPlayer));
			lore.add(Component.empty());
			lore.addAll(MessageUtil.getAttributesLore(itemAttribute.attributeModifiers, pluginPlayer));
			lore.add(Component.empty());
			lore.addAll(MessageUtil.getDurabilityLore(itemAttribute.durability, pluginPlayer));
			meta.lore(lore);
			
			meta.setAttributeModifiers(null);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			icon.setItemMeta(meta);
			
			return icon;
		}
		
		public ItemStack getWeapon()
		{
			ItemStack weapon = new ItemStack(type);
			ItemMeta meta = weapon.getItemMeta();
			
			meta.setAttributeModifiers(itemAttribute.attributeModifiers);
			
			var container = meta.getPersistentDataContainer();
			container.set(PluginNamespacedKey.WEAPON_ITEM_ATTRIBUTE, PersistentDataType.STRING, itemAttribute.name());
			
			if(meta instanceof Damageable damageable)
			{
				damageable.setMaxDamage(itemAttribute.durability);
				damageable.setDamage(0);
			}
			
			weapon.setItemMeta(meta);
			
			return weapon;
		}
		
		public boolean isUnlocked(Armory armory)
		{
			return armory.getBuiltLevel() >= getUnlockLevel();
		}
		
		public int getUnlockLevel()
		{
			return slot / 9 + 1;
		}
	}
	
	public CraftArmoryWeaponShopGui(PluginPlayer pluginPlayer, Armory armory)
	{
		super(pluginPlayer, pluginPlayer.createInventory(9 * 6, PluginMessage.INVENTORY_ARMORY_WEAPON_SHOP_TITLE));
		
		this.armory = armory;
		
		for(Icon icon : Icon.values())
		{
			inventory.setItem(icon.slot, icon.isUnlocked(armory)
					? icon.getIcon(pluginPlayer)
					: getIconUnlocksAtLevel(icon.getUnlockLevel()));
		}
		
		int firstEmpty;
		
		while((firstEmpty = inventory.firstEmpty()) != -1)
		{
			inventory.setItem(firstEmpty, GLASS);
		}
	}
	
	private ItemStack getIconUnlocksAtLevel(int level)
	{
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.displayName(PluginMessage.UNLOCKS_AT_LEVEL.translateOne(commonPlayer, level));
		item.setItemMeta(meta);
		
		return item;
	}
	
	@Override
	public void updateTitle()
	{
	
	}
	
	@Override
	public boolean isLocked(Icon icon)
	{
		return !icon.isUnlocked(armory);
	}
	
	@Override
	public Icon getBySlot(int slot)
	{
		return Icon.ICONS_BY_SLOT.get(slot);
	}
}
