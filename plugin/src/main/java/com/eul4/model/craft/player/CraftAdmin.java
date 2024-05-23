package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Admin;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CraftAdmin extends CraftPhysicalPlayer implements Admin
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
	public boolean mustSavePlayerData()
	{
		return true;
	}
	
	@Override
	public PhysicalPlayerType getPlayerType()
	{
		return PhysicalPlayerType.ADMIN;
	}
	
	@Override
	public PluginPlayer load()
	{
		return super.load();
	}
	
	@Override
	public PluginPlayer reload()
	{
		return load(); //TODO Is it?
	}
}
