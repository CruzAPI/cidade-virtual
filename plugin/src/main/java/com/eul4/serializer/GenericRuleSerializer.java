package com.eul4.serializer;

import com.eul4.Price;
import com.eul4.rule.GenericAttribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.Objects;

public class GenericRuleSerializer
{
	public static void setGenericAttributes(GenericAttribute genericAttribute, ConfigurationSection section)
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
