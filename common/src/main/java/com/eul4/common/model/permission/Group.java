package com.eul4.common.model.permission;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Group implements Permissible, Comparable<Group>
{
	private final PermissionMap permissionMap;
	private final GroupUserMap groupUserMap;
	
	@EqualsAndHashCode.Include
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
		this(new PermissionMap(), new GroupUserMap(), name, order);
	}
	
	@Builder
	public Group(PermissionMap permissionMap, GroupUserMap groupUserMap, String name, int order)
	{
		this.permissionMap = Objects.requireNonNull(permissionMap);
		this.groupUserMap = Objects.requireNonNull(groupUserMap);
		
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
