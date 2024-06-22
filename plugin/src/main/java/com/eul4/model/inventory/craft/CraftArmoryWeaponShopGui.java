package com.eul4.model.inventory.craft;

import com.eul4.Price;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ArmoryWeaponShopGui;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.AttributeModifierUtil;
import com.eul4.util.MessageUtil;
import com.eul4.wrapper.Cost;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.bukkit.Material.LEATHER;
import static org.bukkit.Material.LEATHER_HELMET;

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
	
	public enum ItemAttribute
	{
		LEATHER_HELMET(100, () ->
		{
			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(1, EquipmentSlot.HEAD));
			return attributeModifiers;
		}),
		
//		LEATHER_HELMET(100, () ->
//		{
//			Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
//			attributeModifiers.put(Attribute.GENERIC_ARMOR, AttributeModifierUtil.of(1, EquipmentSlot.HEAD));
//			return attributeModifiers;
//		}),
		
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
	public enum Icon
	{
		LEATHER_HELMET(ItemAttribute.LEATHER_HELMET,
				Material.LEATHER_HELMET,
				0,
				new Cost(Price.builder().dislikes(100).build(), ofEntries(entry(LEATHER, 20 * 5)))),
		
		
		;
		
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
			
			icon.addItemFlags(ItemFlag.values());
			icon.setItemMeta(meta);
			
			return icon;
		}
		
		public int getUnlockLevel()
		{
			return slot / 9 + 1;
		}
	}
	
	public CraftArmoryWeaponShopGui(PluginPlayer pluginPlayer)
	{
		super(pluginPlayer, pluginPlayer.createInventory(9 * 6, PluginMessage.INVENTORY_ARMORY_WEAPON_SHOP_TITLE));
		
		for(Icon icon : Icon.values())
		{
			inventory.setItem(icon.slot, icon.getIcon(pluginPlayer));
		}
	}
	
	@Override
	public void updateTitle()
	{
	
	}
}
