package com.eul4.common.i18n;

import com.eul4.common.command.PexCommand;
import com.eul4.common.model.permission.Group;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

@Getter
public enum CommonMessage implements Message
{
	ADMINISTRATOR("administrator"),
	PLAYER("player"),
	SPECTATOR("spectator"),
	BROADCAST("broadcast"),
	USAGE("usage"),

	ONLINE("online"),
	OFFLINE("offline"),

	GAME_MODE_CHANGED("player-mode-changed")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component mode = ((CommonMessage) args[1]).translate(locale, String::toUpperCase).color((TextColor) args[0]);
			
			return new TagResolver[]
			{
				Placeholder.component("mode", mode),
			};
		}
	},

	COMMAND_BUILD_ENABLED("command.build.enabled"),
	COMMAND_BUILD_DISABLED("command.build.disabled"),
	COMMAND_BUILD_NEED_ADMIN("command.build.need-admin"),
	YOU_DO_NOT_HAVE_PERMISSION("you-do-not-have-permission"),
	PERCENTAGE("percentage")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			DecimalFormat formatter = new DecimalFormat(args[0].toString(), new DecimalFormatSymbols(locale));
			Component percentage = text(formatter.format((double) args[1]).concat("%"));
			
			return new TagResolver[]
			{
				Placeholder.component("percentage", percentage),
			};
		}
	},
	
	DAYS_CHAR("days-char"),
	HOURS_CHAR("hours-char"),
	MINUTES_CHAR("minutes-char"),
	SECONDS_CHAR("seconds-char"),

	CONFIRM("confirm"),
	CANCEL("cancel"),
	CAN_NOT_PERFORM("can-not-perform"),
	SCOREBOARD_ENABLED("command.scoreboard.enabled"),
	SCOREBOARD_DISABLED("command.scoreboard.disabled"),

	EXCEPTION_GROUP_NOT_FOUND("exception.group-not-found")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component group = text("\"" + args[0] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("group", group),
			};
		}
	},

	EXCEPTION_GROUP_ALREADY_EXISTS("exception.group-already-exists")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component group = text("\"" + args[0] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("group", group),
			};
		}
	},
	
	EXCEPTION_USER_NOT_FOUND("exception.user-not-found")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component user = text("\"" + args[0] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("user", user),
			};
		}
	},

	EXCEPTION_USER_NOT_FOUND_IN_GROUP("exception.user-not-found-in-group")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component user = text("\"" + args[0] + "\"");
			Component group = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("user", user),
				Placeholder.component("group", group),
			};
		}
	},

	EXCEPTION_PERM_NOT_FOUND_IN_GROUP("exception.perm-not-found-in-group")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component perm = text("\"" + args[0] + "\"");
			Component group = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("perm", perm),
				Placeholder.component("group", group),
			};
		}
	},

	EXCEPTION_PERM_NOT_FOUND_IN_USER("exception.perm-not-found-in-user")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component perm = text("\"" + args[0] + "\"");
			Component user = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("perm", perm),
				Placeholder.component("user", user),
			};
		}
	},

	EXCEPTION_NUMBER_FORMAT("exception.number-format"),
	EXCEPTION_PAGE_NOT_FOUND("exception.page-not-found"),
	EXCEPTION_EMPTY_LIST("exception.empty-list"),

	COMMAND_PEX_USER_ADDED_TO_GROUP("command.pex.user-added-to-group")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component user = text("\"" + args[0] + "\"");
			Component group = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("user", user),
				Placeholder.component("group", group),
			};
		}
	},

	COMMAND_PEX_USER_REMOVED_FROM_GROUP("command.pex.user-removed-from-group")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component user = text("\"" + args[0] + "\"");
			Component group = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("user", user),
				Placeholder.component("group", group),
			};
		}
	},

	COMMAND_PEX_PERM_ADDED_TO_GROUP("command.pex.perm-added-to-group")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component perm = text("\"" + args[0] + "\"");
			Component group = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("perm", perm),
				Placeholder.component("group", group),
			};
		}
	},

	COMMAND_PEX_PERM_REMOVED_FROM_GROUP("command.pex.perm-removed-from-group")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component perm = text("\"" + args[0] + "\"");
			Component group = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("perm", perm),
				Placeholder.component("group", group),
			};
		}
	},
	
	COMMAND_PEX_PERM_ADDED_TO_USER("command.pex.perm-added-to-user")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component perm = text("\"" + args[0] + "\"");
			Component user = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("perm", perm),
				Placeholder.component("user", user),
			};
		}
	},
	
	COMMAND_PEX_PERM_REMOVED_FROM_USER("command.pex.perm-removed-from-user")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component perm = text("\"" + args[0] + "\"");
			Component user = text("\"" + args[1] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("perm", perm),
				Placeholder.component("user", user),
			};
		}
	},
	
	COMMAND_PEX_GROUP_CREATED("command.pex.group-created")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component group = text("\"" + args[0] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("group", group),
			};
		}
	},
	
	COMMAND_PEX_GROUP_DELETED("command.pex.group-deleted")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component group = text("\"" + args[0] + "\"");
			
			return new TagResolver[]
			{
				Placeholder.component("group", group),
			};
		}
	},

	COMMAND_PEX_LIST_GROUPS("command.pex.list-groups")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
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
			
			return new TagResolver[]
			{
				Placeholder.component("groups", groupsComponent),
			};
		}
	},
	
	PAGE_INDEX("page-index")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			Component currentPage = text((int) args[0]);
			Component amountOfPage = text((int) args[1]);
			
			return new TagResolver[]
			{
				Placeholder.component("current_page", currentPage),
				Placeholder.component("number_of_pages", amountOfPage),
			};
		}
	},
	
	CLICK_TO_NAVIGATE("click-to-navigate"),
	
	COMMAND_PEX_RUN_COMMAND_GROUP_USER_LIST("command.pex.run-command.group-user-list")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			return new TagResolver[]
			{
				unparsed("group", args[0].toString()),
				unparsed("index", args[1].toString()),
			};
		}
	},
	COMMAND_PEX_RUN_COMMAND_GROUP_PERM_LIST("command.pex.run-command.group-perm-list")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			return new TagResolver[]
			{
				unparsed("group", args[0].toString()),
				unparsed("index", args[1].toString()),
			};
		}
	},
	COMMAND_PEX_RUN_COMMAND_USER_PERM_LIST("command.pex.run-command.user-perm-list")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			return new TagResolver[]
			{
				unparsed("user", args[0].toString()),
				unparsed("index", args[1].toString()),
			};
		}
	},
	
	PAGE_GROUP_USER_TITLE("page.group-user.title")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			return new TagResolver[]
			{
				unparsed("number_of_users", args[0].toString()),
				unparsed("group", "(" + args[1].toString() + ")"),
			};
		}
	},
	
	PAGE_GROUP_PERM_TITLE("page.group-perm.title")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			return new TagResolver[]
			{
				unparsed("number_of_perms", args[0].toString()),
				unparsed("group", "(" + args[1].toString() + ")"),
			};
		}
	},

	PAGE_USER_PERM_TITLE("page.user-perm.title")
	{
		@Override
		public TagResolver[] translateTagResolvers(Locale locale, Object... args)
		{
			TranslationRegistry registry = TranslationRegistry.create(Key.key("myplugin"));
			
			GlobalTranslator.translator().addSource(registry);
			Component.translatable("").arguments();
			return new TagResolver[]
			{
				unparsed("number_of_perms", args[0].toString()),
				unparsed("user", "(" + args[1].toString() + ")"),
			};
		}
	},
//
//	COMMAND_PEX_USAGE((locale, args) ->
//	{
//		List<Component> components = new ArrayList<>();
//
//		TextColor baseColor = RED;
//
//		components.add(USAGE.translateOne(locale, word -> WordUtils.capitalize(word + ":")).color(baseColor));
//		components.add(text("/pex group").color(baseColor));
//		components.add(text("/pex group <group> create").color(baseColor));
//		components.add(text("/pex group <group> delete").color(baseColor));
//		components.add(text("/pex group <group> perm add <perm> [ticks]").color(baseColor));
//		components.add(text("/pex group <group> perm remove <perm>").color(baseColor));
//		components.add(text("/pex group <group> user add <user> [ticks]").color(baseColor));
//		components.add(text("/pex group <group> user remove <user>").color(baseColor));
//		components.add(text("/pex group <group> user list").color(baseColor));
//		components.add(text("/pex user <user> perm add <perm> [ticks]").color(baseColor));
//		components.add(text("/pex user <user> perm remove <perm>").color(baseColor));
//		components.add(text("/pex user <user> perm list").color(baseColor));
//
//		return components;
//	}),
//
//	SHOW_PAGE((locale, args) ->
//	{
//		Page<?> page = (Page<?>) args[0];
//		Component title = ((MessageArgs) args[1]).translateOne(locale);
//		MessageArgs runCommand = (MessageArgs) args[2];
//
//		List<Component> components = new ArrayList<>();
//
//		HoverEvent<Component> clickToNavigate = CLICK_TO_NAVIGATE.translateOne(locale).asHoverEvent();
//
//		ClickEvent runPreviousPage = ClickEvent.runCommand(runCommand.moreArgs(page.getDisplayIndex() - 1).translatePlain(locale));
//		ClickEvent runNextPage = ClickEvent.runCommand(runCommand.moreArgs(page.getDisplayIndex() + 1).translatePlain(locale));
//
//		Component previousPage = page.hasPreviousPage()
//				? Component.text("<<<")
//						.color(GOLD)
//						.clickEvent(runPreviousPage)
//						.hoverEvent(clickToNavigate)
//						.append(text(" "))
//				: Component.empty();
//
//		Component nextPage = page.hasNextPage()
//				? Component.text(" ")
//						.append(text(">>>")
//						.color(GOLD)
//						.clickEvent(runNextPage)
//						.hoverEvent(clickToNavigate))
//				: Component.empty();
//
//		Component pageIndex = PAGE_INDEX.translateOne(locale, page.getDisplayIndex(), page.getAmountOfPages());
//
//		Component leftTraces = Component.text("============ ").color(GRAY);
//		Component rightTraces = Component.text(" ============").color(GRAY);
//
//		Component header = leftTraces
//				.append(title)
//				.append(rightTraces);
//
//		Component footer = leftTraces
//				.append(previousPage)
//				.append(pageIndex)
//				.append(nextPage)
//				.append(rightTraces);
//
//		components.add(header);
//		components.addAll(page.getPageComponentsTranslated(locale));
//		components.add(footer);
//
//		return components;
//	}),
//
//	COMMAND_BROADCAST((locale, args) ->
//	{
//		Component broadcast = BROADCAST.translateOne(locale, WordUtils::capitalize).color(AQUA).decorate(BOLD);
//		Component colon = text("»").color(GRAY);
//		Component message = (Component) args[0];
//
//		Component component = empty()
//				.append(broadcast)
//				.appendSpace()
//				.append(colon)
//				.appendSpace()
//				.append(message);
//
//		return Collections.singletonList(component);
//	}),
//
//	COMMAND_BROADCAST_USAGE((locale, args) -> Collections
//			.singletonList(Component.text("/" + BroadcastCommand.COMMAND_NAME + " <msg...>").color(RED))),
//	COMMAND_CLEAR_CHAT_USAGE((locale, args) -> Collections
//			.singletonList(Component.text("/" + BroadcastCommand.COMMAND_NAME + " <msg...>").color(RED))),
	;
	
	private final String key;
	
	CommonMessage(String key)
	{
		this.key = key;
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return CommonBundleBaseName.COMMON;
	}
}
