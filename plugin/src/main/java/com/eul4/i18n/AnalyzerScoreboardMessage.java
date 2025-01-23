package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.common.i18n.RichMessage;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.enums.Currency;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.OFFLINE;
import static com.eul4.common.i18n.CommonMessage.ONLINE;
import static com.eul4.common.util.CommonMessageUtil.displayName;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

@Getter
@RequiredArgsConstructor
public enum AnalyzerScoreboardMessage implements RichMessage
{
	TITLE("title", (locale, args) -> new TagResolver[]
	{
		component("player", displayName(args[0])),
	}),
	
	PLAYER_STATUS_PREFIX("player-status.prefix", (bundle, args) -> new TagResolver[]
	{
		component("player", displayName(args[0]))
	}),
	PLAYER_STATUS_SUFFIX("player-status.suffix", (locale, args) ->
	{
		boolean isOnline = (boolean) args[0];
		Message message = isOnline ? ONLINE : OFFLINE;
		
		return new TagResolver[]
		{
			component("status", message.translate(locale, String::toUpperCase)),
		};
	}),
	
	TOWN_HALL_LEVEL_PREFIX("town-hall-level.prefix"),
	TOWN_HALL_LEVEL_SUFFIX("town-hall-level.suffix", (bundle, args) -> new TagResolver[]
	{
		unparsed("level", args[0].toString())
	}),
	
	LIKES_PREFIX("likes.prefix"),
	LIKES_SUFFIX("likes.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = Currency.LIKE.getDecimalFormat(locale);
		Town town = (Town) args[0];
		
		String balance = town == null
				? "?"
				: decimalFormat.format(town.getLikesIncludingGenerators());
		String capacity = town == null
				? "?"
				: decimalFormat.format(town.getLikeCapacityIncludingGenerators());
		
		return new TagResolver[]
		{
			unparsed("like_balance", balance),
			unparsed("like_capacity", capacity),
		};
	}),
	
	DISLIKES_PREFIX("dislikes.prefix"),
	DISLIKES_SUFFIX("dislikes.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = Currency.DISLIKE.getDecimalFormat(locale);
		Town town = (Town) args[0];
		
		String balance = town == null
				? "?"
				: decimalFormat.format(town.getDislikesIncludingGenerators());
		String capacity = town == null
				? "?"
				: decimalFormat.format(town.getDislikeCapacityIncludingGenerators());
		
		return new TagResolver[]
		{
			unparsed("dislike_balance", balance),
			unparsed("dislike_capacity", capacity),
		};
	}),
	
	CROWNS_PREFIX("crowns.prefix"),
	CROWNS_SUFFIX("crowns.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = Currency.CROWN.getDecimalFormat(locale);
		Town town = (Town) args[0];
		
		String balance = town == null
				? "?"
				: decimalFormat.format(town.getCalculatedCrownBalance());
		String capacity = town == null
				? "?"
				: decimalFormat.format(town.calculateCrownCapacity());
		
		return new TagResolver[]
		{
			unparsed("crown_balance", balance),
			unparsed("crown_capacity", capacity),
		};
	}),
	
	HARDNESS_PREFIX("hardness.prefix"),
	HARDNESS_SUFFIX("hardness.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = new DecimalFormat("0.0", new DecimalFormatSymbols(locale));
		Town town = (Town) args[0];
		
		String hardness = town == null ? "?" : decimalFormat.format(town.getHardness());
		String hardnessLimit = town == null ? "?" : decimalFormat.format(town.getHardnessLimit());
		
		return new TagResolver[]
		{
			unparsed("hardness", hardness),
			unparsed("hardness_limit", hardnessLimit)
		};
	}),
	
	FOOTER_PREFIX("footer.prefix"),
	FOOTER_SUFFIX("footer.suffix"),
	;
	
	private final String key;
	private final BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction;
	
	AnalyzerScoreboardMessage(String key)
	{
		this(key, (locale, args) -> new TagResolver[0]);
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return PluginBundleBaseName.ANALYZER_SCOREBOARD;
	}
}
