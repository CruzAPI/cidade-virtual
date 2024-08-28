package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.exception.CommonException;
import com.eul4.common.exception.CommonRuntimeException;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.permission.*;
import com.eul4.common.service.PermissionService;
import com.eul4.common.util.IntegerUtil;
import com.eul4.common.util.LongUtil;
import com.eul4.common.wrapper.GroupGroupPage;
import com.eul4.common.wrapper.GroupPermPage;
import com.eul4.common.wrapper.GroupUserPage;
import com.eul4.common.wrapper.UserPermPage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

import static com.eul4.common.i18n.CommonMessage.*;
import static com.eul4.common.i18n.CommonRichMessage.*;

public class PexCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "pex";
	public static final String PERMISSION = "command." + COMMAND_NAME;
	
	private final Common plugin;
	
	private final PermissionService permissionService;
	
	public PexCommand(Common plugin)
	{
		this.plugin = plugin;
		
		this.permissionService = plugin.getPermissionService();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!permissionService.hasPermission(commandSender, PERMISSION))
		{
			return Collections.emptyList();
		}
		
		List<String> suggestions = new ArrayList<>();
		
		GroupMap groupMap = permissionService.getGroupMap();
		Collection<Group> groups = groupMap.getGroups().values();
		User user;
		Group groupArg;
		
		if(args.length == 1)
		{
			for(String subCommand : new String[] { "group", "user" })
			{
				if(subCommand.startsWith(args[0].toLowerCase()))
				{
					suggestions.add(subCommand);
				}
			}
		}
		else if(args.length == 2 && args[0].equalsIgnoreCase("group"))
		{
			for(Group group : groups)
			{
				if(group.getName().toLowerCase().startsWith(args[1].toLowerCase()))
				{
					suggestions.add(group.getName());
				}
			}
		}
		else if(args.length == 2 && args[0].equalsIgnoreCase("user"))
		{
			for(String username : plugin.getOfflineUsernames())
			{
				if(username.toLowerCase().startsWith(args[1].toLowerCase()))
				{
					suggestions.add(username);
				}
			}
		}
		else if(args.length == 3
				&& args[0].equalsIgnoreCase("group")
				&& !groupMap.containsByName(args[1].toLowerCase()))
		{
			for(String subCommand : new String[] { "create" })
			{
				if(subCommand.startsWith(args[2].toLowerCase()))
				{
					suggestions.add(subCommand);
				}
			}
		}
		else if(args.length == 3
				&& args[0].equalsIgnoreCase("group")
				&& groupMap.containsByName(args[1].toLowerCase()))
		{
			for(String subCommand : new String[] { "delete", "perm", "user", "group" })
			{
				if(subCommand.startsWith(args[2].toLowerCase()))
				{
					suggestions.add(subCommand);
				}
			}
		}
		else if(args.length == 4
				&& args[0].equalsIgnoreCase("group")
				&& groupMap.containsByName(args[1].toLowerCase())
				&& args[2].toLowerCase().matches("perm|user|group"))
		{
			for(String subCommand : new String[] { "add", "remove", "list" })
			{
				if(subCommand.startsWith(args[3].toLowerCase()))
				{
					suggestions.add(subCommand);
				}
			}
		}
		else if(args.length == 5
				&& args[0].equalsIgnoreCase("group")
				&& (groupArg = permissionService.getGroupByName(args[1].toLowerCase())) != null
				&& args[2].equalsIgnoreCase("perm")
				&& args[3].equalsIgnoreCase("remove"))
		{
			for(String perm : groupArg.getPermissionMap().getPermissions().keySet())
			{
				if(perm.toLowerCase().startsWith(args[4].toLowerCase()))
				{
					suggestions.add(perm);
				}
			}
		}
		else if(args.length == 5
				&& args[0].equalsIgnoreCase("group")
				&& (groupArg = permissionService.getGroupByName(args[1].toLowerCase())) != null
				&& args[2].equalsIgnoreCase("user")
				&& args[3].equalsIgnoreCase("add"))
		{
			for(String username : plugin.getOfflineUsernames())
			{
				if(username.toLowerCase().startsWith(args[4].toLowerCase()))
				{
					suggestions.add(username);
				}
			}
		}
		else if(args.length == 5
				&& args[0].equalsIgnoreCase("group")
				&& (groupArg = permissionService.getGroupByName(args[1].toLowerCase())) != null
				&& args[2].equalsIgnoreCase("user")
				&& args[3].equalsIgnoreCase("remove"))
		{
			for(GroupUser groupUser : groupArg.getGroupUserMap().getGroupUsers().values())
			{
				String groupUserName = groupUser.getName(plugin);
				
				if(groupUserName != null && groupUserName.toLowerCase().startsWith(args[4].toLowerCase()))
				{
					suggestions.add(groupUserName);
				}
			}
		}
		else if(args.length == 5
				&& args[0].equalsIgnoreCase("group")
				&& (groupArg = permissionService.getGroupByName(args[1].toLowerCase())) != null
				&& args[2].equalsIgnoreCase("group")
				&& args[3].equalsIgnoreCase("add"))
		{
			for(Group group : groups)
			{
				if(group != groupArg && group.getName().toLowerCase().startsWith(args[4].toLowerCase()))
				{
					suggestions.add(group.getName());
				}
			}
		}
		else if(args.length == 5
				&& args[0].equalsIgnoreCase("group")
				&& (groupArg = permissionService.getGroupByName(args[1].toLowerCase())) != null
				&& args[2].equalsIgnoreCase("group")
				&& args[3].equalsIgnoreCase("remove"))
		{
			for(Group group : permissionService.getGroupsSlightly(groupArg))
			{
				if(group.getName().toLowerCase().startsWith(args[4].toLowerCase()))
				{
					suggestions.add(group.getName());
				}
			}
		}
		else if(args.length == 3
				&& args[0].equalsIgnoreCase("user")
				&& plugin.getOfflineUsernames().contains(args[1]))
		{
			for(String subCommand : new String[] { "perm" })
			{
				if(subCommand.startsWith(args[2].toLowerCase()))
				{
					suggestions.add(subCommand);
				}
			}
		}
		else if(args.length == 4
				&& args[0].equalsIgnoreCase("user")
				&& plugin.getOfflineUsernames().contains(args[1])
				&& args[2].equalsIgnoreCase("perm"))
		{
			for(String subCommand : new String[] { "add", "remove", "list" })
			{
				if(subCommand.startsWith(args[3].toLowerCase()))
				{
					suggestions.add(subCommand);
				}
			}
		}
		else if(args.length == 5
				&& args[0].equalsIgnoreCase("user")
				&& (user = getUserFromMemoryOrDisk(args[1])) != null
				&& args[2].equalsIgnoreCase("perm")
				&& args[3].equalsIgnoreCase("remove"))
		{
			for(String permission : user.getPermissionMap().getPermissions().keySet())
			{
				if(permission.startsWith(args[4].toLowerCase()))
				{
					suggestions.add(permission);
				}
			}
		}
		
		return suggestions;
	}
	
	private User getUserFromMemoryOrDisk(String userName)
	{
		return Optional.ofNullable(plugin.getOfflinePlayerIfCached(userName))
				.map(OfflinePlayer::getUniqueId)
				.flatMap(plugin.getUserFiler()::getFromMemoryOrDisk)
				.orElse(null);
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(!permissionService.hasPermission(commandSender, PERMISSION))
		{
			messageable.sendMessage(YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		try
		{
			if(args.length == 1 && args[0].equalsIgnoreCase("group"))
			{
				listGroups(messageable, permissionService.listGroupsOrIfEmptyThrow());
				return true;
			}
			else if(args.length == 3
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("create"))
			{
				String groupName = args[1].toLowerCase();
				permissionService.createGroup(groupName);
				messageable.sendMessage(COMMAND_PEX_GROUP_CREATED, groupName);
				return true;
			}
			else if(args.length == 3
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("delete"))
			{
				String groupName = args[1].toLowerCase();
				permissionService.deleteGroup(groupName);
				messageable.sendMessage(COMMAND_PEX_GROUP_DELETED, groupName);
				return true;
			}
			else if((args.length == 4 || args.length == 5)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("user")
					&& args[3].equalsIgnoreCase("list"))
			{
				final String groupName = args[1];
				final int page = args.length == 5 ? IntegerUtil.parseInt(args[4]) - 1 : 0;
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				GroupUserPage groupUserPage = permissionService.getGroupUserPage(group, page, 5);
				
				messageable.sendMessage(SHOW_PAGE,
						groupUserPage,
						PAGE_GROUP_USER_TITLE.withArgs(groupUserPage.getFullList().size(), group.getName()),
						COMMAND_PEX_RUN_COMMAND_GROUP_USER_LIST.withArgs(group.getName()));
				return true;
			}
			else if((args.length == 5 || args.length == 6)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("user")
					&& args[3].equalsIgnoreCase("add"))
			{
				final String groupName = args[1];
				final String userName = args[4];
				final long durationTicks = args.length == 6 ? LongUtil.parseLong(args[5]) : Long.MAX_VALUE;
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				OfflinePlayer offlinePlayer = permissionService.getOfflinePlayerOrElseThrow(userName);
				
				TimedTick timedTick = new TimedTick(plugin, durationTicks);
				
				GroupUser groupUser = GroupUser.builder()
						.userUniqueId(offlinePlayer.getUniqueId())
						.timedTick(timedTick)
						.build();
				
				permissionService.addUser(group, groupUser);
				messageable.sendMessage(COMMAND_PEX_USER_ADDED_TO_GROUP, offlinePlayer.getName(), group.getName());
				return true;
			}
			else if((args.length == 5)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("user")
					&& args[3].equalsIgnoreCase("remove"))
			{
				final String groupName = args[1];
				final String userName = args[4];
				
				OfflinePlayer offlinePlayer = permissionService.getOfflinePlayerOrElseThrow(userName);
				Group group = permissionService.getGroupOrElseThrow(groupName);
				
				permissionService.removeUser(group, offlinePlayer);
				messageable.sendMessage(COMMAND_PEX_USER_REMOVED_FROM_GROUP, offlinePlayer.getName(), group.getName());
				return true;
			}
			else if((args.length == 4 || args.length == 5)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("perm")
					&& args[3].equalsIgnoreCase("list"))
			{
				final String groupName = args[1];
				final int page = args.length == 5 ? IntegerUtil.parseInt(args[4]) - 1 : 0;
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				GroupPermPage groupPermPage = permissionService.getGroupPermPage(group, page, 5);
				
				messageable.sendMessage(SHOW_PAGE,
						groupPermPage,
						PAGE_GROUP_PERM_TITLE.withArgs(groupPermPage.getFullList().size(), group.getName()),
						COMMAND_PEX_RUN_COMMAND_GROUP_PERM_LIST.withArgs(group.getName()));
				return true;
			}
			else if((args.length == 5 || args.length == 6)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("perm")
					&& args[3].equalsIgnoreCase("add"))
			{
				final String groupName = args[1].toLowerCase();
				final String permName = args[4].toLowerCase();
				final long durationTicks = args.length == 6 ? LongUtil.parseLong(args[5]) : Long.MAX_VALUE;
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				
				TimedTick timedTick = new TimedTick(plugin, durationTicks);
				
				Permission permission = Permission.builder()
						.name(permName)
						.timedTick(timedTick)
						.build();
				
				permissionService.addPermission(group, permission);
				messageable.sendMessage(COMMAND_PEX_PERM_ADDED_TO_GROUP, permName, group.getName());
				return true;
			}
			else if((args.length == 5)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("perm")
					&& args[3].equalsIgnoreCase("remove"))
			{
				final String groupName = args[1];
				final String permName = args[4];
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				
				permissionService.removePermission(group, permName);
				messageable.sendMessage(COMMAND_PEX_PERM_REMOVED_FROM_GROUP, permName, group.getName());
				return true;
			}
			else if((args.length == 4 || args.length == 5)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("group")
					&& args[3].equalsIgnoreCase("list"))
			{
				final String groupName = args[1];
				final int page = args.length == 5 ? IntegerUtil.parseInt(args[4]) - 1 : 0;
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				GroupGroupPage groupGroupPage = permissionService.getGroupGroupPage(group, page, 5);
				
				messageable.sendMessage(SHOW_PAGE,
						groupGroupPage,
						PAGE_GROUP_GROUP_TITLE.withArgs(groupGroupPage.getFullList().size(), group.getName()),
						COMMAND_PEX_RUN_COMMAND_GROUP_GROUP_LIST.withArgs(group.getName()));
				return true;
			}
			else if((args.length == 5 || args.length == 6)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("group")
					&& args[3].equalsIgnoreCase("add"))
			{
				final String groupName = args[1].toLowerCase();
				final String subGroupName = args[4].toLowerCase();
				final long durationTicks = args.length == 6 ? LongUtil.parseLong(args[5]) : Long.MAX_VALUE;
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				Group subGroup = permissionService.getGroupOrElseThrow(subGroupName);
				
				TimedTick timedTick = new TimedTick(plugin, durationTicks);
				
				GroupGroup groupGroup = GroupGroup.builder()
						.groupUniqueId(subGroup.getGroupUniqueId())
						.timedTick(timedTick)
						.build();
				
				permissionService.addGroupGroup(group, groupGroup);
				messageable.sendMessage(COMMAND_PEX_GROUP_ADDED_TO_GROUP, subGroup.getName(), group.getName());
				return true;
			}
			else if((args.length == 5)
					&& args[0].equalsIgnoreCase("group")
					&& args[2].equalsIgnoreCase("group")
					&& args[3].equalsIgnoreCase("remove"))
			{
				final String groupName = args[1].toLowerCase();
				final String subGroupName = args[4].toLowerCase();
				
				Group group = permissionService.getGroupOrElseThrow(groupName);
				Group subGroup = permissionService.getGroupOrElseThrow(subGroupName);
				
				permissionService.removeSubGroup(group, subGroup);
				messageable.sendMessage(COMMAND_PEX_PERM_REMOVED_FROM_GROUP, subGroup.getName(), group.getName());
				return true;
			}
			else if((args.length == 4 || args.length == 5)
					&& args[0].equalsIgnoreCase("user")
					&& args[2].equalsIgnoreCase("perm")
					&& args[3].equalsIgnoreCase("list"))
			{
				final String userName = args[1];
				final int page = args.length == 5 ? IntegerUtil.parseInt(args[4]) - 1 : 0;
				
				OfflinePlayer offlinePlayer = permissionService.getOfflinePlayerOrElseThrow(userName);
				
				User user = permissionService.getUser(offlinePlayer.getUniqueId());
				UserPermPage userPermPage = permissionService.getUserPermPage(user, page, 5);
				
				messageable.sendMessage(SHOW_PAGE,
						userPermPage,
						PAGE_USER_PERM_TITLE.withArgs(userPermPage.getFullList().size(), offlinePlayer.getName()),
						COMMAND_PEX_RUN_COMMAND_USER_PERM_LIST.withArgs(offlinePlayer.getName()));
				return true;
			}
			else if((args.length == 5 || args.length == 6)
					&& args[0].equalsIgnoreCase("user")
					&& args[2].equalsIgnoreCase("perm")
					&& args[3].equalsIgnoreCase("add"))
			{
				final String userName = args[1];
				final String permName = args[4].toLowerCase();
				final long durationTicks = args.length == 6 ? LongUtil.parseLong(args[5]) : Long.MAX_VALUE;
				
				OfflinePlayer offlinePlayer = permissionService.getOfflinePlayerOrElseThrow(userName);
				
				User user = permissionService.getUser(offlinePlayer.getUniqueId());
				TimedTick timedTick = new TimedTick(plugin, durationTicks);
				
				Permission permission = Permission.builder()
						.name(permName)
						.timedTick(timedTick)
						.build();
				
				permissionService.addPermission(user, permission);
				messageable.sendMessage(COMMAND_PEX_PERM_ADDED_TO_USER, permName, offlinePlayer.getName());
				return true;
			}
			else if(args.length == 5
					&& args[0].equalsIgnoreCase("user")
					&& args[2].equalsIgnoreCase("perm")
					&& args[3].equalsIgnoreCase("remove"))
			{
				final String userName = args[1];
				final String permName = args[4].toLowerCase();
				
				OfflinePlayer offlinePlayer = permissionService.getOfflinePlayerOrElseThrow(userName);
				User user = permissionService.getUser(offlinePlayer.getUniqueId());
				
				permissionService.removePermission(user, permName);
				messageable.sendMessage(COMMAND_PEX_PERM_REMOVED_FROM_USER, permName, offlinePlayer.getName());
				return true;
			}
			else
			{
				messageable.sendMessage(COMMAND_PEX_USAGE);
				return false;
			}
		}
		catch(CommonRuntimeException | CommonException e)
		{
			messageable.sendMessage(e.getMessageArgs());
			return false;
		}
	}
	
	private void listGroups(Messageable messageable, Collection<Group> groups)
	{
		messageable.sendMessage(CommonMessage.COMMAND_PEX_LIST_GROUPS, groups);
	}
}
