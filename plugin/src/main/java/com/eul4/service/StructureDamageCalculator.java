package com.eul4.service;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

@RequiredArgsConstructor
public class StructureDamageCalculator
{
	private static final double DEFAULT_ATTACK_DAMAGE = 1.0D;
	private static final double DEFAULT_ATTACK_SPEED = 4.0D;
	private static final double CRITICAL_MULTIPLIER = 1.5D;
	
	private final Main plugin;
	
	public double calculateDamage(PluginPlayer pluginPlayer)
	{
		ItemStack weapon = pluginPlayer.getPlayer().getInventory().getItemInMainHand();
		
		double attackSpeed = getAttackSpeed(weapon);
		double attackDamage = getAttackDamage(weapon);
		double damageMultiplier = getDamageMultiplier(attackSpeed, pluginPlayer.getAttackCooldown());
		
		double damage = attackDamage;
		
		damage *= damageMultiplier;
		
		if(pluginPlayer.isCritical())
		{
			damage *= CRITICAL_MULTIPLIER;
		}
		
		return damage;
	}
	
	private double getDamageMultiplier(double attackSpeed, int attackCooldownTicks)
	{
		double timeTaken = 20 / attackSpeed;
		double damageMultiplier = 0.2 + Math.pow((attackCooldownTicks + 0.5) / timeTaken, 2) * 0.8;
		
		return Math.min(1.0D, damageMultiplier);
	}
	
	private double getAttackDamage(ItemStack weapon)
	{
		ItemMeta meta = weapon.getItemMeta();
		
		if(meta == null)
		{
			return DEFAULT_ATTACK_DAMAGE;
		}
		
		Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
		
		if(modifiers == null || modifiers.isEmpty())
		{
			return DEFAULT_ATTACK_DAMAGE;
		}
		
		return modifiers.iterator().next().getAmount();
	}
	
	private double getAttackSpeed(ItemStack weapon)
	{
		ItemMeta meta = weapon.getItemMeta();
		
		if(meta == null)
		{
			return DEFAULT_ATTACK_SPEED;
		}
		
		Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED);
		
		if(modifiers == null || modifiers.isEmpty())
		{
			return DEFAULT_ATTACK_SPEED;
		}
		
		return modifiers.iterator().next().getAmount();
	}
}
