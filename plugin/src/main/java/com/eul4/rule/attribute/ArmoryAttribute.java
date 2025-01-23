package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ArmoryAttribute extends GenericAttribute
{
	public static final ArmoryAttribute DEFAULT = new ArmoryAttribute(GenericAttribute.Data.DEFAULT);
	
	public ArmoryAttribute(GenericAttribute.Data geneticAttributeData)
	{
		super(geneticAttributeData);
	}
}
