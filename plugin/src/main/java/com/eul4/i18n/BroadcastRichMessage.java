package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.RichMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;

@RequiredArgsConstructor
@Getter
public enum BroadcastRichMessage implements RichMessage
{
	JOIN_OUR_DISCORD((byte) 0, "join-our-discord", (locale, args) -> new TagResolver[]
	{
		component("discord_link", Component.text("https://discord.gg/server-do-eul4-716033286190792874")
				.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/server-do-eul4-716033286190792874")))
	}),
	
	SUBSCRIBE_YOUTUBE((byte) 1, "subscribe-youtube", (locale, args) -> new TagResolver[]
	{
		component("youtube_link", Component.text("https://www.youtube.com/@eul4")
				.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://www.youtube.com/@eul4")))
	}),
	
	IGNORE_COMMAND((byte) 2, "ignore-command"),
	TRACK_COMMAND((byte) 3, "track-command"),
	AVOID_BEING_TRACKED((byte) 4, "avoid-being-tracked"),
	;
	
	private final byte id;
	private final String key;
	private final BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction;
	
	BroadcastRichMessage(byte id, String key)
	{
		this(id, key, (locale, args) -> new TagResolver[0]);
	}
	
	public static BroadcastRichMessage getByIdOrThrow(int id)
	{
		for(BroadcastRichMessage broadcastRichMessage : values())
		{
			if(broadcastRichMessage.id == id)
			{
				return broadcastRichMessage;
			}
		}
		
		throw new NoSuchElementException();
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return PluginBundleBaseName.BROADCAST_RICH_MESSAGE;
	}
}
