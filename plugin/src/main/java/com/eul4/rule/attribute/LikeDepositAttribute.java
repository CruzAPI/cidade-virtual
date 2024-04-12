package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class LikeDepositAttribute extends DepositAttribute
{
	public static final LikeDepositAttribute DEFAULT = new LikeDepositAttribute();
}
