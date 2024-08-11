package com.eul4.common.wrapper;

import com.eul4.common.Common;
import com.eul4.common.model.permission.Permission;

import java.util.List;

public class GroupPermPage extends ExpirablePage<Permission>
{
	public GroupPermPage(Common plugin, int index, int pageSize, List<Permission> pageElements, List<Permission> fullList)
	{
		super(plugin, index, pageSize, pageElements, fullList);
	}
}
