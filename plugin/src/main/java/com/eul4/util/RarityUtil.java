package com.eul4.util;

import com.eul4.Main;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.enums.Rarity;
import com.eul4.service.BlockData;
import lombok.experimental.UtilityClass;
import net.minecraft.util.Mth;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.Optional;
import java.util.function.Supplier;

import static com.eul4.enums.PluginNamespacedKey.RARITY;
import static org.bukkit.persistence.PersistentDataType.BYTE;

@UtilityClass
public class RarityUtil
{
	public static ItemStack setRarityIfAbsent(ItemStack item)
	{
		return setRarityIfAbsent(item, Rarity.DEFAULT_RARITY);
	}
	
	public static ItemStack setRarityIfAbsent(ItemStack item, Rarity rarity)
	{
		if(hasRarity(item))
		{
			return item;
		}
		
		setRarity(item, rarity);
		return item;
	}
	
	public static ItemStack setRarity(ItemStack item, Rarity rarity)
	{
		return setRarity(item, rarity, true);
	}
	
	public static ItemStack setRarity(ItemStack item, Rarity rarity, boolean lore)
	{
		if(item == null)
		{
			return null;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return item;
		}
		
		int maxDurability = item.getType().getMaxDurability();
		
		if(meta instanceof Damageable damageable && maxDurability > 0)
		{
			Rarity currentRarity = getRarity(item);
			double multiplier = rarity.getDurabilityMultiplier(item.getType());
			double relativeMultiplier = rarity.getRelativeDurabilityMultiplier(item.getType(), currentRarity);
			
			int damage = damageable.hasDamage() ? damageable.getDamage() : 0;
			
			damageable.setMaxDamage((int) (item.getType().getMaxDurability() * multiplier));
			
			if(damageable.hasDamage())
			{
				damageable.setDamage(Mth.clamp((int) Math.ceil(damage * relativeMultiplier), 0, damageable.getMaxDamage()));
			}
		}
		
		var container = meta.getPersistentDataContainer();
		container.set(RARITY, BYTE, rarity.getId());
		
		if(lore)
		{
			meta.lore(rarity.getStylizedMessage().translateLines(ResourceBundleHandler.DEFAULT_LOCALE));
		}
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static Rarity getRarity(Main plugin, Block block)
	{
		return Optional.of(plugin.getBlockDataFiler())
				.map(blockDataFiler -> blockDataFiler.loadBlockData(block))
				.map(BlockData::getRarity)
				.orElse(Rarity.DEFAULT_RARITY);
	}
	
	public static Optional<Rarity> findRarity(ItemStack item)
	{
		return Optional.ofNullable(item)
				.map(ItemStack::getItemMeta)
				.flatMap(RarityUtil::findRarityId)
				.map(Rarity::getRarityById);
	}
	
	public static Optional<Byte> findRarityId(PersistentDataHolder holder)
	{
		return Optional.ofNullable(holder.getPersistentDataContainer().get(RARITY, BYTE));
	}
	
	public static boolean hasRarity(PersistentDataHolder persistentDataHolder)
	{
		return findRarity(persistentDataHolder).isPresent();
	}
	
	public static boolean hasRarity(ItemStack item)
	{
		return findRarity(item).isPresent();
	}
	
	public static Rarity getRarityOrNull(ItemStack item)
	{
		return findRarity(item).orElse(null);
	}
	
	public static Rarity getRarity(ItemStack item)
	{
		return findRarity(item).orElse(Rarity.DEFAULT_RARITY);
	}
	
	public static Rarity getRarity(Entity entity)
	{
		return getRarityOrIfPlayer(entity, Rarity.DEFAULT_RARITY);
	}
	
	public static Rarity getRarityOrIfPlayer(Entity entity, Rarity rarityIfPlayer)
	{
		return entity instanceof Player ? rarityIfPlayer : getRarity(entity.getPersistentDataContainer());
	}
	
	public static void setRarity(Entity entity, Rarity rarity)
	{
		if(entity instanceof LivingEntity livingEntity)
		{
			Optional.ofNullable(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH))
					.ifPresent(attributeInstance ->
					{
						attributeInstance.setBaseValue
						(
							attributeInstance.getBaseValue() * rarity.getMobMaxHealthMultiplier()
						);
						
						livingEntity.setHealth(attributeInstance.getValue());
					});
		}
		
		entity.getPersistentDataContainer().set(RARITY, BYTE, rarity.getId());
	}
	
	private static Rarity getRarity(PersistentDataContainer container)
	{
		return Rarity.getRarityById(getRarityId(container));
	}
	
	public static Rarity getRarity(PersistentDataHolder persistentDataHolder)
	{
		return Rarity.getRarityById(getRarityId(persistentDataHolder.getPersistentDataContainer()));
	}
	
	public static Rarity getOrSetRarity(PersistentDataHolder persistentDataHolder, Supplier<Rarity> raritySupplier)
	{
		if(hasRarity(persistentDataHolder))
		{
			return getRarity(persistentDataHolder);
		}
		
		Rarity rarity = raritySupplier.get();
		setRarity(persistentDataHolder, rarity);
		return rarity;
	}
	
	public static Optional<Rarity> findRarity(PersistentDataHolder persistentDataHolder)
	{
		return Optional
				.ofNullable(persistentDataHolder.getPersistentDataContainer().get(RARITY, BYTE))
				.map(Rarity::getRarityById);
	}
	
	public static void setRarity(PersistentDataHolder persistentDataHolder, Rarity rarity)
	{
		persistentDataHolder.getPersistentDataContainer().set(RARITY, BYTE, rarity.getId());
	}
	
	private static byte getRarityId(PersistentDataContainer container)
	{
		return container.getOrDefault(RARITY, BYTE, (byte) 0);
	}
	
	public static Rarity getMinRarity(ItemStack[] itemStacks)
	{
		return getMinRarity(Rarity.MAX_RARITY, itemStacks);
	}
	
	public static Rarity getMinRarity(Rarity minRarity, ItemStack[] itemStacks)
	{
		for(ItemStack itemStack : itemStacks)
		{
			if(itemStack == null)
			{
				continue;
			}
			
			Rarity rarity = RarityUtil.getRarity(itemStack);
			
			if(rarity.compareTo(minRarity) < 0)
			{
				minRarity = rarity;
			}
		}
		
		return minRarity;
	}
	
	public static Rarity getRarity(Main plugin, DamageSource damageSource)
	{
		ItemStack weaponItem = damageSource.getWeaponItem();
		
		if(ItemStackUtil.isTool(weaponItem))
		{
			return getRarity(weaponItem);
		}
		
		Entity directEntity = damageSource.getDirectEntity();
		
		if(directEntity != null)
		{
			return getRarity(directEntity);
		}
		
		Block directBlock = damageSource.getDirectBlock();
		
		if(directBlock != null)
		{
			return getRarity(plugin, directBlock);
		}
		
		Location location = damageSource.getDamageLocation();
		
		if(location != null)
		{
			return getRarity(plugin, location.getBlock());
		}
		
		return Rarity.DEFAULT_RARITY;
	}
}
