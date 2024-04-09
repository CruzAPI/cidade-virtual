package com.eul4.serializer;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.rule.GenericAttribute;
import com.eul4.rule.Rule;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class GenericRuleSerializer
{
	protected final Main plugin;
	
	public YamlConfiguration loadConfig(File file) throws FileNotFoundException
	{
		if(!file.exists() || file.length() == 0L)
		{
			throw new FileNotFoundException(file.getName() + " file not found.");
		}
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public <A extends GenericAttribute> Rule<A> deserializeRule(YamlConfiguration config,
			Function<ConfigurationSection, A> function)
	{
		Rule<A> rule = new Rule<>();
		
		for(String key : config.getKeys(false))
		{
			int level = Integer.parseInt(key);
			ConfigurationSection section = config.getConfigurationSection(String.valueOf(level));
			
			rule.setRule(level, function.apply(section));
		}
		
		return rule;
	}
	
	public void readExternal(GenericAttribute genericAttribute, ConfigurationSection section)
	{
		final int requiresTownHallLevel = section.getInt("requires_town_hall_level");
		final int totalBuildTicks = section.getInt("total_build_ticks");
		
		final double hologramX = section.getDouble("hologram_pos.x");
		final double hologramY = section.getDouble("hologram_pos.y");
		final double hologramZ = section.getDouble("hologram_pos.z");
		final Vector hologramVector = new Vector(hologramX, hologramY, hologramZ);
		
		ConfigurationSection priceSection = Objects.requireNonNull(section.getConfigurationSection("price"));
		
		final int like = priceSection.getInt("like");
		final int dislike = priceSection.getInt("dislike");
		
		Price price = new Price(like, dislike);
		
		genericAttribute.setPrice(price);
		genericAttribute.setRequiresTownHallLevel(requiresTownHallLevel);
		genericAttribute.setTotalBuildTicks(totalBuildTicks);
		genericAttribute.setHologramVector(hologramVector);
	}
}
