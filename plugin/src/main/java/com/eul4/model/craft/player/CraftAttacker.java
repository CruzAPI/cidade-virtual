package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Attacker;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.wrapper.TownAttack;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;
import java.util.logging.Level;

public class CraftAttacker extends CraftSpiritualPlayer implements Attacker
{
	private final Town town;
	@Getter
	private final TownAttack townAttack;
	
	public CraftAttacker(Player player, Main plugin)
	{
		super(player, plugin);
		this.town = null;
		this.townAttack = null;
	}
	
	public CraftAttacker(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.town = pluginPlayer instanceof RaidAnalyzer raidAnalyzer
				? raidAnalyzer.getAnalyzingTown()
				: null;
		this.townAttack = pluginPlayer instanceof RaidAnalyzer
				? new TownAttack(town, this)
				: null;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		
		if(town.findTownBlock(player.getLocation().getBlock())
				.filter(Predicate.not(TownBlock::isAvailable)).isEmpty())
		{
			Location spawnLocation = town.getUnavailableRandomTownBlock()
					.getBlock()
					.getRelative(BlockFace.UP)
					.getLocation()
					.toCenterLocation();
			
			Location townHallLocation = town.getTownHall()
					.getCenterTownBlock()
					.getBlock()
					.getLocation()
					.toCenterLocation();
			
			double dx = townHallLocation.getX() - spawnLocation.getX();
			double dz = townHallLocation.getZ() - spawnLocation.getZ();
			
			float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
			
			spawnLocation.setYaw(yaw);
			
			player.teleport(spawnLocation);
		}
		
		player.sendMessage("reset() attack"); //TODO ...
		player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
	}
	
	private PluginPlayer startAttack()
	{
		try
		{
			townAttack.start();
			reset();
			return this;
		}
		catch(Exception e)
		{
			player.sendMessage("could not start attack");//TODO...
			plugin.getLogger().log(Level.WARNING, "Failed to start an attack.", e);
			return reincarnate();
		}
	}
	
	@Override
	public PluginPlayer load()
	{
		if(player.isDead())
		{
			return this;
		}
		
		return super.load();
	}
	
	@Override
	public PluginPlayer reload()
	{
		return startAttack();
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.ATTACKER;
	}
}
