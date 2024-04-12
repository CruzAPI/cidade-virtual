package com.eul4.service;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.StructureIllegalStatusException;
import com.eul4.exception.UpgradeLockedException;
import com.eul4.exception.UpgradeNotFoundException;
import com.eul4.function.Execution;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.attribute.GenericAttribute;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class StructureUpgradeExecutor
{
	private final Main plugin;
	
	public boolean executeUpgradePursache(CommonPlayer commonPlayer, Structure structure)
	{
		GenericAttribute genericAttribute = structure.getRule().getAttribute(structure.getLevel() + 1);
		
		if(genericAttribute == null)
		{
			commonPlayer.sendMessage(PluginMessage.STRUCTURE_MAX_UPGRADE_REACHED);
			return false;
		}
		
		Price price = genericAttribute.getPrice();
		
		if(price == null)
		{
			commonPlayer.sendMessage(PluginMessage.STRUCTURE_MAX_UPGRADE_REACHED);
			return false;
		}
		
		Execution execution = getExecution(commonPlayer, structure);
		
		Purchase purchase = new Purchase(commonPlayer, structure.getTown(), price, execution)	;
		
		return plugin.getPurchaseExecutor().executePurchase(purchase);
	}
	
	public Execution getExecution(CommonPlayer commonPlayer, Structure structure)
	{
		return () ->
		{
			try
			{
				structure.upgrade();
				commonPlayer.sendMessage(PluginMessage.STRUCTURE_UPGRADED_TO_LEVEL, structure.getLevel());
				return true;
			}
			catch(IOException e)
			{
				commonPlayer.sendMessage(PluginMessage.STRUCTURE_SCHEMATIC_NOT_FOUND);
			}
			catch(CannotConstructException e)
			{
				commonPlayer.sendMessage(PluginMessage.STRUCTURE_CAN_NOT_CONSTRUCT_HERE);
			}
			catch(StructureIllegalStatusException e)
			{
				commonPlayer.sendMessage(PluginMessage.CAN_ONLY_UPGRADE_WHEN_BUILT);
			}
			catch(UpgradeLockedException e)
			{
				commonPlayer.sendMessage(PluginMessage.REQUIRES_TOWN_HALL_LEVEL, e.getUnlocksTownHallLevel());
			}
			catch(UpgradeNotFoundException e)
			{
				commonPlayer.sendMessage(PluginMessage.STRUCTURE_MAX_UPGRADE_REACHED);
			}
			
			return false;
		};
	}
}
