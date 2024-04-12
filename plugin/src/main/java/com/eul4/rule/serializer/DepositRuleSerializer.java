package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.rule.attribute.DepositAttribute;
import org.bukkit.configuration.ConfigurationSection;

public abstract class DepositRuleSerializer extends GenericRuleSerializer
{
	public DepositRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public void readExternal(DepositAttribute deposit, ConfigurationSection section)
	{
		super.readExternal(deposit, section);
		
		final int capacity = section.getInt("capacity");
		
		deposit.setCapacity(capacity);
	}
}
