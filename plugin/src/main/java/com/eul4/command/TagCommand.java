package com.eul4.command;

import com.eul4.common.Common;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.wrapper.Tag;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class TagCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "tag";
	public static final Set<String> CLEAR_ALIASES = Set.of("clear", "remover");
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return Collections.emptyList();
		}
		
		final List<String> suggestions = new ArrayList<>();
		final PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 1)
		{
			String subCommandClearTranslated = PluginMessage.COMMAND_TAG_SUB_COMMAND_CLEAR.translatePlain(pluginPlayer);
			
			for(Tag tag : pluginPlayer.getTags())
			{
				String translatedTag = tag.getMessage().translatePlain(pluginPlayer);
				
				if(translatedTag.toLowerCase().startsWith(args[0].toLowerCase()))
				{
					suggestions.add(translatedTag);
				}
			}
			
			if(subCommandClearTranslated.toLowerCase().startsWith(args[0].toLowerCase()))
			{
				suggestions.add(subCommandClearTranslated);
			}
		}
		
		return suggestions;
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		final PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			TreeSet<Tag> tags = pluginPlayer.getTags();
			
			if(tags.isEmpty())
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_YOU_DO_NOT_HAVE_ANY_TAGS);
				return false;
			}
			
			pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_YOUR_TAGS, pluginPlayer.getTags());
			return true;
		}
		else if(args.length == 1 && CLEAR_ALIASES.contains(args[0].toLowerCase()))
		{
			if(!pluginPlayer.hasTag())
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_NO_TAG_TO_CLEAR);
				return false;
			}
			
			pluginPlayer.setTag(null);
			pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_TAG_CLEARED);
			return true;
		}
		else if(args.length == 1)
		{
			final String tagName = args[0];
			final Tag tag = Tag.getTag(tagName);
			
			if(tag == null)
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_TAG_NOT_FOUND, tagName);
				return false;
			}
			
			if(!tag.hasTag(pluginPlayer))
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_YOU_DO_NOT_HAVE_THIS_TAG);
				return false;
			}
			
			pluginPlayer.setTag(tag);
			pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_TAG_SET, tag);
			return true;
		}
		else
		{
			pluginPlayer.sendMessage(PluginMessage.COMMAND_TAG_USAGE, aliases);
			return false;
		}
	}
}
