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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class AceitarTpaCommand implements TabExecutor {
    private final Main plugin;
    private final TpaCommand tpaCommand;

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
            player.sendMessage(Component.text("Uso correto: /aceitartpa <nome_do_jogador>", NamedTextColor.RED));
            return true;
        }

        Player requester = Bukkit.getPlayer(args[0]);
        if (requester == null || !requester.isOnline()) {
            player.sendMessage(Component.text("Jogador n\u00e3o encontrado ou offline!", NamedTextColor.RED));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        UUID requesterUUID = requester.getUniqueId();

        Map<UUID, Map<UUID, Long>> tpaRequests = tpaCommand.getTpaRequests();
        if (!tpaRequests.containsKey(playerUUID) || !tpaRequests.get(playerUUID).containsKey(requesterUUID)) {
            player.sendMessage(Component.text("Nenhuma solicita\u00e7\u00e3o de teleporte encontrada de " + requester.getName(), NamedTextColor.RED));
            return true;
        }

        // Aceitar o TPA
        requester.teleport(player.getLocation());
        player.sendMessage(Component.text("Voc\u00ea aceitou a solicita\u00e7\u00e3o de teleporte de " + requester.getName(), NamedTextColor.GREEN));
        requester.sendMessage(Component.text("Sua solicita\u00e7\u00e3o de teleporte para " + player.getName() + " foi aceita!", NamedTextColor.GREEN));

        // Remover a solicita\u00e7\u00e3o aceita
        tpaRequests.get(playerUUID).remove(requesterUUID);
        if (tpaRequests.get(playerUUID).isEmpty()) {
            tpaRequests.remove(playerUUID);
        }

        return true;
    }

    private boolean isFighter(Player player) {
        return player.getWorld().getName().equals("town_world");
    }
}
