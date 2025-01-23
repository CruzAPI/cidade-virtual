package com.eul4.rule.serializer.data;

import com.eul4.StructureType;
import com.eul4.rule.attribute.TownHallAttribute;
import com.sk89q.worldedit.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TownHallAttributeDataSerializer
{
	public static TownHallAttribute.Data deserialize(ConfigurationSection section)
	{
		final Map<StructureType, Integer> structureLimit = getStructureLimit(section.getConfigurationSection("structure_limit"));
		final int likeCapacity = section.getInt("like_capacity");
		final int dislikeCapacity = section.getInt("dislike_capacity");
		final BigDecimal crownCapacity = new BigDecimal(section.getString("crown_capacity", "0"));
		final Vector3 spawnPosition = Optional.ofNullable(getVector3(section.getConfigurationSection("spawn_position")))
				.orElse(Vector3.ZERO);
		
		return new TownHallAttribute.Data(structureLimit, likeCapacity, dislikeCapacity, crownCapacity, spawnPosition);
	}
	
	private static Vector3 getVector3(ConfigurationSection vector3Section)
	{
		if(vector3Section == null)
		{
			return null;
		}
		
		double x = vector3Section.getDouble("x");
		double y = vector3Section.getDouble("y");
		double z = vector3Section.getDouble("z");
		
		return Vector3.at(x, y , z);
	}
	
	private static Map<StructureType, Integer> getStructureLimit(ConfigurationSection structureLimitSection)
	{
		Map<StructureType, Integer> structureLimit = new HashMap<>();
		
		if(structureLimitSection != null)
		{
			for(String key : structureLimitSection.getKeys(false))
			{
				StructureType structureType = StructureType.valueOf(key);
				int limit = structureLimitSection.getInt(key);
				
				structureLimit.put(structureType, limit);
			}
		}
		
		return structureLimit;
	}
}
