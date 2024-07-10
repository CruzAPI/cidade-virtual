package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Armory;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.ArmoryAttribute;
import com.eul4.util.MessageUtil;
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

import static com.eul4.common.constant.CommonNamespacedKey.FAWE_IGNORE;
import static com.eul4.enums.PluginNamespacedKey.FAKE_VILLAGER;
import static org.bukkit.entity.EntityType.VILLAGER;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CUSTOM;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

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
		
		this.npc = (Villager) centerTownBlock.getBlock().getWorld().spawnEntity(getDefaultNpcLocation(), VILLAGER, CUSTOM, this::setupNPC);
	}
	
	public CraftArmory(Town town)
	{
		super(town);
	}
	
	private void setupNPC(Entity villager)
	{
		this.setupNPC((Villager) villager);
	}
	
	private void setupNPC(Villager villager)
	{
		villager.setAI(false);
		villager.setInvulnerable(true);
		villager.setGravity(false);
		villager.setCollidable(false);
		villager.setProfession(Villager.Profession.WEAPONSMITH);
		villager.setSilent(true);
		
		var container = villager.getPersistentDataContainer();
		container.set(FAKE_VILLAGER, BOOLEAN, true);
		container.set(FAWE_IGNORE, BOOLEAN, true);
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
		
		if(town.isUnderAttack())
		{
			if(isDestroyed())
			{
				teleportHologram(getLocation()
						.toHighestLocation()
						.getBlock()
						.getRelative(BlockFace.UP)
						.getLocation()
						.toCenterLocation());
				hologram.setSize(1);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			}
			else
			{
				hologram.setSize(3);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(2).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
				teleportHologramToDefaultLocation();
			}
		}
		else
		{
			hologram.setSize(1);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
			teleportHologramToDefaultLocation();
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
	
	private Location getDefaultNpcLocation()
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
		
		npc.teleport(getDefaultNpcLocation());
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		Villager copy = (Villager) npc.copy(npc.getLocation());
		this.npc.setInvisible(true);
		this.npc.teleport(this.npc.getLocation().add(0.0D, 1000.0D, 0.0D));
		copy.setSilent(false);
		copy.setHealth(0.0D);
	}
	
	@Override
	public void onFinishAttack()
	{
		super.onFinishAttack();
		
		resetNpc();
	}
	
	@Override
	public void onBuildCorruptedTown()
	{
		super.onBuildCorruptedTown();
		
		resetNpc();
	}
	
	private void resetNpc()
	{
		this.npc.setInvisible(false);
		this.npc.teleport(getDefaultNpcLocation());
	}
}
