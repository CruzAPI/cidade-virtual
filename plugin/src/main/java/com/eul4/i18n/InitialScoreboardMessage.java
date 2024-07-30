package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.model.town.Town;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

@Getter
public enum InitialScoreboardMessage implements Message
{
	TITLE("title"),
	
	HELLO_PREFIX("hello.prefix"),
	HELLO_ENTRY("hello.entry"),
	HELLO_SUFFIX("hello.suffix", (bundle, args) -> new Component[]
	{
		empty(),
		(Component) args[0],
	}),
	
	WELCOME_1_PREFIX("welcome-1.prefix"),
	WELCOME_1_ENTRY("welcome-1.entry"),
	WELCOME_1_SUFFIX("welcome-1.suffix"),
	
	WELCOME_2_PREFIX("welcome-2.prefix"),
	WELCOME_2_ENTRY("welcome-2.entry"),
	WELCOME_2_SUFFIX("welcome-2.suffix"),
	
	USE_TOWN_1_PREFIX("use-town-1.prefix"),
	USE_TOWN_1_ENTRY("use-town-1.entry"),
	USE_TOWN_1_SUFFIX("use-town-1.suffix"),
	
	USE_TOWN_2_PREFIX("use-town-2.prefix"),
	USE_TOWN_2_ENTRY("use-town-2.entry"),
	USE_TOWN_2_SUFFIX("use-town-2.suffix"),
	
	FOOTER_PREFIX("footer.prefix"),
	FOOTER_ENTRY("footer.entry"),
	FOOTER_SUFFIX("footer.suffix"),
	
	;
	
	private final String key;
	private final BundleBaseName bundleBaseName;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	
	InitialScoreboardMessage(String key)
	{
		this(key, empty());
	}
	
	InitialScoreboardMessage(String key, Component baseComponent)
	{
		this(key, (bundle, args) -> new Component[] { baseComponent });
	}
	
	InitialScoreboardMessage(String key, BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this(PluginBundleBaseName.INITIAL_SCOREBOARD, key, componentBiFunction);
	}
	
	InitialScoreboardMessage(BundleBaseName bundleBaseName,
			String key,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this.bundleBaseName = bundleBaseName;
		this.key = key;
		this.componentBiFunction = componentBiFunction;
	}
}
