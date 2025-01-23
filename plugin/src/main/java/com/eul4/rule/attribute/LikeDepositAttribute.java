package com.eul4.rule.attribute;

import com.eul4.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class LikeDepositAttribute extends DepositAttribute<Integer>
{
	public static final LikeDepositAttribute DEFAULT = new LikeDepositAttribute
	(
		GenericAttribute.Data.DEFAULT,
		new DepositAttribute.Data<>(0)
	);
	
	public LikeDepositAttribute
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
		return Currency.LIKE;
	}
}
