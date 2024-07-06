package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.*;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.Attacker;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.wrapper.TownBlockSet;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface Structure
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
	void teleportHologramToDefaultLocation();
	void teleportHologramRelative(Vector3 vector3);
	
	Component getProgressBarComponent();
	
	void upgrade()
			throws IOException, CannotConstructException,
			StructureIllegalStatusException, UpgradeLockedException, UpgradeNotFoundException;
	
	int getBuiltLevel();
	
	Rule<? extends GenericAttribute> getRule();
	
	void resetAttributes();

	void onStartAttack();
	void onFinishAttack();
	void onBuildCorruptedTown();
	void reloadAttributes();
	
	void reloadAttributesAndReloadTownAttributes();
	
	boolean hasUpgradeUnlocked();
	
	TownBlockSet getTownBlockSet();
	TownBlock getCenterTownBlock();
	int getRotation();
	
	void teleportHologram(Location location);
	
	void updateHologram();
	
	void setCenterTownBlock(TownBlock centerTownBlock);
	void setLevel(int level);
	void setRotation(int rotation);
	void setTownBlockSet(TownBlockSet townBlockSet);
	void setStatus(StructureStatus status);
	void setBuildTicks(int buildTicks);
	void setTotalBuildTicks(int totalBuildTicks);
	void setHologram(Hologram hologram);
	int getHealthPercentage();
	double getHealth();
	double getMaxHealth();
	boolean isDestroyed();
	
	void damage(Attacker attacker, double damage, Block hitBlock);
	
	void onTownLikeBalanceChange();
	void onTownDislikeBalanceChange();
	
	void onFinishMove();
}
