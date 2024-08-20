package com.eul4.common.model.permission;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Group implements Permissible, Comparable<Group>
{
	@EqualsAndHashCode.Include
	private final UUID groupUniqueId;
	
	private final PermissionMap permissionMap;
	private final GroupUserMap groupUserMap;
	private final GroupGroupMap groupGroupMap;
	
	@ToString.Include
	private String name;
	@ToString.Include
	private int order;
	
	public Group(String name)
	{
		this(name, 0);
	}
	
	public Group(String name, int order)
	{
		this(UUID.randomUUID(),
				new PermissionMap(),
				new GroupUserMap(),
				new GroupGroupMap(),
				name,
				order);
	}
	
	public Group(PermissionMap permissionMap,
			GroupUserMap groupUserMap,
			String name,
			int order)
	{
		this(UUID.randomUUID(),
				permissionMap,
				groupUserMap,
				new GroupGroupMap(),
				name,
				order);
	}
	
	public Group(UUID groupUniqueId,
			PermissionMap permissionMap,
			GroupUserMap groupUserMap,
			GroupGroupMap groupGroupMap,
			String name,
			int order)
	{
		this.groupUniqueId = Objects.requireNonNull(groupUniqueId);
		
		this.permissionMap = Objects.requireNonNull(permissionMap);
		this.groupUserMap = Objects.requireNonNull(groupUserMap);
		this.groupGroupMap = Objects.requireNonNull(groupGroupMap);
		
		this.name = Objects.requireNonNull(name);
		this.order = order;
	}
	
	@Override
	public int compareTo(@NotNull Group o)
	{
		return Integer.compare(this.order, o.order);
	}
	
	public static void swapOrder(Group group, Group other)
	{
		final int groupOrder = group.getOrder();
		final int otherOrder = other.getOrder();
		
		group.setOrder(otherOrder);
		other.setOrder(groupOrder);
	}
}
