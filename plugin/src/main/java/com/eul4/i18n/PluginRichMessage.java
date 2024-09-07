package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.RichMessage;
import com.eul4.common.util.CommonMessageUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.COMMAND_REPLY_USAGE_$ALIASES;
import static com.eul4.common.util.CommonMessageUtil.displayName;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;

@RequiredArgsConstructor
@Getter
public enum PluginRichMessage implements RichMessage
{
	COMMAND_TOGGLE_COMBAT_NEW("command.toggle-combat.new"),
	COMMAND_TOGGLE_COMBAT_OLD("command.toggle-combat.old"),
	CONTAINTMENT_PICKAXE_LORE("containtment-pickaxe.lore", (locale, args) -> new TagResolver[]
	{
		component("chance", CommonMessageUtil.toPercentage(args[0], "0.#", locale)),
	}),
	MYSTHIC_LABEL("mysthic.label"),
	
	WORLD_RAID_LABEL("world.raid.label"),
	WORLD_RAID_NETHER_LABEL("world.raid-nether.label"),
	WORLD_RAID_END_LABEL("world.raid-end.label"),
	WORLD_NEWBIE_LABEL("world.newbie.label"),
	WORLD_NEWBIE_NETHER_LABEL("world.newbie-nether.label"),
	WORLD_NEWBIE_END_LABEL("world.newbie-end.label"),
	WORLD_TOWN_LABEL("world.town.label"),
	@Deprecated(forRemoval = true)
	WORLD_CIDADE_VIRTUAL_LABEL("world.cidade-virtual.label"),
	;
	
	private final String key;
	private final BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction;
	
	PluginRichMessage(String key)
	{
		this(key, (locale, args) -> new TagResolver[0]);
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return PluginBundleBaseName.PLUGIN_RICH_MESSAGE;
	}
}
