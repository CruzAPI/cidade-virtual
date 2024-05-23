package com.eul4.model.player;

public interface Warder extends SpiritualPlayer
{
	@Override
	default void onFinishingTownAttack()
	{
		reincarnate();
	}
}
