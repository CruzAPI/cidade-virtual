package com.eul4.common.wrapper;

import com.eul4.common.Common;
import com.eul4.common.model.permission.GroupUser;

import java.util.List;

public class GroupUserPage extends ExpirablePage<GroupUser>
{
	public GroupUserPage(Common plugin, int index, int pageSize, List<GroupUser> pageElements, List<GroupUser> fullList)
	{
		super(plugin, index, pageSize, pageElements, fullList);
	}
}
