package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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

@RequiredArgsConstructor
public class TPACCEPTCommand implements TabExecutor
{
	private final Main plugin;
	private final HashMap<UUID, UUID> tpaRequests = new HashMap<>();

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

		if (args.length != 1)
		{
			player.sendMessage("§cUso correto: /tpa <nome do jogador>");
			return true;
		}

		Player targetPlayer = Bukkit.getPlayer(args[0]);
		if (targetPlayer == null)
		{
			player.sendMessage("§cJogador não encontrado.");
			return true;
		}

		tpaRequests.put(targetPlayer.getUniqueId(), player.getUniqueId());
		targetPlayer.sendMessage("§a" + player.getName() + " está solicitando teleporte para você. Use /tpaccept " + player.getName() + " para aceitar.");
		player.sendMessage("§aSolicitação de teleporte enviada para " + targetPlayer.getName() + ".");

		// Mensagem de debug
		player.sendMessage("§eDebug: Solicitação de teleporte registrada para " + targetPlayer.getName() + " com UUID " + targetPlayer.getUniqueId());

		return true;
	}

	public HashMap<UUID, UUID> getTpaRequests()
	{
		return tpaRequests;
	}
}
