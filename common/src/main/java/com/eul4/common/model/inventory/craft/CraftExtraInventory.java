package com.eul4.common.model.inventory.craft;

import com.eul4.common.model.inventory.ExtraInventory;
import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

@Getter
public class CraftExtraInventory extends CraftGui implements ExtraInventory
{
	private final Player target;
	
	public CraftExtraInventory(CommonPlayer commonPlayer, Player target)
	{
		super
		(
			commonPlayer,
			commonPlayer.getPlugin().getServer().createInventory(target, InventoryType.HOPPER)
		);
		
		this.target = target;
	}
	
	@Override
	public void updateInventory()
	{
		final ItemStack helmet = Optional.ofNullable(target.getEquipment().getHelmet()).filter(not(ItemStack::isEmpty)).orElse(null);
		final ItemStack chestplate = Optional.ofNullable(target.getEquipment().getChestplate()).filter(not(ItemStack::isEmpty)).orElse(null);
		final ItemStack leggings = Optional.ofNullable(target.getEquipment().getLeggings()).filter(not(ItemStack::isEmpty)).orElse(null);
		final ItemStack boots = Optional.ofNullable(target.getEquipment().getBoots()).filter(not(ItemStack::isEmpty)).orElse(null);
		final ItemStack offHand = Optional.of(target.getEquipment().getItemInOffHand()).filter(not(ItemStack::isEmpty)).orElse(null);
		
		inventory.setItem(0, helmet);
		inventory.setItem(1, chestplate);
		inventory.setItem(2, leggings);
		inventory.setItem(3, boots);
		inventory.setItem(4, offHand);
	}
	
	@Override
	public void applyToPlayer()
	{
		target.getEquipment().setHelmet(inventory.getItem(0));
		target.getEquipment().setChestplate(inventory.getItem(1));
		target.getEquipment().setLeggings(inventory.getItem(2));
		target.getEquipment().setBoots(inventory.getItem(3));
		target.getEquipment().setItemInOffHand(inventory.getItem(4));
	}
	
	@Override
	public void updateTitle()
	{
	
	}
}
