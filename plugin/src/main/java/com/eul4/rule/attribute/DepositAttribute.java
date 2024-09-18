package com.eul4.rule.attribute;

import com.eul4.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class DepositAttribute<N extends Number> extends GenericAttribute
{
	private final @NotNull N capacity;
	
	protected DepositAttribute
	(
		GenericAttribute.Data genericAttributeData,
		DepositAttribute.Data<N> depositAttributeData
	)
	{
		super(genericAttributeData);
		
		this.capacity = depositAttributeData.capacity;
	}
	
	public abstract Currency getCurrency();
	
	@lombok.Data
	public static class Data<N>
	{
		private final N capacity;
	}
}
