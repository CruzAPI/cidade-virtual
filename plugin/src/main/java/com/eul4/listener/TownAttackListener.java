package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.model.craft.town.structure.ResourceStructure;
import com.eul4.model.player.Attacker;
import com.eul4.model.player.Fighter;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.eul4.wrapper.TownAttack;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

//TODO: if server restart dropped items in attack will persist and can be "dupped"
@RequiredArgsConstructor
public class TownAttackListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void on(BlockDropItemEvent event)
	{
		Block block = event.getBlock();
		
		TownBlock townBlock = Town.getStaticTownBlock(block);
		Town town = townBlock == null ? null : townBlock.getTown();
		TownAttack townAttack = town == null ? null : town.getCurrentAttack();
		
		if(townAttack == null)
		{
			return;
		}
		
		townAttack.getTempEntities().addAll(event.getItems());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockDestroyEvent event)
	{
		if(!event.willDrop())
		{
			return;
		}
		
		Block block = event.getBlock();
		
		TownBlock townBlock = Town.getStaticTownBlock(block);
		Town town = townBlock == null ? null : townBlock.getTown();
		TownAttack townAttack = town == null ? null : town.getCurrentAttack();
		
		if(townAttack == null)
		{
			return;
		}
		
		event.setWillDrop(false);
		block.getDrops().forEach(itemStack -> townAttack
				.getTempEntities()
				.add(block.getWorld().dropItemNaturally(block.getLocation().toCenterLocation(), itemStack)));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Fighter fighter))
		{
			return;
		}
		
		TownAttack townAttack = fighter.getTownAttack();
		
		if(townAttack == null)
		{
			return;
		}
		
		townAttack.getTempEntities().add(event.getItemDrop());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerDeathEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Fighter))
		{
			return;
		}
		
		event.getDrops().clear();
		event.setDroppedExp(0);
	}
	
	@EventHandler
	public void onAttackerHitStructure(PlayerInteractEvent event)
	{
		Block clickedBlock = event.getClickedBlock();
		
		if(event.getAction() != Action.LEFT_CLICK_BLOCK
				|| clickedBlock == null
				|| event.getHand() != EquipmentSlot.HAND)
		{
			return;
		}
		
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Attacker attacker))
		{
			return;
		}
		
		TownBlock townBlock = Town.getStaticTownBlock(clickedBlock);
		Town town = townBlock == null ? null : townBlock.getTown();
		TownAttack townAttack = town == null ? null : town.getCurrentAttack();
		Structure structure = townBlock == null ? null : townBlock.getStructure();
		
		if(townAttack == null || structure == null || townAttack.getAttacker() != attacker)
		{
			return;
		}
		
		structure.damage(5.0D, clickedBlock); //TODO calculate damage.
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAttackerStealResources(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		Town town = townBlock == null ? null : townBlock.getTown();
		TownAttack townAttack = town == null ? null : town.getCurrentAttack();
		
		if(townAttack == null)
		{
			return;
		}
		
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Attacker attacker)
				|| attacker != townAttack.getAttacker()
				|| !(townBlock.getStructure() instanceof ResourceStructure resourceStructure))
		{
			return;
		}
		
		event.setCancelled(false);
		event.setDropItems(false);
		resourceStructure.findResource(block).ifPresent(resourceStructure::steal);
	}
}
