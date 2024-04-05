package com.eul4.common.model.inventory;

public interface UpdatableGui
{
	void scheduleUpdate();
	void cancelScheduleUpdate();
	void updateInventory();
}
