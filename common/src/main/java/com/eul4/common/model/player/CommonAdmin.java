package com.eul4.common.model.player;

public interface CommonAdmin extends CommonPlayer
{
	boolean canBuild();
	void setBuild(boolean value);
	
	boolean toggleBuild();
}
