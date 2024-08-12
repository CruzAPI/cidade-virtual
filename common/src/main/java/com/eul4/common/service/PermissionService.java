package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.exception.*;
import com.eul4.common.model.permission.*;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.*;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;

import java.util.*;

@RequiredArgsConstructor
public class PermissionService
{
	private final Common plugin;
	
	public boolean hasPermission(CommonPlayer commonPlayer, String permissionName)
	{
		return hasPermission(commonPlayer.getUniqueId(), permissionName);
	}
	
	public boolean hasPermission(UUID userUniqueId, String permissionName)
	{
		return hasPermission(plugin.getUserFiler().load(userUniqueId), permissionName);
	}
	
	public boolean hasPermission(User user, String permissionName)
	{
		List<String> wildCardVariants = getWildCardVariants(permissionName);
		
		if(hasAnyPermission(user, wildCardVariants))
		{
			return true;
		}
		
		for(Group group : getGroupHierarchy(user))
		{
			if(hasAnyPermission(group, wildCardVariants))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void addPermission(Permissible permissible, Permission permission)
	{
		permissible.getPermissionMap().compute(plugin, permission);
	}
	
	public void removePermission(Group group, String permName) throws PermNotFoundInGroupException
	{
		if(removePermission((Permissible) group, permName) == null)
		{
			throw new PermNotFoundInGroupException(permName, group.getName());
		}
	}
	
	public Permission removePermission(Permissible permissible, String permName)
	{
		return permissible.getPermissionMap().getPermissions().remove(permName);
	}
	
	public void setPermission(Permissible permissible, Permission permission)
	{
		permissible.getPermissionMap().put(permission);
	}
	
	public void addUser(Group group, GroupUser groupUser)
	{
		group.getGroupUserMap().compute(plugin, groupUser);
	}
	
	public void removeUser(Group group, OfflinePlayer offlinePlayer) throws UserNotFoundInGroupException
	{
		if(group.getGroupUserMap().getGroupUsers().remove(offlinePlayer.getUniqueId()) == null)
		{
			throw new UserNotFoundInGroupException(offlinePlayer.getName(), group.getName());
		}
	}
	
	public void setUser(Group group, GroupUser groupUser)
	{
		group.getGroupUserMap().put(groupUser);
	}
	
	public void createGroup(String groupName) throws GroupAlreadyExistsException
	{
		GroupMap groupMap = getGroupMap();
		TreeMap<Group, Group> groups = groupMap.getGroups();
		
		Group newGroup = new Group(groupName, groups.isEmpty() ? 0 : groups.lastKey().getOrder() + 1);
		
		if(groupMap.containsKeyEqual(newGroup))
		{
			throw new GroupAlreadyExistsException(groupName);
		}
		
		groups.put(newGroup, newGroup);
	}
	
	public void deleteGroup(String groupName) throws GroupNotFoundException
	{
		if(getGroupMap().removeEqual(new Group(groupName)) == null)
		{
			throw new GroupNotFoundException(groupName);
		}
	}
	
	public void increaseGroup(String groupName) throws GroupNotFoundException
	{
		GroupMap groupMap = getGroupMap();
		TreeMap<Group, Group> groups = groupMap.getGroups();
		
		Group groupToIncrease = groupMap.getEqual(new Group(groupName));
		
		if(groupToIncrease == null)
		{
			throw new GroupNotFoundException(groupName);
		}
		
		Group lower = groups.lowerKey(groupToIncrease);
		
		if(lower == null)
		{
			return;
		}
		
		groups.remove(lower);
		groups.remove(groupToIncrease);
		
		Group.swapOrder(groupToIncrease, lower);
		
		groups.put(lower, lower);
		groups.put(groupToIncrease, groupToIncrease);
	}
	
	public User getUser(UUID userUniqueId)
	{
		return plugin.getUserFiler().load(userUniqueId);
	}
	
	public User getUserOrElseThrow(String userName) throws UserNotFoundException
	{
		return plugin.getUserFiler().load(getUserUniqueIdOrElseThrow(userName));
	}
	
	public Group getGroupOrElseThrow(String groupName) throws GroupNotFoundException
	{
		Group group = getGroup(groupName);
		
		if(group == null)
		{
			throw new GroupNotFoundException(groupName);
		}

		return group;
	}
	
	public Group getGroup(String groupName)
	{
		return getGroupMap().getEqual(new Group(groupName));
	}
	
	public UUID getUserUniqueIdOrElseThrow(String userName) throws UserNotFoundException
	{
		return getOfflinePlayerOrElseThrow(userName).getUniqueId();
	}
	
	public OfflinePlayer getOfflinePlayerOrElseThrow(String userName) throws UserNotFoundException
	{
		OfflinePlayer offlinePlayer = plugin.getOfflinePlayerIfCached(userName);
		
		if(offlinePlayer == null)
		{
			throw new UserNotFoundException(userName);
		}
		
		return offlinePlayer;
	}
	
	public GroupMap getGroupMap()
	{
		return plugin.getGroupMapFiler().getMemoryGroupMap();
	}
	
	public TreeMap<Group, Group> getGroups()
	{
		return plugin.getGroupMapFiler().getMemoryGroupMap().getGroups();
	}
	
	public Set<Group> listGroups()
	{
		return plugin.getGroupMapFiler().getMemoryGroupMap().getGroups().keySet();
	}
	
	public Set<Group> listGroupsOrIfEmptyThrow() throws EmptyListException
	{
		Set<Group> groups = listGroups();
		
		if(groups.isEmpty())
		{
			throw new EmptyListException();
		}
		
		return groups;
	}
	
	public UserPermPage getUserPermPage(User user, int page, int pageSize)
			throws PageNotFoundException, EmptyListException
	{
		return (UserPermPage) getPage(new ArrayList<>(user.getPermissionMap().getPermissions().values()), page, pageSize, UserPermPage::new);
	}
	
	public GroupUserPage getGroupUserPage(Group group, int page, int pageSize)
			throws PageNotFoundException, EmptyListException
	{
		return (GroupUserPage) getPage(new ArrayList<>(group.getGroupUserMap().getGroupUsers().values()), page, pageSize, GroupUserPage::new);
	}
	
	public GroupPermPage getGroupPermPage(Group group, int page, int pageSize)
			throws PageNotFoundException, EmptyListException
	{
		return (GroupPermPage) getPage(new ArrayList<>(group.getPermissionMap().getPermissions().values()), page, pageSize, GroupPermPage::new);
	}
	
	private <T> Page<T> getPage(List<T> list, int page, int pageSize, PageConstructor<T> pageConstructor)
			throws PageNotFoundException, EmptyListException
	{
		if(list.isEmpty())
		{
			throw new EmptyListException();
		}
		
		int fromIndex = page * pageSize;
		int toIndex = Math.min(list.size(), fromIndex + pageSize);
		
		List<T> subList = fromIndex < 0 || fromIndex >= list.size()
				? Collections.emptyList()
				: list.subList(fromIndex, toIndex);
		
		if(subList.isEmpty())
		{
			throw new PageNotFoundException();
		}
		
		return pageConstructor.newInstance(plugin, page, pageSize, subList, list);
	}
	
	private boolean hasUser(Group group, User user)
	{
		return Optional
				.ofNullable(group.getGroupUserMap().getGroupUsers().get(user.getUuid()))
				.filter(groupUser -> groupUser.isValid(plugin))
				.isPresent();
	}
	
	private Set<Group> getGroupHierarchy(User user)
	{
		TreeMap<Group, Group> groups = plugin.getGroupMapFiler().getMemoryGroupMap().getGroups();
		
		for(Group group : groups.keySet())
		{
			if(hasUser(group, user))
			{
				return groups.tailMap(group).keySet();
			}
		}
		
		return Collections.emptySet();
	}
	
	
	private List<String> getWildCardVariants(String permissionName)
	{
		List<String> wildCardVariants = new ArrayList<>();
		
		wildCardVariants.add(permissionName);
		
		String variant = permissionName;
		
		for(;;)
		{
			if(!variant.contains("."))
			{
				wildCardVariants.add("*");
				return wildCardVariants;
			}
			
			variant = variant.replaceAll("\\.[^.]*$", "");
			wildCardVariants.add(variant + ".*");
		}
	}
	
	private boolean hasAnyPermission(Permissible permissible, List<String> permissionNames)
	{
		return hasAnyPermission(permissible.getPermissionMap(), permissionNames);
	}
	
	private boolean hasAnyPermission(PermissionMap permissionMap, List<String> permissionNames)
	{
		for(String permissionName : permissionNames)
		{
			if(hasPermission(permissionMap, permissionName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hasPermission(PermissionMap permissionMap, String permissionName)
	{
		return Optional.ofNullable(permissionMap.getPermissions().get(permissionName))
				.filter(permission -> permission.isValid(plugin))
				.isPresent();
	}
}
