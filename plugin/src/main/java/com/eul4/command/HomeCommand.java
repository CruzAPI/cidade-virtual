package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class HomeCommand implements TabExecutor {
	private final Main plugin;
	private final HashMap<UUID, HashMap<String, Long>> cooldowns = new HashMap<>();
	private static final long COOLDOWN_TIME = TimeUnit.MINUTES.toMillis(10); // 10 minutos em milissegundos

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

		String homeName = (args.length > 0) ? args[0] : "default";
		UUID playerUUID = player.getUniqueId();

		if (isCooldownActive(playerUUID, homeName)) {
			long remainingTime = getRemainingCooldownTime(playerUUID, homeName);
			player.sendMessage("§cVocê deve esperar mais " + formatTime(remainingTime) + " antes de usar o comando /home novamente.");
			return true;
		}

		String path = "homes." + playerUUID + "." + homeName;

		if (plugin.getConfig().contains(path)) {
			World world = Bukkit.getWorld(plugin.getConfig().getString(path + ".world"));
			double x = plugin.getConfig().getDouble(path + ".x");
			double y = plugin.getConfig().getDouble(path + ".y");
			double z = plugin.getConfig().getDouble(path + ".z");

			Location location = new Location(world, x, y, z);
			player.teleport(location);

			player.sendMessage("§aTeleportado para a sua casa " + homeName);
			setCooldown(playerUUID, homeName);
		} else {
			player.sendMessage("§cA sua casa " + homeName + " não foi encontrada.");
		}

		return true;
	}

	private boolean isCooldownActive(UUID playerUUID, String homeName) {
		return cooldowns.containsKey(playerUUID) && cooldowns.get(playerUUID).containsKey(homeName)
				&& System.currentTimeMillis() - cooldowns.get(playerUUID).get(homeName) < COOLDOWN_TIME;
	}

	private long getRemainingCooldownTime(UUID playerUUID, String homeName) {
		long lastUsed = cooldowns.get(playerUUID).get(homeName);
		return COOLDOWN_TIME - (System.currentTimeMillis() - lastUsed);
	}

	private void setCooldown(UUID playerUUID, String homeName) {
		cooldowns.putIfAbsent(playerUUID, new HashMap<>());
		cooldowns.get(playerUUID).put(homeName, System.currentTimeMillis());
	}

	private String formatTime(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%d minutos e %d segundos", minutes, seconds);
	}
}
