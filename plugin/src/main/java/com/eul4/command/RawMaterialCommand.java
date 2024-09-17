package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.Messageable;
import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.service.MarketDataManager;
import com.eul4.wrapper.CryptoInfo;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.YOU_DO_NOT_HAVE_PERMISSION;

public class RawMaterialCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "raw-material";
	private static final String PERMISSION = "command." + COMMAND_NAME;
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Main plugin;
	
	private final MarketDataManager marketDataManager;
	
	public RawMaterialCommand(Main plugin)
	{
		this.plugin = plugin;
		this.marketDataManager = plugin.getMarketDataManager();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(!plugin.getPermissionService().hasPermission(commandSender, PERMISSION))
		{
			messageable.sendMessage(YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length == 1)
		{
			Material material = Material.getMaterial(args[0].toUpperCase());
			
			if(material == null)
			{
				commandSender.sendMessage("Material not found.");
				return false;
			}
			
			if(!marketDataManager.getRawMaterialMap().containsKey(material))
			{
				commandSender.sendMessage("RawMaterial not found.");
				return false;
			}
			
			CryptoInfo cryptoInfo = marketDataManager.getRawMaterialMap().get(material).getCryptoInfo();
			
			commandSender.sendMessage("cap: " + cryptoInfo.getMarketCap());
			commandSender.sendMessage("supply: " + cryptoInfo.getCirculatingSupply());
			
			try
			{
				commandSender.sendMessage("price: " + cryptoInfo.calculatePrice());
				return true;
			}
			catch(InvalidCryptoInfoException e)
			{
				return false;
			}
		}
		else if(args.length == 3)
		{
			Material material = Material.getMaterial(args[0].toUpperCase());
			
			if(material == null)
			{
				commandSender.sendMessage("Material not found.");
				return false;
			}
			
			try
			{
				BigDecimal value = new BigDecimal(args[2]);
				
				if(!marketDataManager.getRawMaterialMap().containsKey(material))
				{
					commandSender.sendMessage("RawMaterial not found.");
					return false;
				}
				
				CryptoInfo cryptoInfo = marketDataManager.getRawMaterialMap().get(material).getCryptoInfo();
				
				if(args[1].equalsIgnoreCase("cap"))
				{
					BigDecimal newMarketCap = value.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
							: cryptoInfo.getMarketCap().add(value);
					
					if(newMarketCap.compareTo(BigDecimal.ZERO) < 0)
					{
						commandSender.sendMessage("Negative Exception");
						return false;
					}
					
					cryptoInfo.setMarketCap(newMarketCap);
					commandSender.sendMessage("newMarketCap: " + newMarketCap);
					return true;
				}
				else if(args[1].equalsIgnoreCase("supply"))
				{
					BigDecimal newCirculatingSupply = value.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
							: cryptoInfo.getCirculatingSupply().add(value);
					
					if(newCirculatingSupply.compareTo(BigDecimal.ZERO) < 0)
					{
						commandSender.sendMessage("Negative Exception");
						return false;
					}
					
					cryptoInfo.setCirculatingSupply(newCirculatingSupply);
					commandSender.sendMessage("newCirculatingSupply: " + newCirculatingSupply);
					return true;
				}
				else
				{
					commandSender.sendMessage("Wrong usage.");
					return false;
				}
			}
			catch(NumberFormatException e)
			{
				commandSender.sendMessage("NumberFormatException.");
				return false;
			}
		}
		else
		{
			commandSender.sendMessage("Wrong usage.");
			return false;
		}
	}
}
