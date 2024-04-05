package com.eul4.enums;

public enum StructureStatus
{
	UNREADY, READY, BUILT;
	
	@Override
	public String toString()
	{
		return name().toLowerCase();
	}
}
