package com.eul4.plugin2.command;

import com.eul4.plugin2.Main;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor
public class HomeCommand implements TabExecutor, Listener
{
    private final Main plugin;
    private final Map<UUID, BukkitRunnable> teleportTasks = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private static final long COOLDOWN_TIME = 20 * 1000; // 20 seconds in milliseconds

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

        UUID playerUUID = player.getUniqueId();

        // Check for cooldown
        if (cooldowns.containsKey(playerUUID))
        {
            long timeElapsed = System.currentTimeMillis() - cooldowns.get(playerUUID);
            if (timeElapsed < COOLDOWN_TIME)
            {
                long timeLeft = (COOLDOWN_TIME - timeElapsed) / 1000;
                player.sendMessage(Component.text("Voc\u00ea precisa esperar " + timeLeft + " segundos antes de usar o comando novamente.", NamedTextColor.RED));
                return true;
            }
        }

        String homeName = (args.length > 0) ? args[0] : "default";
        String pathPrefix = "homes." + playerUUID + "." + homeName;

        if (plugin.getConfig().getConfigurationSection(pathPrefix) == null)
        {
            player.sendMessage(Component.text("Casa n\u00e3o encontrada!", NamedTextColor.RED));
            return true;
        }

        String worldName = plugin.getConfig().getString(pathPrefix + ".world");
        double x = plugin.getConfig().getDouble(pathPrefix + ".x");
        double y = plugin.getConfig().getDouble(pathPrefix + ".y");
        double z = plugin.getConfig().getDouble(pathPrefix + ".z");
        Location location = new Location(plugin.getServer().getWorld(worldName), x, y, z);

        Vector vector = player.getLocation().toVector();

        player.sendMessage(Component.text("Fique parado por 5 segundos para ser teleportado.", NamedTextColor.GREEN));

        BukkitRunnable teleportTask = new BukkitRunnable()
        {
            int countdown = 5;

            @Override
            public void run()
            {
                if (!player.getLocation().toVector().equals(vector))
                {
                    Optional.ofNullable(teleportTasks.remove(playerUUID))
                            .ifPresent(lixeira -> player.sendActionBar(Component.text("Teleporte cancelado.", NamedTextColor.RED)));
                    return;
                }
                if (countdown > 0)
                {
                    player.sendActionBar(Component.text("Teleportando em " + countdown + " segundos...", NamedTextColor.YELLOW));
                    countdown--;
                }
                else
                {
                    player.teleport(location);
                    player.sendActionBar(Component.text("Voc\u00ea foi teleportado para sua casa!", NamedTextColor.GREEN));
                    teleportTasks.remove(playerUUID);
                    cancel();
                }
            }
        };

        teleportTasks.put(playerUUID, teleportTask);
        teleportTask.runTaskTimer(plugin, 0L, 20L);

        // Set the cooldown
        cooldowns.put(playerUUID, System.currentTimeMillis());

        return true;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            UUID playerUUID = player.getUniqueId();
            if (teleportTasks.containsKey(playerUUID))
            {
                teleportTasks.get(playerUUID).cancel();
                teleportTasks.remove(playerUUID);
                player.sendActionBar(Component.text("Teleporte cancelado.", NamedTextColor.RED));
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player player)
        {
            UUID playerUUID = player.getUniqueId();
            if (teleportTasks.containsKey(playerUUID))
            {
                teleportTasks.get(playerUUID).cancel();
                teleportTasks.remove(playerUUID);
                player.sendActionBar(Component.text("Teleporte cancelado.", NamedTextColor.RED));
            }
        }

        if (event.getEntity() instanceof Player player)
        {
            UUID playerUUID = player.getUniqueId();
            if (teleportTasks.containsKey(playerUUID))
            {
                teleportTasks.get(playerUUID).cancel();
                teleportTasks.remove(playerUUID);
                player.sendActionBar(Component.text("Teleporte cancelado.", NamedTextColor.RED));
            }
        }
    }
}
