package com.eul4.rule.serializer.data;

import com.eul4.Price;
import com.eul4.rule.attribute.GenericAttribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public class GenericAttributeDataSerializer
{
	public static GenericAttribute.Data deserialize(ConfigurationSection section)
	{
		final Price price = getPrice(section.getConfigurationSection("price"));
		final double hp = section.getDouble("hp", 500.0D);
		final int requiresTownHallLevel = section.getInt("requires_town_hall_level");
		final int totalBuildTicks = section.getInt("total_build_ticks");
		final Vector hologramVector = getVector(section.getConfigurationSection("hologram_pos"));
		
		return new GenericAttribute.Data(price, hp, requiresTownHallLevel, totalBuildTicks, hologramVector);
	}
	
	private static Price getPrice(ConfigurationSection priceSection)
	{
		if(priceSection == null)
		{
			return null;
		}
		
		final int like = priceSection.getInt("like");
		final int dislike = priceSection.getInt("dislike");
		
		return new Price(like, dislike);
	}
	
	private static Vector getVector(ConfigurationSection vectorSection)
	{
		if(vectorSection == null)
		{
			return null;
		}
		
		final double x = vectorSection.getDouble("x");
		final double y = vectorSection.getDouble("y");
		final double z = vectorSection.getDouble("z");
		
		return new Vector(x, y, z);
	}
}
