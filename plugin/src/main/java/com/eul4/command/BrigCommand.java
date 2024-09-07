package com.eul4.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BrigCommand implements BasicCommand
{
	@Override
	public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args)
	{
		commandSourceStack.getSender().sendMessage("brig api sucks!");
	}
	
	@Override
	public boolean canUse(@NotNull CommandSender sender)
	{
		return false;
	}
}
