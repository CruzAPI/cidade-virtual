package com.eul4.common.externalizer.filer;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.UserReader;
import com.eul4.common.externalizer.writer.UserWriter;
import com.eul4.common.model.permission.User;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.common.util.FileUtil;
import com.eul4.common.util.LoggerUtil;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

public class UserFiler extends Filer
{
	private static final byte VERSION = 0;
	
	private static final ObjectType[]
	
	OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.OBJECT,
		CommonObjectType.PERMISSION,
		CommonObjectType.PERMISSION_MAP,
		CommonObjectType.TIMED_TICK,
		CommonObjectType.USER,
	};
	
	private final Map<UUID, User> memoryUsers = new HashMap<>();
	
	public UserFiler(Common plugin)
	{
		super(plugin, VERSION);
	}
	
	public void saveMemoryUsers()
	{
		final Iterator<User> iterator = memoryUsers.values().iterator();
		
		final int memoryUsersSize = memoryUsers.size();
		
		if(memoryUsersSize > 0)
		{
			LoggerUtil.info(plugin, "Saving {0} memory users...", memoryUsersSize);
		}
		
		int successCount = 0;
		int failCount = 0;
		
		while(iterator.hasNext())
		{
			User user = iterator.next();
			
			if(save(user))
			{
				successCount++;
			}
			else
			{
				failCount++;
			}
			
			if(!plugin.getPlayerManager().isRegistered(user.getUuid()))
			{
				iterator.remove();
			}
		}
		
		if(successCount > 0)
		{
			LoggerUtil.info(plugin, "{0} memory users saved!", successCount);
		}
		
		if(failCount > 0)
		{
			LoggerUtil.warning(plugin, "Failed to save {0} memory users!", failCount);
		}
	}
	
	private boolean save(User user)
	{
		File tmp = null;
		
		try
		{
			File file = plugin.getCommonDataFileManager().createUserFileIfNotExists(user.getUuid());
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try
			(
				FileOutputStream fileOut = new FileOutputStream(tmp);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
				DataOutputStream out = new DataOutputStream(bufferedOut);
			)
			{
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(UserWriter.class)
						.writeReference(user);
				out.flush();
			}
			
			if(!tmp.renameTo(file))
			{
				throw new IOException(MessageFormat.format(
						"Failed to replace the old user file with the new one. user={0}",
						user.getUuid()));
			}
			
			return true;
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to create user file.", e);
		}
		finally
		{
			FileUtil.deleteTempFile(tmp, plugin.getLogger());
		}
		
		return false;
	}
	
	public User load(Player player)
	{
		return getFromMemoryOrDiskOrCreateNewOne(player);
	}
	
	public User load(UUID userUniqueId)
	{
		return getFromMemoryOrDiskOrCreateNewOne(userUniqueId);
	}
	
	public User getFromMemoryOrDiskOrCreateNewOne(Player player)
	{
		return getFromMemoryOrDiskOrCreateNewOne(player.getUniqueId());
	}
	
	public Optional<User> getFromMemoryOrDisk(UUID uuid)
	{
		return Optional.ofNullable(memoryUsers.get(uuid)).or(() -> loadFromDisk(uuid));
	}
	
	public User getFromMemoryOrDiskOrCreateNewOne(UUID uuid)
	{
		return memoryUsers.computeIfAbsent(uuid, (key) -> Optional
				.ofNullable(memoryUsers.get(uuid))
				.or(() -> loadFromDisk(uuid))
				.orElseGet(() -> new User(uuid)));
	}
	
	private Optional<User> loadFromDisk(UUID uuid)
	{
		final File file = plugin.getCommonDataFileManager().getUserFile(uuid);
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning(MessageFormat.format("{0} file is empty or not exists!", file.getName()));
			return Optional.empty();
		}
		
		try
		(
			FileInputStream fileIn = new FileInputStream(file);
			FileChannel fileChannel = fileIn.getChannel()
		)
		{
			byte[] header = new byte[2];
			fileIn.read(header);
			
			fileChannel.position(0L);
			
			try(BufferedInputStream bufferedIn = new BufferedInputStream(fileIn))
			{
				if(isObjectStream(header))
				{
					try(ObjectInputStream in = new ObjectInputStream(bufferedIn))
					{
						User user = Readers.of(plugin, in, readVersions(in))
								.getReader(UserReader.class)
								.readReference();
						LoggerUtil.info(plugin,
								"(ObjectStream) User loaded from disk! name={0} uuid={1}",
								plugin.getServer().getOfflinePlayer(uuid).getName(),
								uuid);
						return Optional.of(user);
					}
				}
				else
				{
					try(DataInputStream in = new DataInputStream(bufferedIn))
					{
						User user = Readers.of(plugin, in, readVersions(in))
								.getReader(UserReader.class)
								.readReference();
						LoggerUtil.info(plugin,
								"(DataStream) User loaded from disk! name={0} uuid={1}",
								plugin.getServer().getOfflinePlayer(uuid).getName(),
								uuid);
						return Optional.of(user);
					}
				}
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
					"Failed to load {0}!",
					file.getName()), e);
			return Optional.empty();
		}
	}
	
	@Override
	protected ObjectType[] getObjectTypes(byte version) throws InvalidVersionException
	{
		return switch(version)
		{
			case 0 -> OBJECT_TYPES_V0;
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
