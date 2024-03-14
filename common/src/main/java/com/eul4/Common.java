package com.eul4;

import com.eul4.i18n.BundleBaseName;
import com.eul4.i18n.CommonBundleBaseName;
import com.eul4.i18n.ResourceBundleHandler;
import com.eul4.listener.PlayerManagerListener;
import com.eul4.model.player.CommonPlayer;
import com.eul4.service.PlayerManager;
import com.eul4.type.player.PlayerType;
import lombok.Getter;
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
		getServer().getPluginManager().registerEvents(new PlayerManagerListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("Commons disabled!");
	}
	
	public abstract PlayerType<? extends CommonPlayer> getDefaultPlayerType();
}