package com.eul4.plugin2.command;

import com.eul4.plugin2.Main;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ListHomesCommand implements TabExecutor
{
    private final Main plugin;

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String alias,
                             @NotNull String[] args)
    {
        if (!(commandSender instanceof Player player))
        {
            return true;
        }

        String playerUUID = player.getUniqueId().toString();
        String pathPrefix = "homes." + playerUUID;
        Set<String> playerHomes = plugin.getConfig().getConfigurationSection(pathPrefix).getKeys(false);

        if (playerHomes.isEmpty())
        {
            player.sendMessage(Component.text("Você não tem nenhuma casa registrada.", NamedTextColor.RED));
        }
        else
        {
            player.sendMessage(Component.text("Suas casas:", NamedTextColor.GREEN));
            for (String home : playerHomes)
            {
                player.sendMessage(Component.text("- " + home, NamedTextColor.WHITE));
            }
        }

        return true;
    }
}
