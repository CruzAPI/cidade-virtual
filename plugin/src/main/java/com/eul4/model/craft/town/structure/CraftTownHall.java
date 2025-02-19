package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.TownHall;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.TownHallAttribute;
import com.eul4.util.FaweUtil;
import com.eul4.util.MessageUtil;
import com.eul4.wrapper.Resource;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Getter
public class CraftTownHall extends CraftResourceStructure implements TownHall
{
	private int likeCapacity;
	private int dislikeCapacity;
	private transient Vector3 relativeSpawnPosition;
	
	@Setter(AccessLevel.PRIVATE)
	private transient int remainingLikeCapacity;
	@Setter(AccessLevel.PRIVATE)
	private transient int remainingDislikeCapacity;
	
	private Map<StructureType, Integer> structureLimitMap;
	private final Resource likeResource = Resource.builder()
			.type(Resource.Type.LIKE)
			.relativePosition(BlockVector3.at(-1, 1, 0))
			.subtractOperation(this::subtractVirtualLikes)
			.emptyChecker(this::isVirtualLikesEmpty)
			.build();
	private final Resource dislikeResource = Resource.builder()
			.type(Resource.Type.DISLIKE)
			.relativePosition(BlockVector3.at(1, 1, 0))
			.subtractOperation(this::subtractVirtualDislikes)
			.emptyChecker(this::isVirtualDislikesEmpty)
			.build();
	
	private final Set<Resource> resources = Set.of(likeResource, dislikeResource);
	
	public CraftTownHall(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, false);
	}
	
	public CraftTownHall(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	public CraftTownHall(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.TOWN_HALL;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<TownHallAttribute> getRule()
	{
		return (Rule<TownHallAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	public void resetAttributes()
	{
		super.resetAttributes();
		
		likeCapacity = getRule().getAttribute(getBuiltLevel()).getLikeCapacity();
		dislikeCapacity = getRule().getAttribute(getBuiltLevel()).getDislikeCapacity();
		structureLimitMap = getRule().getAttribute(getBuiltLevel()).getStructureLimit();
		relativeSpawnPosition = getRule().getAttributeOrDefault(getLevel()).getSpawnPosition();
	}
	
	@Override
	public void updateHologram()
	{
		if(status != StructureStatus.BUILT)
		{
			super.updateHologram();
			return;
		}
		
		if(town.isUnderAttack())
		{
			if(isDestroyed())
			{
				teleportHologram(getLocation()
						.toHighestLocation()
						.getBlock()
						.getRelative(BlockFace.UP)
						.getLocation()
						.toCenterLocation());
				hologram.setSize(3);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_TOWN_HALL_VIRTUAL_LIKES, getVirtualLikes());
				hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_TOWN_HALL_VIRTUAL_DISLIKES, getVirtualDislikes());
			}
			else
			{
				hologram.setSize(5);
				hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_HOLOGRAM_TITLE, getStructureType(), level);
				hologram.getLine(1).setMessageAndArgs(PluginMessage.STRUCTURE_TOWN_HALL_VIRTUAL_LIKES, getVirtualLikes());
				hologram.getLine(2).setMessageAndArgs(PluginMessage.STRUCTURE_TOWN_HALL_VIRTUAL_DISLIKES, getVirtualDislikes());
				hologram.getLine(3).setMessageAndArgs(PluginMessage.STRUCTURE_HEALTH_POINTS, getHealth(), getMaxHealth());
				hologram.getLine(4).setCustomName(MessageUtil.getPercentageProgressBar(getHealthPercentage()));
				teleportHologramToDefaultLocation();
			}
		}
		else
		{
			hologram.remove();
		}
	}
	
	@Override
	public void onTownLikeBalanceChange()
	{
		onAnyBalanceChange();
	}
	
	@Override
	public void onTownDislikeBalanceChange()
	{
		onAnyBalanceChange();
	}
	
	private void onAnyBalanceChange()
	{
		ifUnderAttackUpdateResources();
	}
	
	private int getVirtualLikes()
	{
		return Math.min(likeCapacity, town.getLikes());
	}
	
	private int getVirtualDislikes()
	{
		return Math.min(dislikeCapacity, town.getDislikes());
	}
	
	private boolean isVirtualLikesEmpty()
	{
		return getVirtualLikes() == 0;
	}
	
	private boolean isVirtualDislikesEmpty()
	{
		return getVirtualDislikes() == 0;
	}
	
	private int subtractVirtualLikes(int likesToSubtract)
	{
		return subtractVirtualBalance(this::setRemainingLikeCapacity,
				town::subtractLikes,
				this::getVirtualLikes,
				this::getRemainingLikeCapacity,
				likesToSubtract);
	}
	
	private int subtractVirtualDislikes(int dislikesToSubtract)
	{
		return subtractVirtualBalance(this::setRemainingDislikeCapacity,
				town::subtractDislikes,
				this::getVirtualDislikes,
				this::getRemainingDislikeCapacity,
				dislikesToSubtract);
	}
	
	@Override
	public void onStartAttack()
	{
		super.onStartAttack();
		
		remainingLikeCapacity = likeCapacity;
		remainingDislikeCapacity = dislikeCapacity;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return getBukkitRotatedSpawnPosition().toLocation(getWorld(), getRotation() + 180.0F, 0.0F);
	}
	
	public Vector getBukkitRotatedSpawnPosition()
	{
		return FaweUtil.toBukkitVector(getRotatedSpawnPosition());
	}
	
	public Vector3 getRotatedSpawnPosition()
	{
		return getCenterPosition().add(getRotatedSpawnRelativePosition());
	}
	
	public Vector3 getRotatedSpawnRelativePosition()
	{
		return new AffineTransform().rotateY(-getRotation()).apply(relativeSpawnPosition);
	}
}
