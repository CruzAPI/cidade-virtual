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
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.Arrays;

import static org.bukkit.entity.EntityType.VILLAGER;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CUSTOM;

@Getter
@Setter
public class CraftArmory extends CraftStructure implements Armory
{
	private ItemStack[] storageContents = new ItemStack[9 * 6];
	private ItemStack[] battleInventoryContents = new ItemStack[41];
	
	private Villager npc;
	
	public CraftArmory(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftArmory(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
		town.setArmory(this);
		
		this.npc = (Villager) centerTownBlock.getBlock().getWorld().spawnEntity(createNpcLocation(), VILLAGER, CUSTOM, this::setupNCP);
	}
	
	public CraftArmory(Town town)
	{
		super(town);
	}
	
	private void setupNCP(Entity villager)
	{
		this.setupNCP((Villager) villager);
	}
	
	private void setupNCP(Villager villager)
	{
		villager.setAI(false);
		villager.setInvulnerable(true);
		villager.setGravity(false);
		villager.setCollidable(false);
		villager.setProfession(Villager.Profession.WEAPONSMITH);
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
		return getStorageInventoryClone(InventoryType.CHEST.defaultTitle());
	}
	
	@Override
	public Inventory getStorageInventoryClone(Component title)
	{
		Inventory inventory = town.getPlugin().getServer().createInventory(null, 9 * 6, title);
		inventory.setContents(storageContents);
		return inventory;
	}
	
	@Override
	public void setBattleInventory(PlayerInventory playerInventory)
	{
		setBattleInventoryContents(playerInventory.getContents());
	}
	
	@Override
	public boolean isEmptyExcludingExtraSlots()
	{
		return isBattleInventoryEmptyExcludingExtraSlots() && isStorageInventoryEmpty();
	}
	
	@Override
	public boolean isEmpty()
	{
		return isBattleInventoryEmpty() && isStorageInventoryEmpty();
	}
	
	@Override
	public boolean isBattleInventoryEmpty()
	{
		return isEmpty(battleInventoryContents);
	}
	
	private boolean isBattleInventoryEmptyExcludingExtraSlots()
	{
		return isEmpty(Arrays.copyOf(battleInventoryContents, 9 * 4));
	}
	
	@Override
	public boolean isStorageInventoryEmpty()
	{
		return isEmpty(storageContents);
	}
	
	private boolean isEmpty(ItemStack[] contents)
	{
		for(ItemStack content : contents)
		{
			if(content != null && !content.isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Villager getNPC()
	{
		return npc;
	}
	
	@Override
	public void setNPC(Villager npc)
	{
		this.npc = npc;
	}
	
	private Location createNpcLocation()
	{
		Location npcLocation = getCenterTownBlock().getBlock()
				.getRelative(BlockFace.UP)
				.getLocation()
				.toCenterLocation();
		
		npcLocation.setY(npcLocation.getBlockY());
		
		return npcLocation;
	}
	
	@Override
	public void onFinishMove()
	{
		super.onFinishMove();
		npc.teleport(createNpcLocation());
	}
}
