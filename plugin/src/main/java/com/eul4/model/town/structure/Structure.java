package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotBuildYetException;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.StructureAlreadyBuiltException;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface Structure extends Externalizable
{
	UUID getUUID();
	String getName();
	
	void demolishStructureConstruction(ClipboardHolder clipboardHolder);
	
	void construct(ClipboardHolder clipboardHolder, TownBlock centerTownBlock, int rotation)
			throws CannotConstructException;
	
	ClipboardHolder loadSchematic() throws IOException;
	
	int getLevel();
	File getSchematicFile();
	StructureType getStructureType();
	
	void startMove() throws IOException, CannotConstructException;
	
	void cancelMove() throws CannotConstructException;
	
	void finishMove(TownBlock centerTownBlock, int rotation) throws CannotConstructException;
	
	ItemStack getItem();
	
	void finishMove(TownBlock centerTownBlock) throws CannotConstructException;
	Location getLocation();
	
	void construct(ClipboardHolder movingStructureClipboardHolder) throws CannotConstructException;
	
	void load();
	
	Town getTown();
	
	StructureGui newGui(CommonPlayer commonPlayer);
	
	void finishBuild()
			throws StructureAlreadyBuiltException, CannotBuildYetException, IOException, CannotConstructException;
	
	
	boolean canFinishBuild();
	int getBuildTicks();
	int getTotalBuildTicks();
	
	StructureStatus getStatus();
	
	Hologram getHologram();
	Vector getHologramRelativePosition();
	void teleportHologram();
	
	Component getProgressBarComponent();
}
