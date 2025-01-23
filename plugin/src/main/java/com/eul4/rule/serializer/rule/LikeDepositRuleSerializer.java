package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DepositAttribute;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.rule.attribute.LikeDepositAttribute;
import com.eul4.rule.serializer.data.DepositAttributeDataSerializer;
import com.eul4.rule.serializer.data.GenericAttributeDataSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class LikeDepositRuleSerializer extends GenericRuleSerializer
{
	public LikeDepositRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<LikeDepositAttribute> load() throws FileNotFoundException
	{
		return deserializeRule
		(
			loadConfig(plugin.getDataFileManager().getRuleFile(StructureType.LIKE_DEPOSIT)),
			LikeDepositAttribute.DEFAULT,
			this::deserializeAttribute
		);
	}
	
	public LikeDepositAttribute deserializeAttribute(ConfigurationSection section)
	{
		GenericAttribute.Data genericAttributeData = GenericAttributeDataSerializer.deserialize(section);
		DepositAttribute.Data<Integer> depositAttributeData = DepositAttributeDataSerializer.deserialize(section, Integer.class);
		
		return new LikeDepositAttribute(genericAttributeData, depositAttributeData);
	}
}
