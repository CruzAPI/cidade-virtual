package com.eul4.plugin2.command;

import com.eul4.model.player.Fighter;
import com.eul4.plugin2.Main;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
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
public class SethomeCommand implements TabExecutor {
    private final Main plugin;

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String alias,
                             @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (isFighter(player)) {
            player.sendMessage(Component.text("Voc\u00ea n\u00e3o pode usar esse comando porque est\u00e1 no mundo da Cidade Virtual. Digite /spawn para voltar.", NamedTextColor.RED));
            return true;
        }

        int maxHomes = 3;
        String playerUUID = player.getUniqueId().toString();
        String homeName = (args.length > 0) ? args[0] : "default";
        String pathPrefix = "homes." + playerUUID;

        if (plugin.getConfig().getConfigurationSection(pathPrefix) == null) {
            plugin.getConfig().createSection(pathPrefix);
        }

        Set<String> playerHomes = plugin.getConfig().getConfigurationSection(pathPrefix).getKeys(false);

        if (playerHomes.size() >= maxHomes && !playerHomes.contains(homeName)) {
            player.sendMessage(Component.text("Voc\u00ea j\u00e1 ultrapassou o seu limite de casas!", NamedTextColor.RED));
            player.sendMessage(Component.text("Para adicionar uma nova, apague uma de suas casas com /delhome", NamedTextColor.RED));
            return true;
        }

        Location location = player.getLocation();
        String path = pathPrefix + "." + homeName;

        plugin.getConfig().set(path + ".world", location.getWorld().getName());
        plugin.getConfig().set(path + ".x", location.getX());
        plugin.getConfig().set(path + ".y", location.getY());
        plugin.getConfig().set(path + ".z", location.getZ());
        plugin.saveConfig();

        player.sendMessage(Component.text("Sua casa foi adicionada!", NamedTextColor.GREEN));

        if (homeName.equals("default")) {
            player.sendMessage(
                    Component.text("Para se teletransportar at\u00e9 sua casa, digite o comando ", NamedTextColor.GRAY)
                            .append(Component.text("/home", NamedTextColor.WHITE))
            );
        } else {
            player.sendMessage(
                    Component.text("Para se teletransportar at\u00e9 sua casa, digite o comando ", NamedTextColor.GRAY)
                            .append(Component.text("/home ", NamedTextColor.WHITE))
                            .append(Component.text(homeName, NamedTextColor.WHITE))
            );
        }

        return true;
    }

    private boolean isFighter(Player player) {
        return player.getWorld().getName().equals("town_world");
    }
}
