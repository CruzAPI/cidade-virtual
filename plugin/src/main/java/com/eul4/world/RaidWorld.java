package com.eul4.world;

public interface RaidWorld extends RaidLevel, SpawnProtectedLevel
{
	int NEAR_SPAWN_RADIUS = 512;
	
	@Override
	default int getNearSpawnRadius()
	{
		return NEAR_SPAWN_RADIUS;
	}
}
