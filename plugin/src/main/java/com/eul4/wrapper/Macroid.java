package com.eul4.wrapper;

import com.eul4.enums.StructureStatus;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import lombok.*;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Macroid
{
	private final List<Clipboard> clipboards = new ArrayList<>();
	
	private final Player player;
	private final String structureName;
	
	@Setter
	private StructureStatus structureStatus;
	
	private final CuboidRegionSelector selector;
	
	public Macroid(Player player, String structureName, StructureStatus structureStatus)
	{
		this.player = player;
		this.structureName = structureName;
		this.structureStatus = structureStatus;
		
		this.selector = new CuboidRegionSelector(FaweAPI.getWorld(getPlayer().getWorld().getName()));
	}
	
	public void saveSelector()
	{
		if(selector == null)
		{
			player.sendMessage("No selector");
			return;
		}
		
		try
		{
			CuboidRegion cuboidRegion = selector.getRegion();
			Clipboard clipboard = newClipboard(cuboidRegion);
			clipboards.add(clipboard);
			selector.clear();
			player.sendMessage("Clipboard added!");
		}
		catch(IncompleteRegionException e)
		{
			player.sendMessage("Incomplete region");
		}
	}
	
	private Clipboard newClipboard(CuboidRegion region)
	{
		BlockVector3 origin = region.getCenter().toBlockPoint().add(0, 1, 0);
		
		region.setPos1(region.getPos1().withY(0)); //TODO maybe weWorld.getMinY()?
		region.setPos2(region.getPos2().withY(region.getWorld().getMaxY()));
		
		Clipboard clipboard = new BlockArrayClipboard(region);
		clipboard.setOrigin(origin);
		
		return clipboard;
	}
	
	public String getFileName(int level)
	{
		return MessageFormat.format("{0}_{1}_{2}.schem",
				structureName,
				level,
				structureStatus.name().toLowerCase());
	}
}
