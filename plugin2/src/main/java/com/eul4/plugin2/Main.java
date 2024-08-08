package com.eul4.plugin2;

import com.eul4.common.Common;
import com.eul4.common.type.player.CommonWorldType;
import com.eul4.plugin2.command.SethomeCommand;
import com.eul4.plugin2.command.HomeCommand;
import com.eul4.plugin2.command.ListHomesCommand;
import com.eul4.plugin2.command.DelHomeCommand;
import com.eul4.plugin2.command.TpaCommand;
import com.eul4.plugin2.command.AceitarTpaCommand;

import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin
{

	@Override
	public void onEnable()
	{
		super.onEnable();
		saveDefaultConfig();
		// Passa a instância do plugin para os comandos
//		getCommand("sethome").setExecutor(new SethomeCommand(this));
//		getCommand("home").setExecutor(new HomeCommand(this));
//		getCommand("delhome").setExecutor(new DelHomeCommand(this));
//		getCommand("listhomes").setExecutor(new ListHomesCommand(this));

		TpaCommand tpaCommand = new TpaCommand(this);
		getCommand("tpa").setExecutor(tpaCommand);
		getCommand("aceitartpa").setExecutor(new AceitarTpaCommand(this, tpaCommand));

		// Registra o evento de teleporte cancelado
		HomeCommand homeCommand = new HomeCommand(this);
		getServer().getPluginManager().registerEvents(homeCommand, this);


	}

}
