package com.eul4.rule.attribute;

import com.eul4.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DislikeDepositAttribute extends DepositAttribute<Integer>
{
	public static final DislikeDepositAttribute DEFAULT = new DislikeDepositAttribute
	(
		GenericAttribute.Data.DEFAULT,
		new DepositAttribute.Data<>(0)
	);
	
	public DislikeDepositAttribute
	(
		GenericAttribute.Data geneticAttributeData,
		DepositAttribute.Data<Integer> depositAttributeData
	)
	{
		super(geneticAttributeData, depositAttributeData);
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.DISLIKE;
	}
}
