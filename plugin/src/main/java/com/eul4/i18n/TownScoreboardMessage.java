package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.RichMessage;
import com.eul4.enums.Currency;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

@Getter
@RequiredArgsConstructor
public enum TownScoreboardMessage implements RichMessage
{
	TITLE("title"),
	
	LIKES_PREFIX("likes.prefix"),
	LIKES_SUFFIX("likes.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = Currency.LIKE.getDecimalFormat(locale);
		Town town = (Town) args[0];
		
		return new TagResolver[]
		{
			unparsed("like_balance", decimalFormat.format(town.getLikes())),
			unparsed("like_capacity", decimalFormat.format(town.getLikeCapacity()))
		};
	}),
	
	DISLIKES_PREFIX("dislikes.prefix"),
	DISLIKES_SUFFIX("dislikes.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = Currency.DISLIKE.getDecimalFormat(locale);
		Town town = (Town) args[0];
		
		return new TagResolver[]
		{
			unparsed("dislike_balance", decimalFormat.format(town.getDislikes())),
			unparsed("dislike_capacity", decimalFormat.format(town.getDislikeCapacity()))
		};
	}),
	
	CROWNS_PREFIX("crowns.prefix"),
	CROWNS_SUFFIX("crowns.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = Currency.CROWN.getDecimalFormat(locale);
		Town town = (Town) args[0];
		
		return new TagResolver[]
		{
			unparsed("crown_balance", decimalFormat.format(town.getCalculatedCrownBalance())),
			unparsed("crown_capacity", decimalFormat.format(town.calculateCrownCapacity()))
		};
	}),
	
	HARDNESS_PREFIX("hardness.prefix"),
	HARDNESS_SUFFIX("hardness.suffix", (locale, args) ->
	{
		DecimalFormat decimalFormat = new DecimalFormat("0.0", new DecimalFormatSymbols(locale));
		Town town = (Town) args[0];
		
		return new TagResolver[]
		{
			unparsed("hardness", decimalFormat.format(town.getHardness())),
			unparsed("hardness_limit", decimalFormat.format(town.getHardnessLimit()))
		};
	}),
	
	FOOTER_PREFIX("footer.prefix"),
	FOOTER_SUFFIX("footer.suffix"),
	
	;
	
	private final String key;
	private final BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction;
	
	TownScoreboardMessage(String key)
	{
		this(key, (locale, args) -> new TagResolver[0]);
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return PluginBundleBaseName.TOWN_SCOREBOARD;
	}
}
