package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.StructureTypeEnum;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DislikeDepositAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class DislikeDepositRuleSerializer extends DepositRuleSerializer
{
	public DislikeDepositRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<DislikeDepositAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(
				loadConfig(plugin.getDataFileManager().getRuleFile(StructureTypeEnum.DISLIKE_DEPOSIT)),
				DislikeDepositAttribute.DEFAULT,
				this::deserializeAttribute);
	}
	
	public DislikeDepositAttribute deserializeAttribute(ConfigurationSection section)
	{
		DislikeDepositAttribute dislikeDepositAttribute = new DislikeDepositAttribute();
		readExternal(dislikeDepositAttribute, section);
		return dislikeDepositAttribute;
	}
	
	private void readExternal(DislikeDepositAttribute dislikeDepositAttribute, ConfigurationSection section)
	{
		super.readExternal(dislikeDepositAttribute, section);
	}
}
