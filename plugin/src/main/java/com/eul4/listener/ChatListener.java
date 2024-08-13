package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ComponentUtil;
import com.eul4.model.player.PluginPlayer;
import com.eul4.wrapper.Tag;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

@RequiredArgsConstructor
public class ChatListener implements Listener, ChatRenderer
{
	private final Main plugin;
	
	@EventHandler
	public void on(AsyncChatEvent event)
	{
		event.renderer(this);
	}
	
	@Override
	public @NotNull Component render(@NotNull Player player,
			@NotNull Component sourceDisplayName,
			@NotNull Component originalMessageComponent,
			@NotNull Audience viewer)
	{
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		pluginPlayer.refreshTag();
		
		Tag tag = pluginPlayer.getTag();
		
		Component tagComponent = tag == null || pluginPlayer.isTagHidden()
				? Component.empty()
				: tag.getTagComponentTranslated(pluginPlayer).appendSpace();
		Component displayNameBase = Optional.ofNullable(tag).map(Tag::getDisplayNameComponent).orElse(empty().color(GRAY));
		Component displayName = displayNameBase.append(text(player.getName()));
		Component message;
		
		
		if(pluginPlayer.hasPermission("chat.colored"))
		{
			String originalPlainMessage = ComponentUtil.toPlain(originalMessageComponent);
			message = ComponentUtil.chatInputToComponent(originalPlainMessage).color(WHITE);
		}
		else
		{
			message = originalMessageComponent.color(GRAY);
		}
		
		return tagComponent
				.append(displayName)
				.append(text(": ").color(GRAY))
				.append(message);
	}
}
