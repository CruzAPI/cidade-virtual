package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Admin;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginCommonPlayerType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CraftAdmin extends CraftPluginPlayer implements Admin
{
	private boolean canBuild;
	
	public CraftAdmin(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftAdmin(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		player.setGameMode(GameMode.CREATIVE);
	}
	
	@Override
	public boolean canBuild()
	{
		return canBuild;
	}
	
	@Override
	public void canBuild(boolean value)
	{
		this.canBuild = value;
	}
	
	@Override
	public PluginCommonPlayerType.Type getCommonPlayerTypeEnum()
	{
		return PluginCommonPlayerType.Type.ADMIN;
	}
}
