package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
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
public class SetHomeCommand implements TabExecutor
{
	private final Main plugin;

	// Variável para determinar se o jogador é VIP
	private static final int VIP = 1; // Defina isso conforme a lógica de verificação de VIP do seu plugin

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

		int maxHomes = (isVIP(player) ? 10 : 3);
		String playerUUID = player.getUniqueId().toString();
		String homeName = (args.length > 0) ? args[0] : "default";
		String pathPrefix = "homes." + playerUUID;

		// Garantir que a seção de configuração exista
		if (plugin.getConfig().getConfigurationSection(pathPrefix) == null)
		{
			plugin.getConfig().createSection(pathPrefix);
		}

		Set<String> playerHomes = plugin.getConfig().getConfigurationSection(pathPrefix).getKeys(false);

		if (playerHomes.size() >= maxHomes && !playerHomes.contains(homeName))
		{
			player.sendMessage("§cVocê já ultrapassou o seu limite de casas!");
			player.sendMessage("§cPara adicionar uma nova, apague uma de suas casas com §4/delhome");
			return true;
		}

		Location location = player.getLocation();
		String path = pathPrefix + "." + homeName;

		plugin.getConfig().set(path + ".world", location.getWorld().getName());
		plugin.getConfig().set(path + ".x", location.getX());
		plugin.getConfig().set(path + ".y", location.getY());
		plugin.getConfig().set(path + ".z", location.getZ());
		plugin.saveConfig();

		player.sendMessage("§aSua casa foi adicionada!");
		player.sendMessage("§aPara se teletransportar até sua casa, digite o comando §f/home " + homeName);

		return true;
	}

	private boolean isVIP(Player player)
	{
		// Aqui você deve implementar a lógica para verificar se o jogador é VIP
		// Atualmente, retorna true se VIP for 1 (apenas para demonstração)
		return VIP == 1;
	}
}
