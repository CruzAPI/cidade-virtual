package com.eul4.i18n;

import com.eul4.command.TrackCommand;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.RichMessage;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.enums.Rarity;
import com.eul4.model.town.Town;
import com.eul4.wrapper.CrownInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.eul4.common.util.CommonMessageUtil.argToComponent;
import static com.eul4.common.util.CommonMessageUtil.decimalToComponent;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.STRIKETHROUGH;
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
	
	CROWN_INFO("crown-info", (locale, args) ->
	{
		final CrownInfo crownInfo = (CrownInfo) args[0];
		final DecimalFormat decimalFormat = new DecimalFormat();
		
		decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
		decimalFormat.applyPattern("0.00");
		
		return new TagResolver[]
		{
			component("server_treasue", CommonMessageUtil.decimalToComponent(crownInfo.getServerTreasure(), decimalFormat)),
			component("jackpot", CommonMessageUtil.decimalToComponent(crownInfo.getJackpot(), decimalFormat)),
			component("town_hall_vault", CommonMessageUtil.decimalToComponent(crownInfo.getTownHallVault(), decimalFormat)),
			component("eul4_insights", CommonMessageUtil.decimalToComponent(crownInfo.getEul4Insights(), decimalFormat)),
		};
	}),
	
	STRUCTURE_CROWN_DEPOSIT_SHOP_LORE("structure.crown-deposit.shop-lore"),
	
	COMMAND_BALANCE_$TOWN("command.balance", (bundle, args) ->
	{
		Town town = (Town) args[0];
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(bundle);
		
		DecimalFormat intDecimalFormat = new DecimalFormat("0", symbols);
		DecimalFormat crownDecimalFormat = new DecimalFormat("0.00", symbols);
		
		intDecimalFormat.setGroupingUsed(true);
		crownDecimalFormat.setGroupingUsed(true);
		intDecimalFormat.setGroupingSize(3);
		crownDecimalFormat.setGroupingSize(3);
		
		return new TagResolver[]
		{
			component("like_balance", decimalToComponent(town.getLikes(), intDecimalFormat)),
			component("like_capacity", decimalToComponent(town.getLikeCapacity(), intDecimalFormat)),
			component("dislike_balance", decimalToComponent(town.getDislikeCapacity(), intDecimalFormat)),
			component("dislike_capacity", decimalToComponent(town.getDislikeCapacity(), intDecimalFormat)),
			component("crown_balance", decimalToComponent(town.calculateCrownBalance(), crownDecimalFormat)),
			component("crown_capacity", decimalToComponent(town.calculateCrownCapacity(), crownDecimalFormat)),
		};
	}),
	
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
