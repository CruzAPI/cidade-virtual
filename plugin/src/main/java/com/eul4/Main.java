package com.eul4;

public class Main extends Commons
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		
		getLogger().info("Plugin enabled.");
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		
		getLogger().info("Plugin disabled.");
	}
}
