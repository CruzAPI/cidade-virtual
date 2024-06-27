package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Armory;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.ArmoryAttribute;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;

@Getter
@Setter
public class CraftArmory extends CraftStructure implements Armory
{
	private ItemStack[] storageContents = new ItemStack[9 * 6];
	private ItemStack[] battleInventoryContents = new ItemStack[41];
	
	public CraftArmory(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, false);
	}
	
	public CraftArmory(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	public CraftArmory(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.ARMORY;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<ArmoryAttribute> getRule()
	{
		return (Rule<ArmoryAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	public void updateHologram()
	{
		if(status != StructureStatus.BUILT)
		{
			super.updateHologram();
			return;
		}
	}
	
	@Override
	public Inventory getStorageInventoryClone()
	{
		Inventory inventory = town.getPlugin().getServer().createInventory(null, 9 * 6);
		inventory.setContents(storageContents);
		return inventory;
	}
	
	@Override
	public void setBattleInventory(PlayerInventory playerInventory)
	{
		setBattleInventoryContents(playerInventory.getContents());
	}
}
