package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.model.town.Town;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@Getter
public enum AnalyzerScoreboardMessage implements Message
{
	TITLE("title", (bundle, args) -> new Component[]
	{
		empty().color(WHITE).decorate(BOLD),
		text(Optional.ofNullable((Town) args[0])
				.map(Town::getOwner)
				.map(OfflinePlayer::getName)
				.map(String::toUpperCase)
				.orElse("???")),
	}),
	
	ANALYZING_PREFIX("analyzing.prefix"),
	ANALYZING_ENTRY("analyzing.entry"),
	ANALYZING_SUFFIX("analyzing.suffix", (bundle, args) -> new Component[]
	{
		empty(),
		(Component) args[0],
	}),
	
	TOWN_HALL_LEVEL_PREFIX("town-hall-level.prefix"),
	TOWN_HALL_LEVEL_ENTRY("town-hall-level.entry"),
	TOWN_HALL_LEVEL_SUFFIX("town-hall-level.suffix", (bundle, args) -> new Component[]
	{
		empty(),
		text((int) args[0]),
	}),
	
	LIKES_PREFIX("likes.prefix"),
	LIKES_ENTRY("likes.entry"),
	LIKES_SUFFIX("likes.suffix", (bundle, args) ->
	{
		NumberFormat numberFormat = NumberFormat.getInstance(bundle.getLocale());
		Town town = (Town) args[0];
		
		return new Component[]
		{
			empty(),
			text(town == null ? "?" : numberFormat.format(town.getLikesIncludingGenerators())),
			text(town == null ? "?" : numberFormat.format(town.getLikeCapacityIncludingGenerators())),
		};
	}),
	
	DISLIKES_PREFIX("dislikes.prefix"),
	DISLIKES_ENTRY("dislikes.entry"),
	DISLIKES_SUFFIX("dislikes.suffix", (bundle, args) ->
	{
		NumberFormat numberFormat = NumberFormat.getInstance(bundle.getLocale());
		Town town = (Town) args[0];
		
		return new Component[]
		{
			empty(),
			text(town == null ? "?" : numberFormat.format(town.getDislikesIncludingGenerators())),
			text(town == null ? "?" : numberFormat.format(town.getDislikeCapacityIncludingGenerators())),
		};
	}),
	
	HARDNESS_PREFIX("hardness.prefix"),
	HARDNESS_ENTRY("hardness.entry"),
	HARDNESS_SUFFIX("hardness.suffix", (bundle, args) ->
	{
		DecimalFormat decimalFormat = new DecimalFormat("0.0", new DecimalFormatSymbols(bundle.getLocale()));
		Town town = (Town) args[0];
		
		return new Component[]
		{
			empty(),
			text(town == null ? "?" : decimalFormat.format(town.getHardness())),
			text(town == null ? "?" : decimalFormat.format(town.getHardnessLimit())),
		};
	}),
	
	FOOTER_PREFIX("footer.prefix"),
	FOOTER_ENTRY("footer.entry"),
	FOOTER_SUFFIX("footer.suffix"),
	;
	
	private final String key;
	private final BundleBaseName bundleBaseName;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	
	AnalyzerScoreboardMessage(String key)
	{
		this(key, empty());
	}
	
	AnalyzerScoreboardMessage(String key, Component baseComponent)
	{
		this(key, (bundle, args) -> new Component[] { baseComponent });
	}
	
	AnalyzerScoreboardMessage(String key, BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this(PluginBundleBaseName.ANALYZER_SCOREBOARD, key, componentBiFunction);
	}
	
	AnalyzerScoreboardMessage(BundleBaseName bundleBaseName,
			String key,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this.bundleBaseName = bundleBaseName;
		this.key = key;
		this.componentBiFunction = componentBiFunction;
	}
}
