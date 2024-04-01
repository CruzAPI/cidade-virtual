package com.eul4.common;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.common.command.AdminCommand;
import com.eul4.common.command.BuildCommand;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.CommonBundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.interceptor.HologramTranslatorAdapter;
import com.eul4.common.interceptor.SpawnEntityInterceptor;
import com.eul4.common.listener.*;
import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.service.PlayerManager;
import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.common.type.player.CraftCommonPlayerType;
import com.eul4.common.type.player.PlayerType;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.ResourceBundle;

@Getter
public abstract class Common extends JavaPlugin
{
	private PlayerManager playerManager;
	private HologramTranslatorAdapter hologramTranslatorAdapter;
	
	private EntityRegisterListener entityRegisterListener;
	private SpawnEntityInterceptor spawnEntityInterceptor;
	
	@Override
	public void onEnable()
	{
		playerManager = new PlayerManager(this);
		
		loadServices();
		
		registerCommonResourceBundles();
		registerListeners();
		registerPacketAdapters();
		registerCommand();
		
		entityRegisterListener.loadEntities();
		
		getLogger().info("Commons enabled!");
	}
	
	private void loadServices()
	{
	
	}
	
	private void registerCommand()
	{
		getCommand("admin").setExecutor(new AdminCommand(this));
		getCommand("build").setExecutor(new BuildCommand(this));
	}
	
	private void registerPacketAdapters()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(hologramTranslatorAdapter = new HologramTranslatorAdapter(this));
		protocolManager.addPacketListener(spawnEntityInterceptor = new SpawnEntityInterceptor(this));
	}
	
	private void registerCommonResourceBundles()
	{
		for(Locale locale : new Locale[] { new Locale("pt", "BR") })
		{
			for(BundleBaseName bundleBaseName : CommonBundleBaseName.values())
			{
				ResourceBundleHandler.registerBundle(ResourceBundle.getBundle(bundleBaseName.getName(), locale));
			}
		}
	}
	
	private void registerListeners()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(entityRegisterListener = new EntityRegisterListener(this), this);
		pluginManager.registerEvents(new CommonAdminListener(this), this);
		pluginManager.registerEvents(new CommonPlayerListener(this), this);
		pluginManager.registerEvents(new PlayerManagerListener(this), this);
		pluginManager.registerEvents(new GuiListener(this), this);
		pluginManager.registerEvents(new FixInventoryVisualBugListener(this), this);
		pluginManager.registerEvents(new CancelItemDropListener(this), this);
		pluginManager.registerEvents(new CancelItemMoveListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("Commons disabled!");
	}
	
	public abstract PlayerType<? extends CommonPlayer> getDefaultPlayerType();
	
	public abstract CommonPlayerType<? extends CommonPlayer> getDefaultCommonPlayerType();
	
	public CommonPlayerType<? extends CommonAdmin> getDefaultCommonAdminPlayerType()
	{
		return CraftCommonPlayerType.COMMON_ADMIN;
	}
}