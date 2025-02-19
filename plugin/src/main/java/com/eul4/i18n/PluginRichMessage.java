package com.eul4.i18n;

import com.eul4.command.TrackCommand;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.RichMessage;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.enums.Rarity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.COMMAND_REPLY_USAGE_$ALIASES;
import static com.eul4.common.util.CommonMessageUtil.argToComponent;
import static com.eul4.common.util.CommonMessageUtil.displayName;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.styling;

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
	
	ORE_FOUND_ALERT("ore-found.alert", (locale, args) -> new TagResolver[]
	{
		component("player", CommonMessageUtil.displayName(args[0])),
		styling("rarity_color", Optional.ofNullable(((Rarity) args[1]).getStyle().color()).orElse(NamedTextColor.WHITE)),
		component("block", CommonMessageUtil.argToComponent(args[2])),
		component("amount", CommonMessageUtil.argToComponent(args[3])),
	}),
	
	COMMAND_TRACK_$RESULT("command.track", (locale, args) ->
	{
		TrackCommand.Result result = (TrackCommand.Result) args[0];
		
		return new TagResolver[]
		{
			component("tracker_x", argToComponent(result.getTrackerX())),
			component("tracker_y", argToComponent(result.getTrackerY())),
			component("tracker_z", argToComponent(result.getTrackerZ())),
			component("north_range", argToComponent(result.getRange(TrackCommand.Direction.NORTH))),
			component("south_range", argToComponent(result.getRange(TrackCommand.Direction.SOUTH))),
			component("east_range", argToComponent(result.getRange(TrackCommand.Direction.EAST))),
			component("west_range", argToComponent(result.getRange(TrackCommand.Direction.WEST))),
			component("results", result.getResultComponent()),
		};
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
