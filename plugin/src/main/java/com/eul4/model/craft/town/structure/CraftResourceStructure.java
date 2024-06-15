package com.eul4.model.craft.town.structure;

import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.wrapper.Resource;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public abstract class CraftResourceStructure extends CraftStructure implements ResourceStructure
{
//	private final Map<Block, Resource> resourceMap; TODO....
	
	public CraftResourceStructure(Town town)
	{
		super(town);
	}
	
	public CraftResourceStructure(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock);
	}
	
	public CraftResourceStructure(Town town, TownBlock centerTownBlock, boolean isBuilt)
			throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
//	TODO....
//	{
//		for(Resource resource : getResources())
//		{
//			resourceMap.put(resource.getRelative(getCenterTownBlock().getBlock()));
//		}
//		resourceMap = null;
//	}
	
	public abstract Set<Resource> getResources();
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		placeResources();
	}
	
	private void placeResource(Resource resource)
	{
		resource.getRelative(getCenterTownBlock().getBlock()).setBlockData(resource.getBlockData());
	}
	
	private void placeResources()
	{
		for(Resource resource : getResources())
		{
			placeResource(resource);
		}
	}
	
	@Override
	public Optional<Resource> findResource(Block block)
	{
		for(Resource resource : getResources())
		{
			if(resource.getRelative(this.getCenterTownBlock().getBlock()).equals(block))
			{
				return Optional.of(resource);
			}
		}
		
		return Optional.empty();
	}
}
