package com.eul4.rule.attribute;

import com.eul4.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class CrownDepositAttribute extends DepositAttribute<BigDecimal>
{
	public static final CrownDepositAttribute DEFAULT = new CrownDepositAttribute
	(
		GenericAttribute.Data.DEFAULT,
		new DepositAttribute.Data<>(BigDecimal.ZERO)
	);
	
	public CrownDepositAttribute
	(
		GenericAttribute.Data genericAttributeData,
		DepositAttribute.Data<BigDecimal> depositAttributeData
	)
	{
		super(genericAttributeData, depositAttributeData);
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.CROWN;
	}
}
