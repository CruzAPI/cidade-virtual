package com.eul4.common.i18n;

import com.eul4.common.command.BroadcastCommand;
import com.eul4.common.command.ClearChatCommand;
import com.eul4.common.command.PexCommand;
import com.eul4.common.model.permission.Group;
import com.eul4.common.wrapper.Page;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@Getter
public enum CommonMessage implements Message
{
	ADMINISTRATOR("administrator"),
	PLAYER("player"),
	SPECTATOR("spectator"),
	BROADCAST("broadcast"),
	USAGE("usage", empty().color(RED)),
	
	ONLINE("online", empty().color(GREEN).decorate(BOLD)),
	OFFLINE("offline", empty().color(DARK_RED).decorate(BOLD)),
	
	GAME_MODE_CHANGED("player-mode-changed",
	(bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		((CommonMessage) args[1]).translateOne(bundle.getLocale(), String::toUpperCase).color((TextColor) args[0]),
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
	CAN_NOT_PERFORM("can-not-perform", empty().color(RED)),
	SCOREBOARD_ENABLED("command.scoreboard.enabled", empty().color(GREEN)),
	SCOREBOARD_DISABLED("command.scoreboard.disabled", empty().color(GREEN)),
	
	EXCEPTION_GROUP_NOT_FOUND("exception.group-not-found", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
	}),
	
	EXCEPTION_GROUP_ALREADY_EXISTS("exception.group-already-exists", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
	}),
	
	EXCEPTION_USER_NOT_FOUND("exception.user-not-found", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
	}),
	
	EXCEPTION_USER_NOT_FOUND_IN_GROUP("exception.user-not-found-in-group", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
		text("\"" + args[1] + "\""),
	}),
	
	EXCEPTION_PERM_NOT_FOUND_IN_GROUP("exception.perm-not-found-in-group", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
		text("\"" + args[1] + "\""),
	}),
	
	EXCEPTION_PERM_NOT_FOUND_IN_USER("exception.perm-not-found-in-user", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
		text("\"" + args[1] + "\""),
	}),
	
	EXCEPTION_NUMBER_FORMAT("exception.number-format", empty().color(RED)),
	EXCEPTION_PAGE_NOT_FOUND("exception.page-not-found", empty().color(RED)),
	EXCEPTION_EMPTY_LIST("exception.empty-list", empty().color(RED)),
	
	COMMAND_PEX_USER_ADDED_TO_GROUP("command.pex.user-added-to-group", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" + args[0] + "\""),
		text("\"" + args[1] + "\""),
	}),
	
	COMMAND_PEX_USER_REMOVED_FROM_GROUP("command.pex.user-removed-from-group", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
		text("\"" +  args[1] + "\""),
	}),
	
	COMMAND_PEX_PERM_ADDED_TO_GROUP("command.pex.perm-added-to-group", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
		text("\"" +  args[1] + "\""),
	}),
	
	COMMAND_PEX_PERM_REMOVED_FROM_GROUP("command.pex.perm-removed-from-group", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
		text("\"" +  args[1] + "\""),
	}),
	
	COMMAND_PEX_PERM_ADDED_TO_USER("command.pex.perm-added-to-user", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
		text("\"" +  args[1] + "\""),
	}),
	
	COMMAND_PEX_PERM_REMOVED_FROM_USER("command.pex.perm-removed-from-user", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
		text("\"" +  args[1] + "\""),
	}),
	
	COMMAND_PEX_GROUP_CREATED("command.pex.group-created", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
	}),
	
	COMMAND_PEX_GROUP_DELETED("command.pex.group-deleted", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
	}),
	
	COMMAND_PEX_LIST_GROUPS("command.pex.list-groups", (bundle, args) ->
	{
		Set<Group> groups = (Set<Group>) args[0];
		
		Component groupsComponent = Component.empty().color(YELLOW);
		Iterator<Group> iterator = groups.iterator();
		
		while(iterator.hasNext())
		{
			final Group group = iterator.next();
			final String groupName = group.getName();
			final String runCommand = PexCommand.getSubCommandGroupIncrease(groupName);
			
			final ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, runCommand);
			final Component groupComponent = text(groupName).clickEvent(clickEvent);
			
			groupsComponent = groupsComponent.append(groupComponent);
			
			if(iterator.hasNext())
			{
				groupsComponent = groupsComponent.append(text(", ").color(GREEN));
			}
		}
		
		return new Component[]
		{
			empty().color(GREEN),
			groupsComponent,
		};
	}),
	
	PAGE_INDEX("page-index", (bundle, args) -> new Component[]
	{
		empty().color(YELLOW),
		text((int) args[0]).color(GOLD),
		text((int) args[1]).color(GOLD),
	}),
	
	CLICK_TO_NAVIGATE("click-to-navigate"),
	
	COMMAND_PEX_RUN_COMMAND_GROUP_USER_LIST((locale, args) -> Collections.singletonList(
			text("/" + PexCommand.COMMAND_NAME + " group " + args[0] + " user list " + args[1]))),
	COMMAND_PEX_RUN_COMMAND_GROUP_PERM_LIST((locale, args) -> Collections.singletonList(
			text("/" + PexCommand.COMMAND_NAME + " group " + args[0] + " perm list " + args[1]))),
	COMMAND_PEX_RUN_COMMAND_USER_PERM_LIST((locale, args) -> Collections.singletonList(
			text("/" + PexCommand.COMMAND_NAME + " user " + args[0] + " perm list " + args[1]))),
	
	PAGE_GROUP_USER_TITLE("page.group-user.title", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		text((int) args[0]).color(GOLD),
		text("(" + args[1] + ")").color(GOLD),
	}),
	
	PAGE_GROUP_PERM_TITLE("page.group-perm.title", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		text((int) args[0]).color(DARK_RED),
		text("(" + args[1] + ")").color(DARK_RED),
	}),
	
	PAGE_USER_PERM_TITLE("page.user-perm.title", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		text((int) args[0]).color(DARK_PURPLE),
		text("(" + args[1] + ")").color(DARK_PURPLE),
	}),
	
	COMMAND_PEX_USAGE((locale, args) ->
	{
		List<Component> components = new ArrayList<>();
		
		TextColor baseColor = RED;
		
		components.add(USAGE.translateOne(locale, word -> WordUtils.capitalize(word + ":")).color(baseColor));
		components.add(text("/pex group").color(baseColor));
		components.add(text("/pex group <group> create").color(baseColor));
		components.add(text("/pex group <group> delete").color(baseColor));
		components.add(text("/pex group <group> perm add <perm> [ticks]").color(baseColor));
		components.add(text("/pex group <group> perm remove <perm>").color(baseColor));
		components.add(text("/pex group <group> user add <user> [ticks]").color(baseColor));
		components.add(text("/pex group <group> user remove <user>").color(baseColor));
		components.add(text("/pex group <group> user list").color(baseColor));
		components.add(text("/pex user <user> perm add <perm> [ticks]").color(baseColor));
		components.add(text("/pex user <user> perm remove <perm>").color(baseColor));
		components.add(text("/pex user <user> perm list").color(baseColor));
		
		return components;
	}),
	
	SHOW_PAGE((locale, args) ->
	{
		Page<?> page = (Page<?>) args[0];
		Component title = ((MessageArgs) args[1]).translateOne(locale);
		MessageArgs runCommand = (MessageArgs) args[2];
		
		List<Component> components = new ArrayList<>();
		
		HoverEvent<Component> clickToNavigate = CLICK_TO_NAVIGATE.translateOne(locale).asHoverEvent();
		
		ClickEvent runPreviousPage = ClickEvent.runCommand(runCommand.moreArgs(page.getDisplayIndex() - 1).translatePlain(locale));
		ClickEvent runNextPage = ClickEvent.runCommand(runCommand.moreArgs(page.getDisplayIndex() + 1).translatePlain(locale));
		
		Component previousPage = page.hasPreviousPage()
				? Component.text("<<<")
						.color(GOLD)
						.clickEvent(runPreviousPage)
						.hoverEvent(clickToNavigate)
						.append(text(" "))
				: Component.empty();
		
		Component nextPage = page.hasNextPage()
				? Component.text(" ")
						.append(text(">>>")
						.color(GOLD)
						.clickEvent(runNextPage)
						.hoverEvent(clickToNavigate))
				: Component.empty();
		
		Component pageIndex = PAGE_INDEX.translateOne(locale, page.getDisplayIndex(), page.getAmountOfPages());
		
		Component leftTraces = Component.text("============ ").color(GRAY);
		Component rightTraces = Component.text(" ============").color(GRAY);
		
		Component header = leftTraces
				.append(title)
				.append(rightTraces);
		
		Component footer = leftTraces
				.append(previousPage)
				.append(pageIndex)
				.append(nextPage)
				.append(rightTraces);
		
		components.add(header);
		components.addAll(page.getPageComponentsTranslated(locale));
		components.add(footer);
		
		return components;
	}),
	
	COMMAND_BROADCAST((locale, args) ->
	{
		Component broadcast = BROADCAST.translateOne(locale, WordUtils::capitalize).color(AQUA).decorate(BOLD);
		Component colon = text("»").color(GRAY);
		Component message = (Component) args[0];
		
		Component component = empty()
				.append(broadcast)
				.appendSpace()
				.append(colon)
				.appendSpace()
				.append(message);
		
		return Collections.singletonList(component);
	}),
	
	COMMAND_BROADCAST_USAGE((locale, args) -> Collections
			.singletonList(Component.text("/" + BroadcastCommand.COMMAND_NAME + " <msg...>").color(RED))),
	
	COMMAND_CLEAR_CHAT_USAGE((locale, args) -> Collections
			.singletonList(Component.text("/" + args[0]).color(RED))),
	
	COMMAND_CHAT_USAGE((locale, args) -> Collections
			.singletonList(Component.text("/" + args[0] + " <on:off>").color(RED))),
	COMMAND_CHAT_CHAT_ENABLED("command.chat.chat-enabled", empty().color(GREEN)),
	COMMAND_CHAT_CHAT_DISABLED("command.chat.chat-disabled", empty().color(GREEN)),
	COMMAND_CHAT_CHAT_ALREADY_ENABLED("command.chat.chat-already-enabled", empty().color(RED)),
	COMMAND_CHAT_CHAT_ALREADY_DISABLED("command.chat.chat-already-disabled", empty().color(RED)),
	COMMAND_CHAT_CHAT_IS_DISABLED("command.chat.chat-is-disabled", empty().color(RED)),
	;
	
	private final BundleBaseName bundleBaseName;
	private final String key;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	private final BiFunction<Locale, Object[], List<Component>> untranslatableComponentBiFunction;
	
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
		
		this.untranslatableComponentBiFunction = null;
	}
	
	CommonMessage(BiFunction<Locale, Object[], List<Component>> untranslatableComponentBiFunction)
	{
		this.untranslatableComponentBiFunction = untranslatableComponentBiFunction;
		
		this.bundleBaseName = null;
		this.key = null;
		this.componentBiFunction = null;
	}
}
