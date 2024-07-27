package com.eul4.plugin2.command;

import com.eul4.model.player.Fighter;
import com.eul4.plugin2.Main;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor
public class TpaCommand implements TabExecutor {
    private final Main plugin;
    private final Map<UUID, Map<UUID, Long>> tpaRequests = new HashMap<>();

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

        if (args.length == 0) {
            player.sendMessage(Component.text("Uso correto: /tpa <nome_do_amigo>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Jogador n\u00e3o encontrado ou offline!", NamedTextColor.RED));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        tpaRequests.computeIfAbsent(targetUUID, k -> new HashMap<>()).put(playerUUID, System.currentTimeMillis() + 60000);

        target.sendMessage(
                Component.text(player.getName(), NamedTextColor.GREEN)
                        .append(Component.text(" enviou uma solicita\u00e7\u00e3o de teleporte para voc\u00ea. Use ", NamedTextColor.YELLOW))
                        .append(Component.text("/aceitartpa " + player.getName(), NamedTextColor.GREEN))
                        .append(Component.text(" para aceitar. (expira em 60 segundos)", NamedTextColor.YELLOW))
        );

        player.sendMessage(Component.text("Solicita\u00e7\u00e3o de teleporte enviada para " + target.getName(), NamedTextColor.GREEN));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (tpaRequests.containsKey(targetUUID) && tpaRequests.get(targetUUID).containsKey(playerUUID)) {
                    tpaRequests.get(targetUUID).remove(playerUUID);
                    if (tpaRequests.get(targetUUID).isEmpty()) {
                        tpaRequests.remove(targetUUID);
                    }
                    player.sendMessage(Component.text("Sua solicita\u00e7\u00e3o de teleporte para " + target.getName() + " expirou.", NamedTextColor.RED));
                    target.sendMessage(Component.text("A solicita\u00e7\u00e3o de teleporte de " + player.getName() + " expirou.", NamedTextColor.RED));
                }
            }
        }.runTaskLater(plugin, 1200L); // 1200 ticks = 60 seconds

        return true;
    }

    public Map<UUID, Map<UUID, Long>> getTpaRequests() {
        return tpaRequests;
    }

    private boolean isFighter(Player player) {
        return player.getWorld().getName().equals("town_world");
    }
}
