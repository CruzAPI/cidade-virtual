package com.eul4.model.player.tutorial.step;

import com.eul4.model.player.TutorialTownPlayer;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public interface Step
{
	void completeStep();
	BukkitTask runTaskTimer(@NotNull Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException;
	TutorialTownPlayer getTutorialTownPlayer();
	Villager getAssistant();
	void cancel() throws IllegalStateException;
	CheckpointStepEnum getCheckpointStep();
}
