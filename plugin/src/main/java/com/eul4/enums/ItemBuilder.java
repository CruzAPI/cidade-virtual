package com.eul4.enums;

import com.eul4.StructureType;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum ItemBuilder
{
	LIKE_GENERATOR((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.LIKE_GENERATOR,
			PluginMessage.STRUCTURE_LIKE_GENERATOR_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_LIKE_GENERATOR_SHOP_LORE),
	
	DISLIKE_GENERATOR((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.DISLIKE_GENERATOR,
			PluginMessage.STRUCTURE_DISLIKE_GENERATOR_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_DISLIKE_GENERATOR_SHOP_LORE),
	
	LIKE_DEPOSIT((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.LIME_CONCRETE);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.LIKE_DEPOSIT,
			PluginMessage.STRUCTURE_LIKE_DEPOSIT_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_LIKE_DEPOSIT_SHOP_LORE),
	
	DISLIKE_DEPOSIT((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.RED_CONCRETE);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.DISLIKE_DEPOSIT,
			PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_SHOP_LORE),
	
	ARMORY((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.IRON_BLOCK);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.ARMORY,
			PluginMessage.STRUCTURE_ARMORY_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_ARMORY_SHOP_LORE),
	
	CANNON((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.DISPENSER);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.CANNON,
			PluginMessage.STRUCTURE_CANNON_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_CANNON_SHOP_LORE),
	
	TURRET((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.SMOOTH_STONE);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.TURRET,
			PluginMessage.STRUCTURE_TURRET_SHOP_PREVIEW_ATTRIBUTES,
			PluginMessage.STRUCTURE_TURRET_SHOP_LORE),
	
	;
	
	private final BiFunction<ItemBuilder, CommonPlayer, ItemStack> itemStackFunction;
	private final StructureType structureType;
	private final Message shopPreviewAttributesMessage;
	private final Message shopLoreMessage;
	
	public ItemStack getItem(CommonPlayer commonPlayer)
	{
		return itemStackFunction.apply(this, commonPlayer);
	}
	
	public static Optional<ItemBuilder> findItemBuilder(ItemStack item)
	{
		if(!item.hasItemMeta())
		{
			return Optional.empty();
		}
		
		ItemMeta meta = item.getItemMeta();
		var container = meta.getPersistentDataContainer();
		
		if(!container.has(PluginNamespacedKey.ITEM_BUILDER_ORDINAL, PersistentDataType.INTEGER))
		{
			return Optional.empty();
		}
		
		final int ordinal = container.get(PluginNamespacedKey.ITEM_BUILDER_ORDINAL, PersistentDataType.INTEGER);
		
		return ordinal < values().length
				? Optional.of(values()[ordinal])
				: Optional.empty();
	}
	
	private static void setItemBuilderTag(ItemStack item, ItemBuilder itemBuilder, CommonPlayer commonPlayer)
	{
		ItemMeta meta = item.getItemMeta();
		
		meta.displayName(PluginMessage.STRUCTURE_CONSTRUCTOR
				.translate(commonPlayer, itemBuilder.getStructureType().getNameMessage()));
		meta.lore(PluginMessage.STRUCTURE_CONSTRUCTOR_LORE.translateLore(commonPlayer));
		meta.addEnchant(Enchantment.UNBREAKING, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		var container = meta.getPersistentDataContainer();
		
		container.set(PluginNamespacedKey.ITEM_BUILDER_ORDINAL, PersistentDataType.INTEGER, itemBuilder.ordinal());
		container.set(CommonNamespacedKey.CANCEL_SPAWN, PersistentDataType.BOOLEAN, true);
		container.set(CommonNamespacedKey.CANCEL_MOVE, PersistentDataType.BOOLEAN, true);
		container.set(CommonNamespacedKey.CANCEL_SWAP, PersistentDataType.BOOLEAN, true);
		container.set(CommonNamespacedKey.CANCEL_INTERACTION, PersistentDataType.BOOLEAN, true);
		container.set(NamespacedKey.randomKey(), PersistentDataType.BOOLEAN, true);
		
		item.setItemMeta(meta);
	}
}
