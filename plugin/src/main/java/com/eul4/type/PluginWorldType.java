package com.eul4.type;

import com.eul4.common.type.player.CommonWorldType;
import com.eul4.common.world.CommonWorld;
import com.eul4.world.craft.CraftOverWorld;
import com.eul4.world.craft.CraftTownWorld;
import com.eul4.world.craft.CraftWorldNether;
import com.eul4.world.craft.CraftWorldTheEnd;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

@RequiredArgsConstructor
@Getter
public enum PluginWorldType implements CommonWorldType
{
	OVER_WORLD(new CraftOverWorld(WorldCreator.name("world")
			.environment(World.Environment.NORMAL)
			.type(WorldType.NORMAL)
			.createWorld())),
	
	WORLD_NETHER(new CraftWorldNether(WorldCreator.name("world_nether")
			.environment(World.Environment.NETHER)
			.type(WorldType.NORMAL)
			.createWorld())),
	
	WORLD_THE_END(new CraftWorldTheEnd(WorldCreator.name("world_the_end")
			.environment(World.Environment.THE_END)
			.type(WorldType.NORMAL)
			.createWorld())),
	
	TOWN_WORLD(new CraftTownWorld(WorldCreator.name("town_world")
			.environment(World.Environment.NORMAL)
			.type(WorldType.FLAT)
			.generator(new ChunkGenerator() {})
			.createWorld())),
	
	//TODO: Remove
//	CIDADE_VIRTUAL(new CraftTownWorld(WorldCreator.name("cidade_virtual")
//			.environment(World.Environment.THE_END)
//			.generator(new ChunkGenerator() {})
//			.createWorld())),
	;
	
	private final CommonWorld instance;
}
