package com.eul4.model.craft.town.structure;

import com.eul4.model.town.structure.Structure;
import com.eul4.wrapper.Resource;
import org.bukkit.block.Block;

import java.util.Optional;

public interface ResourceStructure extends Structure
{
	Optional<Resource> findResource(Block block);
	
	void steal(Resource resource);
}
