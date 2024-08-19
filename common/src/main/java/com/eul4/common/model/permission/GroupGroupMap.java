package com.eul4.common.model.permission;

import com.eul4.common.Common;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class GroupGroupMap extends ExpirableMap<UUID, GroupGroup>
{
	private final HashMap<UUID, GroupGroup> groupGroups;
	
	public GroupGroupMap()
	{
		this(new HashMap<>());
	}
	
	private GroupGroupMap(HashMap<UUID, GroupGroup> groupGroups)
	{
		super(groupGroups);
		this.groupGroups = groupGroups;
	}
	
	public void put(GroupGroup groupGroup)
	{
		groupGroups.put(groupGroup.getGroupUniqueId(), groupGroup);
	}
	
	public void compute(Common plugin, GroupGroup groupGroup)
	{
		computeExpirable(plugin, groupGroup.getGroupUniqueId(), groupGroup);
	}
	
	public GroupGroup remove(GroupGroup groupGroup)
	{
		return remove(groupGroup.getGroupUniqueId());
	}
	
	public GroupGroup remove(UUID groupUniqueId)
	{
		return groupGroups.remove(groupUniqueId);
	}
}
