package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.externalizer.reader.AdminReader;
import com.eul4.externalizer.reader.PhysicalPlayerReader;
import com.eul4.externalizer.reader.TownPlayerReader;
import com.eul4.externalizer.reader.VanillaPlayerReader;
import com.eul4.externalizer.writer.AdminWriter;
import com.eul4.externalizer.writer.PhysicalPlayerWriter;
import com.eul4.externalizer.writer.TownPlayerWriter;
import com.eul4.externalizer.writer.VanillaPlayerWriter;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.craft.player.CraftVanillaPlayer;
import com.eul4.model.player.*;
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
	;
	
	private final Class<? extends PhysicalPlayer> interfaceType;
	private final BiFunction<Player, Main, PhysicalPlayer> pluginConstructor;
	private final BiFunction<Player, PluginPlayer, PhysicalPlayer> commonPlayerConstructor;
	private final Class<? extends PhysicalPlayerWriter<?>> writerClass;
	private final Class<? extends PhysicalPlayerReader<?>> readerClass;
	
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
