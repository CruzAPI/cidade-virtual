package com.eul4.command;

import com.eul4.Main;
import com.eul4.i18n.PluginMessage;
import com.eul4.i18n.PluginRichMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.wrapper.CooldownMap;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.eul4.i18n.PluginMessage.*;
import static com.eul4.i18n.PluginRichMessage.COMMAND_TOGGLE_COMBAT_NEW;
import static com.eul4.i18n.PluginRichMessage.COMMAND_TOGGLE_COMBAT_OLD;

public class ToggleCombatCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "toggle-combat";
	public static final String BYPASS_PERMISSION = "command." + COMMAND_NAME + ".bypass";
	
	private static final long COOLDOWN_TICKS = 60L * 20L;
	
	private final Main plugin;
	@Getter
	private final CooldownMap cooldownMap;
	
	public ToggleCombatCommand(Main plugin)
	{
		this.plugin = plugin;
		this.cooldownMap = new CooldownMap(plugin);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			if(!pluginPlayer.hasPermission(BYPASS_PERMISSION)
					&& cooldownMap.isInCooldown(pluginPlayer))
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_COOLDOWN_$TICKS, cooldownMap.getRemainingCooldownTicks(pluginPlayer));
				return false;
			}
			
			cooldownMap.putInCooldown(pluginPlayer, COOLDOWN_TICKS);
			
			if(pluginPlayer.hasAttackSpeed())
			{
				pluginPlayer.setAttackSpeed(false);
				pluginPlayer.sendMessage(COMMAND_TOGGLE_COMBAT_OLD);
			}
			else
			{
				pluginPlayer.setAttackSpeed(true);
				pluginPlayer.sendMessage(COMMAND_TOGGLE_COMBAT_NEW);
			}
			
			plugin.getItemDamageAttributeListener().fixInventory(pluginPlayer);
			return true;
		}
		else
		{
			pluginPlayer.sendMessage(COMMAND_TOGGLE_COMBAT_USE_$ALIASES, aliases);
			return true;
		}
	}
}
