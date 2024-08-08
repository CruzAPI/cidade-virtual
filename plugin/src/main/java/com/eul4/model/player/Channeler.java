package com.eul4.model.player;

import com.eul4.wrapper.ChannelingTask;

import java.util.Optional;

public interface Channeler extends PluginPlayer
{
	void channel(long ticks, Runnable runnable);
	ChannelingTask getChannelingTask();
	
	default Optional<ChannelingTask> findChannelingTask()
	{
		return Optional.ofNullable(getChannelingTask());
	}
}
