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

@RequiredArgsConstructor
public class DelHomeCommand implements TabExecutor
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

        String homeName = (args.length > 0) ? args[0] : "default";
        String playerUUID = player.getUniqueId().toString();
        String path = "homes." + playerUUID + "." + homeName;

        if (plugin.getConfig().contains(path))
        {
            plugin.getConfig().set(path, null);
            plugin.saveConfig();
            player.sendMessage(Component.text("Casa " + homeName + " deletada com sucesso.", NamedTextColor.GREEN));
        }
        else
        {
            player.sendMessage(Component.text("A casa " + homeName + " não foi encontrada.", NamedTextColor.RED));
        }

        return true;
    }
}
