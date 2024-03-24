package com.eul4.common;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.CommonBundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.listener.CommonAdminListener;
import com.eul4.common.listener.CommonPlayerListener;
import com.eul4.common.listener.GuiListener;
import com.eul4.common.listener.PlayerManagerListener;
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
	
	@Override
	public void onEnable()
	{
		playerManager = new PlayerManager(this);
		
		registerCommonResourceBundles();
		registerListeners();
		
		getLogger().info("Commons enabled!");
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
		
		pluginManager.registerEvents(new CommonAdminListener(this), this);
		pluginManager.registerEvents(new CommonPlayerListener(this), this);
		pluginManager.registerEvents(new PlayerManagerListener(this), this);
		pluginManager.registerEvents(new GuiListener(this), this);
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