package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public interface Structure extends Externalizable
{
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
}
