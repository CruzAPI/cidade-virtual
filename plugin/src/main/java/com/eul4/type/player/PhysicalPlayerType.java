package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.externalizer.reader.*;
import com.eul4.externalizer.writer.*;
import com.eul4.model.craft.player.*;
import com.eul4.model.player.*;
import com.eul4.model.player.physical.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum PhysicalPlayerType implements PluginPlayerType
{
	ADMIN(Admin.class, CraftAdmin::new, CraftAdmin::new, AdminWriter.class, AdminReader.class),
	TOWN_PLAYER(TownPlayer.class, CraftTownPlayer::new, CraftTownPlayer::new, TownPlayerWriter.class, TownPlayerReader.class),
	VANILLA_PLAYER(VanillaPlayer.class, CraftVanillaPlayer::new, CraftVanillaPlayer::new, VanillaPlayerWriter.class, VanillaPlayerReader.class),
	SPAWN_PLAYER(SpawnPlayer.class, CraftSpawnPlayer::new, CraftSpawnPlayer::new, SpawnPlayerWriter.class, SpawnPlayerReader.class),
	TUTORIAL_TOWN_PLAYER(TutorialTownPlayer.class, CraftTutorialTownPlayer::new, CraftTutorialTownPlayer::new, TutorialTownPlayerWriter.class, TutorialTownPlayerReader.class),
	;
	
	private final Class<? extends PhysicalPlayer> interfaceType;
	private final BiFunction<Player, Main, PhysicalPlayer> pluginConstructor;
	private final BiFunction<Player, PluginPlayer, PhysicalPlayer> commonPlayerConstructor;
	private final Class<? extends PhysicalPlayerWriter> writerClass;
	private final Class<? extends PhysicalPlayerReader> readerClass;
	
	@Override
	public PhysicalPlayer newInstance(Player player, Main plugin)
	{
		return pluginConstructor.apply(player, plugin);
	}
	
	@Override
	public PhysicalPlayer newInstance(Player player, PluginPlayer pluginPlayer)
	{
		return commonPlayerConstructor.apply(player, pluginPlayer);
	}
	
	@Override
	public PlayerCategory getCategory()
	{
		return PlayerCategory.PHYSICAL;
	}
}
