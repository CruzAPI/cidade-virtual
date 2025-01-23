package com.eul4.rule.serializer.data;

import com.eul4.rule.attribute.TurretAttribute;
import org.bukkit.configuration.ConfigurationSection;

public class TurretAttributeDataSerializer
{
	public static TurretAttribute.Data deserialize(ConfigurationSection section)
	{
		final double attackDamage = section.getDouble("attack_damage", 1.0D);
		final int attackSpeed = section.getInt("attack_speed", 100);
		final double missileSpeed = section.getDouble("missile_speed", 0.1D);
		final double range = section.getDouble("range", 5.0D);
		
		return new TurretAttribute.Data(attackDamage, attackSpeed, missileSpeed, range);
	}
}
