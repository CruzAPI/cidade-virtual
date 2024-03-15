package com.eul4.common.model.craft.player;

import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;

public class CraftCommonAdmin extends CraftCommonPlayer implements CommonAdmin
{
	private boolean canBuild;
	
	public CraftCommonAdmin(CommonPlayer oldCommonPlayer)
	{
		super(oldCommonPlayer);
	}
	
	@Override
	public void setBuild(boolean value)
	{
		this.canBuild = value;
	}
	
	@Override
	public boolean canBuild()
	{
		return canBuild;
	}
	
	@Override
	public boolean toggleBuild()
	{
		return canBuild = !canBuild;
	}
}
