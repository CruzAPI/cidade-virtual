package com.eul4.common.i18n;

import com.eul4.common.command.BroadcastCommand;
import com.eul4.common.command.PexCommand;
import com.eul4.common.model.permission.Group;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.common.util.CommonWordUtil;
import com.eul4.common.wrapper.Page;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.BiFunction;

import static com.eul4.common.util.CommonMessageUtil.*;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

@Getter
public enum CommonMessage implements Message
{
	ADMINISTRATOR("administrator"),
	PLAYER("player"),
	USER("user"),
	SPECTATOR("spectator"),
	BROADCAST("broadcast"),
	MESSAGE("message"),
	
	USAGE("usage", empty().color(RED)),
	
	ONLINE("online", empty().color(GREEN).decorate(BOLD)),
	OFFLINE("offline", empty().color(DARK_RED).decorate(BOLD)),
	
	GAME_MODE_CHANGED("player-mode-changed",
	(bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		((CommonMessage) args[1]).translate(bundle.getLocale(), String::toUpperCase).color((TextColor) args[0]),
	}),
	
	COMMAND_INVSEE_USAGE_$ALIASES((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(args[0]))
					.appendSpace()
					.append(usageRequiredArg(PLAYER.translate(locale)))
					.color(RED))),
	COMMAND_INVSEE_USE_$ALIASES((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendNewline()
					.append(COMMAND_INVSEE_USAGE_$ALIASES.translate(locale, args[0]))
					.color(RED))),
	
	COMMAND_EXTRAINVSEE_USAGE_$ALIASES((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(args[0]))
					.appendSpace()
					.append(usageRequiredArg(PLAYER.translate(locale)))
					.color(RED))),
	COMMAND_EXTRAINVSEE_USE_$ALIASES((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendNewline()
					.append(COMMAND_EXTRAINVSEE_USAGE_$ALIASES.translate(locale, args[0]))
					.color(RED))),
	
	
	COMMAND_BUILD_ENABLED("command.build.enabled", empty().color(GREEN)),
	COMMAND_BUILD_DISABLED("command.build.disabled", empty().color(GREEN)),
	COMMAND_BUILD_NEED_ADMIN("command.build.need-admin", empty().color(RED)),
	YOU_DO_NOT_HAVE_PERMISSION("you-do-not-have-permission", empty().color(RED)),
	PERCENTAGE("percentage", (bundle, args) -> new Component[]
	{
		Component.empty(),
		text(new DecimalFormat(args[0].toString(), new DecimalFormatSymbols(bundle.getLocale()))
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
	
	COMMAND_INVSEE_YOURSELF("command.invsee.yourself", empty().color(RED)),
	
	COMMAND_MUTE_NAME("command.mute.name"),
	COMMAND_MUTE_YOURSELF("command.mute.yourself", empty().color(RED)),
	COMMAND_MUTE_USER_MUTED("command.mute.user-muted", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		CommonMessageUtil.getOfflinePlayerDisplayName((OfflinePlayer) args[0]),
	}),
	
	COMMAND_UNMUTE_NAME("command.unmute.name"),
	COMMAND_UNMUTE_USER_UNMUTED("command.unmute.user-unmuted", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		CommonMessageUtil.getOfflinePlayerDisplayName((OfflinePlayer) args[0]),
	}),
	
	COMMAND_DISABLE_TELL_NAME("command.disable-tell.name"),
	COMMAND_ENABLE_TELL_NAME("command.enable-tell.name"),
	COMMAND_DISABLE_CHAT_NAME("command.disable-chat.name"),
	COMMAND_ENABLE_CHAT_NAME("command.enable-chat.name"),
	
	COMMAND_TELL_NAME("command.tell.name"),
	
	COMMAND_BROADCAST_USAGE((locale, args) -> Collections
			.singletonList(text("/" + BroadcastCommand.COMMAND_NAME + " <msg...>").color(RED))),
	
	COMMAND_CLEAR_CHAT_USAGE((locale, args) -> Collections
			.singletonList(text("/" + args[0]).color(RED))),
	COMMAND_CHAT_USAGE((locale, args) -> Collections
			.singletonList(text("/" + args[0] + " <on:off>").color(RED))),
	COMMAND_MUTE_USAGE((locale, args) -> Collections
			.singletonList(text("/" + args[0] + " <")
					.append(argToComponent(args[1]))
					.append(text(">"))
					.color(RED))),
	COMMAND_MUTE_USE((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendNewline()
					.append(COMMAND_MUTE_USAGE.translate(locale, args[0], USER.translate(locale)))
					.color(RED))),
	COMMAND_ENABLE_TELL_USAGE((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(getArgument(args, 0).orElseGet(() -> COMMAND_ENABLE_TELL_NAME.translate(locale))))
					.color(RED))),
	COMMAND_ENABLE_TELL_USE((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendSpace()
					.append(COMMAND_ENABLE_TELL_USAGE.translate(locale, args[0]))
					.color(RED))),
	COMMAND_DISABLE_TELL_USAGE((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(getArgument(args, 0).orElseGet(() -> COMMAND_DISABLE_TELL_NAME.translate(locale))))
					.color(RED))),
	COMMAND_DISABLE_TELL_USE((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendSpace()
					.append(COMMAND_DISABLE_TELL_USAGE.translate(locale, args[0]))
					.color(RED))),
	COMMAND_ENABLE_CHAT_USAGE((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(getArgument(args, 0).orElseGet(() -> COMMAND_ENABLE_CHAT_NAME.translate(locale))))
					.color(RED))),
	COMMAND_ENABLE_CHAT_USE((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendSpace()
					.append(COMMAND_ENABLE_CHAT_USAGE.translate(locale, args[0]))
					.color(RED))),
	COMMAND_DISABLE_CHAT_USAGE((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(getArgument(args, 0).orElseGet(() -> COMMAND_DISABLE_CHAT_NAME.translate(locale))))
					.color(RED))),
	COMMAND_DISABLE_CHAT_USE((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendSpace()
					.append(COMMAND_DISABLE_CHAT_USAGE.translate(locale, args[0]))
					.color(RED))),
	COMMAND_UNMUTE_USAGE((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(args[0]))
					.appendSpace()
					.append(usageRequiredArg(USER.translate(locale)))
					.color(RED))),
	COMMAND_UNMUTE_USAGE_$ALIASES_$PLAYER((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(args[0]))
					.appendSpace()
					.append(argToComponent(args[1]))
					.color(RED))),
	COMMAND_UNMUTE_USE((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendNewline()
					.append(COMMAND_UNMUTE_USAGE.translate(locale, args[0]))
					.color(RED))),
	COMMAND_TELL_USAGE((locale, args) -> Collections
			.singletonList(text("/")
					.append(COMMAND_TELL_NAME.translate(locale))
					.appendSpace()
					.append(usageRequiredArg(PLAYER.translate(locale)))
					.appendSpace()
					.append(usageRequiredVarArgs(MESSAGE.translate(locale)))
					.color(RED))),
	COMMAND_TELL_USAGE_$ALIASES((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(args[0]))
					.appendSpace()
					.append(usageRequiredArg(PLAYER.translate(locale)))
					.appendSpace()
					.append(usageRequiredVarArgs(MESSAGE.translate(locale)))
					.color(RED))),
	COMMAND_TELL_USE_$ALIASES((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendNewline()
					.append(COMMAND_TELL_USAGE_$ALIASES.translate(locale, args[0]))
					.color(RED))),
	COMMAND_TELL_USAGE_$ALIASES_$PLAYER_NAME((locale, args) -> Collections
			.singletonList(text("/")
					.append(argToComponent(args[0]))
					.appendSpace()
					.append(argToComponent(args[1]))
					.appendSpace()
					.append(usageRequiredVarArgs(MESSAGE.translate(locale)))
					.color(RED))),
	COMMAND_TELL_USE_$ALIASES_$PLAYER_NAME((locale, args) -> Collections
			.singletonList(USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
					.appendNewline()
					.append(COMMAND_TELL_USAGE_$ALIASES_$PLAYER_NAME.translate(locale, args[0], args[1]))
					.color(RED))),
	
	COMMAND_TELL_$SENDER_$RECEIVER_$MESSAGE((locale, args) -> Collections.singletonList(text("[")
			.append(displayName(args[0]))
			.appendSpace()
			.append(text("»"))
			.appendSpace()
			.append(displayName(args[1]))
			.append(text("]"))
			.append(space().decorate(BOLD))
			.append(text(args[2].toString()).color(GRAY).decorate(ITALIC))
			.applyFallbackStyle(GRAY))),
	
	COMMAND_TELL_BUSY_$PLAYER("command.tell.busy", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		playerName(args[0]),
	}),
	COMMAND_TELL_YOURSELF("command.tell.yourself", empty().color(RED)),
	COMMAND_TELL_CAN_NOT_TELL_IGNORED_PLAYER("command.tell.can-not-tell-ignored-player", empty().color(RED)),
	COMMAND_TELL_YOU_DISABLED_TELL("command.tell.you-disabled-tell", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_ENABLE_TELL_USAGE.translate(bundle),
	}),
	
	COMMAND_REPLY_NAME("command.reply.name"),
	COMMAND_REPLY_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
		.append(argToComponent(args[0]))
		.appendSpace()
		.append(usageRequiredVarArgs(MESSAGE.translate(locale)))
	)),
	COMMAND_REPLY_NO_CHATTING_$ALIASES("command.reply.no-chatting", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_TELL_USAGE.translate(bundle, args[0]),
	}),
	COMMAND_REPLY_GONE_OFFLINE_$PLAYER("command.reply.gone-offline", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		playerName(args[0]),
	}),
	
	COMMAND_DISABLE_TELL_ALREADY_DISABLED("command.disable-tell.already-disabled", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_ENABLE_TELL_USAGE.translate(bundle),
	}),
	COMMAND_DISABLE_TELL_DISABLED("command.disable-tell.disabled", empty().color(GREEN)),
	COMMAND_DISABLE_TELL_THIS_PLAYER_DISABLED_TELL("command.disable-tell.this-player-disabled-tell", empty().color(RED)),
	
	COMMAND_ENABLED_TELL_ALREADY_ENABLED("command.enable-tell.already-enabled", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_DISABLE_TELL_USAGE.translate(bundle),
	}),
	COMMAND_ENABLED_TELL_ENABLED("command.enable-tell.enabled", empty().color(GREEN)),
	
	COMMAND_DISABLE_CHAT_ALREADY_DISABLED("command.disable-chat.already-disabled", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_ENABLE_CHAT_USAGE.translate(bundle),
	}),
	COMMAND_DISABLE_CHAT_DISABLED("command.disable-chat.disabled", empty().color(GREEN)),
	
	COMMAND_ENABLE_CHAT_ALREADY_ENABLED("command.enable-chat.already-enabled", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_DISABLE_CHAT_USAGE.translate(bundle),
	}),
	COMMAND_ENABLE_CHAT_ENABLED("command.enable-chat.enabled", empty().color(GREEN)),
	
	EXCEPTION_GROUP_NOT_FOUND("exception.group-not-found", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
	}),
	
	EXCEPTION_GROUP_SELF_ADD_EXCEPTION("exception.group-self-add", empty().color(RED)),
	
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
	
	EXCEPTION_PLAYER_NOT_FOUND("exception.player-not-found", (bundle, args) -> new Component[]
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
	
	EXCEPTION_GROUP_NOT_FOUND_IN_GROUP("exception.group-not-found-in-group", (bundle, args) -> new Component[]
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
	
	EXCEPTION_USER_ALREADY_MUTED("exception.user-already-muted", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
		COMMAND_UNMUTE_USAGE_$ALIASES_$PLAYER.translate(bundle, COMMAND_UNMUTE_NAME.translate(bundle), args[0]),
	}),
	
	EXCEPTION_USER_IS_NOT_MUTED("exception.user-is-not-muted", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
	}),
	
	EXCEPTION_MAX_MUTE_REACHED("exception.max-mute-reached", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
	}),
	
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
	
	COMMAND_PEX_GROUP_ADDED_TO_GROUP("command.pex.group-added-to-group", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" +  args[0] + "\""),
		text("\"" +  args[1] + "\""),
	}),
	
	COMMAND_PEX_GROUP_REMOVED_FROM_GROUP("command.pex.group-removed-from-group", (bundle, args) -> new Component[]
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
		Collection<Group> groups = (Collection<Group>) args[0];
		
		Component groupsComponent = Component.empty().color(YELLOW);
		Iterator<Group> iterator = groups.iterator();
		
		while(iterator.hasNext())
		{
			final Group group = iterator.next();
			final String groupName = group.getName();
			final Component groupComponent = text(groupName);
			
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
	COMMAND_PEX_RUN_COMMAND_GROUP_GROUP_LIST((locale, args) -> Collections.singletonList(
			text("/" + PexCommand.COMMAND_NAME + " group " + args[0] + " group list " + args[1]))),
	COMMAND_PEX_RUN_COMMAND_USER_PERM_LIST((locale, args) -> Collections.singletonList(
			text("/" + PexCommand.COMMAND_NAME + " user " + args[0] + " perm list " + args[1]))),
	
	COMMAND_PEX_USAGE((locale, args) ->
	{
		List<Component> components = new ArrayList<>();
		
		TextColor baseColor = RED;
		
		components.add(USAGE.translate(locale, word -> WordUtils.capitalize(word + ":")).color(baseColor));
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
		Component title = ((MessageArgs) args[1]).translate(locale);
		MessageArgs runCommand = (MessageArgs) args[2];
		
		List<Component> components = new ArrayList<>();
		
		HoverEvent<Component> clickToNavigate = CLICK_TO_NAVIGATE.translate(locale).asHoverEvent();
		
		ClickEvent runPreviousPage = ClickEvent.runCommand(runCommand.moreArgs(page.getDisplayIndex() - 1).translatePlain(locale));
		ClickEvent runNextPage = ClickEvent.runCommand(runCommand.moreArgs(page.getDisplayIndex() + 1).translatePlain(locale));
		
		Component previousPage = page.hasPreviousPage()
				? text("<<<")
						.color(GOLD)
						.clickEvent(runPreviousPage)
						.hoverEvent(clickToNavigate)
						.append(text(" "))
				: Component.empty();
		
		Component nextPage = page.hasNextPage()
				? text(" ")
						.append(text(">>>")
						.color(GOLD)
						.clickEvent(runNextPage)
						.hoverEvent(clickToNavigate))
				: Component.empty();
		
		Component pageIndex = PAGE_INDEX.translate(locale, page.getDisplayIndex(), page.getAmountOfPages());
		
		Component leftTraces = text("============ ").color(GRAY);
		Component rightTraces = text(" ============").color(GRAY);
		
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
		Component broadcast = BROADCAST.translate(locale, WordUtils::capitalize).color(AQUA).decorate(BOLD);
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
	
	COMMAND_CHAT_CHAT_ENABLED("command.chat.chat-enabled", empty().color(GREEN)),
	COMMAND_CHAT_CHAT_DISABLED("command.chat.chat-disabled", empty().color(GREEN)),
	COMMAND_CHAT_CHAT_ALREADY_ENABLED("command.chat.chat-already-enabled", empty().color(RED)),
	COMMAND_CHAT_CHAT_ALREADY_DISABLED("command.chat.chat-already-disabled", empty().color(RED)),
	COMMAND_CHAT_CHAT_IS_DISABLED("command.chat.chat-is-disabled", empty().color(RED)),
	
	COMMAND_DISABLE_CHAT_CHAT_IS_DISABLED("command.disable-chat.chat-is-disabled", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		COMMAND_ENABLE_CHAT_USAGE.translate(bundle),
	}),
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
