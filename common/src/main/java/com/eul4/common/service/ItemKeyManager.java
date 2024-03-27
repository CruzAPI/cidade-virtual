package com.eul4.common.service;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

@RequiredArgsConstructor
public class ItemKeyManager
{
	private final Common plugin;
	
//	public static void setMetadata(org.bukkit.inventory.ItemStack item, String metadata, Object value)
//	{
//		setMetadata(CraftItemStack.asNMSCopy(item), metadata, value);
//	}
//
//	public static void setMetadata(ItemStack item, String metadata, Object value)
//	{
//		CompoundTag tag = item.getTag();
//		tag.putUU
//		if(tag == null)
//		{
//			tag = new CompoundTag();
//		}
//
//		setTag(tag, metadata, value);
//		item.setTag(tag);
//	}
	
	
	
	
	
	
	
	
	
	
	//	public boolean equals(ItemStack item1, ItemStack item2)
	//	{
	//		final Optional<String> key1 = findKey(item1);
	//		final Optional<String> key2 = findKey(item2);
	//
	//		return key1.isPresent() && key2.isPresent() && key1.equals(key2);
	//	}
	//
	//	public Optional<String> findKey(ItemStack item)
	//	{
	//		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
	//		{
	//			return Optional.empty();
	//		}
	//
	//		Pattern pattern = Pattern.compile("^((?:§[0-9a-f]){32})");
	//		Matcher matcher = pattern.matcher(item.getItemMeta().getDisplayName());
	//
	//		return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
	//	}
	//
	//	public Key generateRandomKey()
	//	{
	//		return new Key(UUID.randomUUID());
	//	}
	//
	//	private UUID keyToUUID(String key)
	//	{
	//		return UUID.fromString(key.replace(String.valueOf(COLOR_CHAR), "")
	//				.replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5"));
	//	}
	//
	//	private String uuidToKey(UUID uuid)
	//	{
	//		return uuid.toString().replaceAll("-", "").replaceAll("(.)", COLOR_CHAR + "$1");
	//	}
	//
	//	public class Key
	//	{
	//		public final UUID uuid;
	//		public final String key;
	//
	//		private Key(UUID uuid)
	//		{
	//			this.uuid = uuid;
	//			this.key = uuidToKey(uuid);
	//		}
	//
	//		@Override
	//		public String toString()
	//		{
	//			return key;
	//		}
	//	}
}
