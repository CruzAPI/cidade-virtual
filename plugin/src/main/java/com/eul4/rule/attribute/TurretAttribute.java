package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TurretAttribute extends GenericAttribute
{
	public static final TurretAttribute DEFAULT = new TurretAttribute();
	
	private double attackDamage;
	private int attackSpeed = Integer.MAX_VALUE;
	private double missileSpeed;
	private double range;
	
	public double getAttackSpeedPerSecond()
	{
		return attackSpeed == 0 ? Double.POSITIVE_INFINITY : 20.0D / attackSpeed;
	}
	
	public double getMissileSpeedPerSecond()
	{
		return missileSpeed * 20.0D;
	}
}
