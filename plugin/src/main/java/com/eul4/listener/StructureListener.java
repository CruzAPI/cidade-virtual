package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ContainerUtil;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotBuildYetException;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.StructureAlreadyBuiltException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.IOException;

@RequiredArgsConstructor
public class StructureListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onRightClickStructure(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| !(plugin.getPlayerManager().get(event.getPlayer()) instanceof TownPlayer townPlayer)
				|| event.getHand() != EquipmentSlot.HAND)
		{
			return;
		}
		
		Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		if(ContainerUtil.hasFlag(event.getItem(), PluginNamespacedKey.CANCEL_STRUCTURE_INTERACTION))
		{
			return;
		}
		
		TownBlock townBlock = town.getTownBlock(event.getClickedBlock());
		
		if(townBlock == null || !townBlock.hasStructure())
		{
			return;
		}
		
		Structure structure = townBlock.getStructure();
		
		if(structure.getStatus() == StructureStatus.READY)
		{
			try
			{
				structure.finishBuild();
				townPlayer.sendMessage(PluginMessage.STRUCTURE_BUILD_FINISHED);
			}
			catch(StructureAlreadyBuiltException e)
			{
				townPlayer.sendMessage(PluginMessage.STRUCTURE_ALREADY_BUILT);
			}
			catch(CannotBuildYetException e)
			{
				townPlayer.sendMessage(PluginMessage.STRUCTURE_NOT_READY_YET);
			}
			catch(CannotConstructException e)
			{
				townPlayer.sendMessage(PluginMessage.STRUCTURE_CAN_NOT_CONSTRUCT_HERE);
			}
			catch(IOException e)
			{
				townPlayer.sendMessage(PluginMessage.STRUCTURE_SCHEMATIC_NOT_FOUND);
				throw new RuntimeException(e);
			}
		}
		else if(structure.getStatus() == StructureStatus.BUILT)
		{
			townPlayer.openGui(structure.newGui(townPlayer));
		}
	}
}
