package com.eul4.common.model.permission;

import lombok.Getter;

import java.util.*;

@Getter
public class GroupMap
{
	private final HashMap<UUID, Group> groups;
	
	public GroupMap()
	{
		groups = new HashMap<>();
	}
	
	public void put(Group group)
	{
		groups.put(group.getGroupUniqueId(), group);
	}
	
	public boolean containsByName(String groupName)
	{
		return getByName(groupName) != null;
	}
	
	public Group getByName(String groupName)
	{
		for(Map.Entry<UUID, Group> entry : groups.entrySet())
		{
			Group group = entry.getValue();
			
			if(groupName.equals(group.getName()))
			{
				return group;
			}
		}
		
		return null;
	}
	
	public Group removeByName(String groupName)
	{
		Group groupToRemove = getByName(groupName);
		
		if(groupToRemove == null)
		{
			return null;
		}
		
		return groups.remove(groupToRemove.getGroupUniqueId());
	}
	
	public Group remove(UUID groupUniqueId)
	{
		return groups.remove(groupUniqueId);
	}
	
	public boolean containsKey(UUID groupUniqueId)
	{
		return groups.containsKey(groupUniqueId);
	}
	
	public Group get(UUID groupUniqueId)
	{
		return groups.get(groupUniqueId);
	}
}
