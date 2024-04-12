package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DislikeDepositAttribute extends DepositAttribute
{
	public static final DislikeDepositAttribute DEFAULT = new DislikeDepositAttribute();
}
