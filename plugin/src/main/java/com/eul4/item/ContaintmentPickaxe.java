package com.eul4.item;

import com.eul4.common.wrapper.CommonItemStack;
import com.eul4.enums.MysthicType;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.i18n.PluginRichMessage;
import com.eul4.util.RarityUtil;
import com.google.common.collect.HashMultimap;
import net.kyori.adventure.text.Component;
import net.minecraft.util.Mth;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Optional;

public class ContaintmentPickaxe extends CommonItemStack
{
	public ContaintmentPickaxe()
	{
		this(1.0F);
	}
	
	public ContaintmentPickaxe(float chance)
	{
		super(ItemStack.of(Material.GOLDEN_PICKAXE));
		
		chance = Mth.clamp(chance, 0.0F, 1.0F);
		
		RarityUtil.setRarity(itemStack, Rarity.COMMON, false);
		
		List<Component> lore = PluginRichMessage.CONTAINTMENT_PICKAXE_LORE.translateLines(chance);
		lore.add(Component.empty());
		
		MysthicType.setMysthic
		(
			itemStack,
			MysthicType.CONTAINTMENT_PICKAXE,
			lore
		);
		
		ItemMeta meta = itemStack.getItemMeta();
		
		PersistentDataContainer container = meta.getPersistentDataContainer();
		meta.displayName(PluginMessage.CONTAINTMENT_PICKAXE_DISPLAY_NAME.translate());
		
		container.set(PluginNamespacedKey.CONTAINTMENT_CHANCE, PersistentDataType.FLOAT, chance);
		
		if(meta instanceof Damageable damageable)
		{
			damageable.setMaxDamage(100);
			int damage = (int) Math.ceil(Mth.clamp(100.0F - chance * 100.0F, 0.0F, damageable.getMaxDamage()));
			damageable.setDamage(damage);
		}
		
		meta.addEnchant(Enchantment.INFINITY, 1, false);
		meta.setAttributeModifiers(HashMultimap.create());
		
		itemStack.setItemMeta(meta);
		itemStack.addItemFlags(ItemFlag.values());
	}
	
	public static ItemStack createContaintmentPickaxe()
	{
		return createContaintmentPickaxe(1.0F);
	}
	
	public static boolean isContaintmentPickaxe(ItemStack itemStack)
	{
		return MysthicType.getMysthicType(itemStack) == MysthicType.CONTAINTMENT_PICKAXE;
	}
	
	public static float getChance(ItemStack itemStack)
	{
		return Optional.ofNullable(itemStack)
				.filter(ContaintmentPickaxe::isContaintmentPickaxe)
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(container -> container.get(PluginNamespacedKey.CONTAINTMENT_CHANCE, PersistentDataType.FLOAT))
				.orElse(0.0F);
	}
	
	public static ItemStack createContaintmentPickaxe(float chance)
	{
		return new ContaintmentPickaxe(chance).getItemStack();
	}
	
	public static ContaintmentPickaxe fromItemStack(ItemStack itemStack)
	{
		if(itemStack == null)
		{
			return null;
		}
		
		ItemMeta meta = itemStack.getItemMeta();
		
		if(meta == null)
		{
			return null;
		}
		
		PersistentDataContainer container = meta.getPersistentDataContainer();
		Float chance = container.get(PluginNamespacedKey.CONTAINTMENT_CHANCE, PersistentDataType.FLOAT);
		
		if(chance == null)
		{
			return null;
		}
		
		return new ContaintmentPickaxe(chance);
	}
}
