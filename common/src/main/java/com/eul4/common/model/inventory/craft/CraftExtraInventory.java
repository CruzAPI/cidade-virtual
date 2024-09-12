package com.eul4.common.model.inventory.craft;

import com.eul4.common.model.inventory.ExtraInventory;
import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

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
		inventory.setItem(0, target.getEquipment().getHelmet());
		inventory.setItem(1, target.getEquipment().getChestplate());
		inventory.setItem(2, target.getEquipment().getHelmet());
		inventory.setItem(3, target.getEquipment().getBoots());
		inventory.setItem(4, target.getEquipment().getItemInOffHand());
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
