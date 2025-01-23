package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TurretAttribute extends GenericAttribute
{
	public static final TurretAttribute DEFAULT = new TurretAttribute
	(
		GenericAttribute.Data.DEFAULT,
		TurretAttribute.Data.DEFAULT
	);
	
	private final double attackDamage;
	private final int attackSpeed;
	private final double missileSpeed;
	private final double range;
	
	public TurretAttribute
	(
		GenericAttribute.Data genericAttributeData,
		TurretAttribute.Data turretAttributeData
	)
	{
		super(genericAttributeData);
		
		this.attackDamage = turretAttributeData.attackDamage;
		this.attackSpeed = turretAttributeData.attackSpeed;
		this.missileSpeed = turretAttributeData.missileSpeed;
		this.range = turretAttributeData.range;
	}
	
	public double getAttackSpeedPerSecond()
	{
		return attackSpeed == 0 ? Double.POSITIVE_INFINITY : 20.0D / attackSpeed;
	}
	
	public double getMissileSpeedPerSecond()
	{
		return missileSpeed * 20.0D;
	}
	
	@lombok.Data
	public static class Data
	{
		public static final Data DEFAULT = new Data(0.0D, Integer.MAX_VALUE, 0.0D, 0.0D);
		
		private final double attackDamage;
		private final int attackSpeed;
		private final double missileSpeed;
		private final double range;
	}
}
