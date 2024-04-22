package com.eul4.common.model.player;

public interface CommonAdmin extends CommonPlayer
{
	boolean canBuild();
	void canBuild(boolean value);
	
	default boolean toggleBuild()
	{
		canBuild(!canBuild());
		return canBuild();
	}
}
