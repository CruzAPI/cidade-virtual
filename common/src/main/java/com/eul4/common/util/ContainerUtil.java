package com.eul4.common.util;

import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.wrapper.UUIDUtil;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class ContainerUtil
{
	public static void setUUID(PersistentDataContainer container, NamespacedKey namespacedKey, UUID uuid)
	{
		container.set(namespacedKey, PersistentDataType.LONG_ARRAY, UUIDUtil.uuidToLongArray(uuid));
	}
	
	public static boolean hasUUID(PersistentDataContainer container, NamespacedKey namespacedKey)
	{
		return getUUID(container, namespacedKey) != null;
	}
	
	public static Optional<UUID> findUUID(PersistentDataContainer container, NamespacedKey namespacedKey)
	{
		return Optional.ofNullable(getUUID(container, namespacedKey));
	}
	
	public static Optional<UUID> findUUID(ItemStack item)
	{
		return Optional.ofNullable(getUUID(item));
	}
	
	public static UUID getUUID(ItemStack item)
	{
		return getUUID(item, CommonNamespacedKey.ITEM_UUID);
	}
	
	public static UUID getUUID(ItemStack item, NamespacedKey namespacedKey)
	{
		return Optional.ofNullable(item)
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(container -> getUUID(container, namespacedKey))
				.orElse(null);
	}
	
	public static UUID getUUID(PersistentDataContainer container, NamespacedKey namespacedKey)
	{
		long[] bits = container.get(namespacedKey, PersistentDataType.LONG_ARRAY);
		
		if(bits == null || bits.length != 2)
		{
			return null;
		}
		
		return new UUID(bits[0], bits[1]);
	}
	
	public static void setFlag(ItemStack itemStack, NamespacedKey namespacedKey, boolean flag)
	{
		itemStack.getItemMeta().getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, flag);
	}
	
	public static void setFlag(PersistentDataContainer container, NamespacedKey namespacedKey)
	{
		setFlag(container, namespacedKey, true);
	}
	
	public static void setFlag(PersistentDataContainer container, NamespacedKey namespacedKey, boolean flag)
	{
		container.set(namespacedKey, PersistentDataType.BOOLEAN, flag);
	}
	
	public static boolean hasFlag(ItemStack itemStack, NamespacedKey namespacedKey)
	{
		return Optional.ofNullable(itemStack)
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(container -> hasFlag(container, namespacedKey))
				.orElse(false);
	}
	
	public static boolean hasFlag(PersistentDataContainer container, NamespacedKey namespacedKey)
	{
		return container.getOrDefault(namespacedKey, PersistentDataType.BOOLEAN, false);
	}
	
	public static UUID setRandomUUID(PersistentDataContainer container)
	{
		return setRandomUUID(container, CommonNamespacedKey.ITEM_UUID);
	}
	
	public static UUID setRandomUUID(PersistentDataContainer container, NamespacedKey namespacedKey)
	{
		final UUID uuid = UUID.randomUUID();
		setUUID(container, namespacedKey, uuid);
		return uuid;
	}
}
