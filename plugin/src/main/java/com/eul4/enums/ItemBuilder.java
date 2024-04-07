package com.eul4.enums;

import com.eul4.StructureType;
import com.eul4.common.constant.CommonNamespacedKey;
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

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum ItemBuilder
{
	LIKE_GENERATOR((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.EMERALD);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.LIKE_GENERATOR),
	
	DISLIKE_GENERATOR((itemBuilder, commonPlayer) ->
	{
		ItemStack item = new ItemStack(Material.REDSTONE);
		setItemBuilderTag(item, itemBuilder, commonPlayer);
		return item;
	}, StructureType.DISLIKE_GENERATOR),
	;
	
	private final BiFunction<ItemBuilder, CommonPlayer, ItemStack> itemStackFunction;
	private final StructureType structureType;
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
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		var container = meta.getPersistentDataContainer();
		
		container.set(PluginNamespacedKey.ITEM_BUILDER_ORDINAL, PersistentDataType.INTEGER, itemBuilder.ordinal());
		container.set(CommonNamespacedKey.CANCEL_SPAWN, PersistentDataType.BOOLEAN, true);
		container.set(CommonNamespacedKey.CANCEL_MOVE, PersistentDataType.BOOLEAN, true);
		container.set(CommonNamespacedKey.CANCEL_INTERACTION, PersistentDataType.BOOLEAN, true);
		container.set(NamespacedKey.randomKey(), PersistentDataType.BOOLEAN, true);
		
		item.setItemMeta(meta);
	}
}
