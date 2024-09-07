package com.eul4.type;

import com.eul4.common.i18n.TranslatableMessage;
import com.eul4.common.type.player.CommonWorldType;
import com.eul4.common.world.CommonWorld;
import com.eul4.i18n.PluginRichMessage;
import com.eul4.world.craft.*;
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
	RAID_WORLD
	(
		new CraftRaidWorld
		(
			WorldCreator.name("world")
				.environment(World.Environment.NORMAL)
				.type(WorldType.NORMAL)
				.createWorld()
		),
		PluginRichMessage.WORLD_RAID_LABEL
	),
	
	RAID_NETHER
	(
		new CraftRaidNether
		(
			WorldCreator.name("world_nether")
				.environment(World.Environment.NETHER)
				.type(WorldType.NORMAL)
				.createWorld()
		),
		PluginRichMessage.WORLD_RAID_NETHER_LABEL
	),
	
	RAID_END
	(
		new CraftRaidEnd
		(
			WorldCreator.name("world_the_end")
				.environment(World.Environment.THE_END)
				.type(WorldType.NORMAL)
				.createWorld()
		),
		PluginRichMessage.WORLD_RAID_END_LABEL
	),
	
	NEWBIE_WORLD
	(
		new CraftNewbieWorld
		(
			WorldCreator.name("newbie_world")
				.environment(World.Environment.NORMAL)
				.type(WorldType.NORMAL)
				.createWorld()
		),
		PluginRichMessage.WORLD_NEWBIE_LABEL
	),
	
	NEWBIE_NETHER
	(
		new CraftNewbieNether
		(
			WorldCreator.name("newbie_world_nether")
				.environment(World.Environment.NETHER)
				.type(WorldType.NORMAL)
				.createWorld()
		),
		PluginRichMessage.WORLD_NEWBIE_NETHER_LABEL
	),
	
	NEWBIE_END
	(
		new CraftNewbieEnd
		(
			WorldCreator.name("newbie_world_the_end")
				.environment(World.Environment.THE_END)
				.type(WorldType.NORMAL)
				.createWorld()
		),
		PluginRichMessage.WORLD_NEWBIE_END_LABEL
	),
	
	TOWN_WORLD
	(
		new CraftTownWorld
		(
			WorldCreator.name("town_world")
				.environment(World.Environment.NORMAL)
				.type(WorldType.FLAT)
				.generator(new ChunkGenerator() {})
				.createWorld()
		),
		PluginRichMessage.WORLD_TOWN_LABEL
	),
	
	@Deprecated(forRemoval = true)
	CIDADE_VIRTUAL
	(
		new CraftTownWorld
		(
			WorldCreator.name("old_cidade_virtual")
				.environment(World.Environment.NORMAL)
				.generator(new ChunkGenerator() {})
				.createWorld()
		),
		PluginRichMessage.WORLD_CIDADE_VIRTUAL_LABEL
	),
	;
	
	private final CommonWorld instance;
	private final TranslatableMessage label;
}
