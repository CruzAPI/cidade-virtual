package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.TurretAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class TurretRuleSerializer extends GenericRuleSerializer
{
	public TurretRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<TurretAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(
				loadConfig(plugin.getDataFileManager().getRuleFile(StructureType.TURRET)),
				TurretAttribute.DEFAULT,
				this::deserializeAttribute);
	}
	
	public TurretAttribute deserializeAttribute(ConfigurationSection section)
	{
		TurretAttribute turretAttribute = new TurretAttribute();
		readExternal(turretAttribute, section);
		return turretAttribute;
	}
	
	private void readExternal(TurretAttribute turretAttribute, ConfigurationSection section)
	{
		super.readExternal(turretAttribute, section);
		
		final double attackDamage = section.getDouble("attack_damage", 1.0D);
		final int attackSpeed = section.getInt("attack_speed", 100);
		final double missileSpeed = section.getDouble("missile_speed", 0.1D);
		final double range = section.getDouble("range", 5.0D);
		
		turretAttribute.setAttackDamage(attackDamage);
		turretAttribute.setAttackSpeed(attackSpeed);
		turretAttribute.setMissileSpeed(missileSpeed);
		turretAttribute.setRange(range);
	}
}
