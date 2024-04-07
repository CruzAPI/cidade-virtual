package com.eul4.service;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.InsufficientBalanceException;
import com.eul4.exception.StructureNotForSaleException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class StructureUpgradeExecutor
{
	private final Main plugin;
	
	public boolean executeUpgrade(CommonPlayer commonPlayer, Structure structure)
	{
		try
		{
			structure.buyUpgrade();
			commonPlayer.sendMessage(PluginMessage.STRUCTURE_UPGRADED_TO_LEVEL, structure.getLevel());
			return true;
		}
		catch(IOException e)
		{
			commonPlayer.sendMessage(PluginMessage.STRUCTURE_SCHEMATIC_NOT_FOUND);
		}
		catch(StructureNotForSaleException e)
		{
			commonPlayer.sendMessage(PluginMessage.STRUCTURE_MAX_UPGRADE_REACHED);
		}
		catch(InsufficientBalanceException e)
		{
			if(e.isMissingLikes())
			{
				commonPlayer.sendMessage(PluginMessage.MISSING_LIKES, e.getLike());
			}
			
			if(e.isMissingDislikes())
			{
				commonPlayer.sendMessage(PluginMessage.MISSING_DISLIKES, e.getDislike());
			}
		}
		catch(CannotConstructException e)
		{
			commonPlayer.sendMessage(PluginMessage.STRUCTURE_CAN_NOT_CONSTRUCT_HERE);
		}
		
		return false;
	}
}
