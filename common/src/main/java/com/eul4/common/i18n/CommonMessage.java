package com.eul4.common.i18n;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@Getter
public enum CommonMessage implements Message
{
	ADMINISTRATOR("administrator"),
	PLAYER("player"),
	SPECTATOR("spectator"),
	USAGE("usage", empty().color(RED)),
	
	GAME_MODE_CHANGED("player-mode-changed",
	(bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		((CommonMessage) args[1]).translateWord(bundle.getLocale(), String::toUpperCase).color((TextColor) args[0]),
	}),
	
	COMMAND_BUILD_ENABLED("command.build.enabled", empty().color(GREEN)),
	COMMAND_BUILD_DISABLED("command.build.disabled", empty().color(GREEN)),
	COMMAND_BUILD_NEED_ADMIN("command.build.need-admin", empty().color(RED)),
	YOU_DO_NOT_HAVE_PERMISSION("you-do-not-have-permission", empty().color(RED)),
	PERCENTAGE("percentage", (bundle, args) -> new Component[]
	{
		Component.empty(),
		Component.text(new DecimalFormat(args[0].toString(), new DecimalFormatSymbols(bundle.getLocale()))
				.format((double) args[1]).concat("%"))
	}),
	
	DAYS_CHAR("days-char"),
	HOURS_CHAR("hours-char"),
	MINUTES_CHAR("minutes-char"),
	SECONDS_CHAR("seconds-char"),
	
	CONFIRM("confirm"),
	CANCEL("cancel"),
	CAN_NOT_PERFORM("can-not-perform", empty().color(RED));
	
	private final BundleBaseName bundleBaseName;
	private final String key;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	
	CommonMessage(String key)
	{
		this(key, empty());
	}
	
	CommonMessage(String key, Component baseComponent)
	{
		this(key, (bundle, args) -> new Component[] { baseComponent });
	}
	
	CommonMessage(String key, BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this(CommonBundleBaseName.COMMON, key, componentBiFunction);
	}
	
	CommonMessage(BundleBaseName bundleBaseName,
			String key,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this.bundleBaseName = bundleBaseName;
		this.key = key;
		this.componentBiFunction = componentBiFunction;
	}
}
