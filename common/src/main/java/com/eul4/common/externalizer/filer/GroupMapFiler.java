package com.eul4.common.externalizer.filer;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.GroupMapReader;
import com.eul4.common.externalizer.writer.GroupMapWriter;
import com.eul4.common.model.permission.GroupMap;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.common.util.FileUtil;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.logging.Level;

public class GroupMapFiler extends Filer
{
	private static final byte VERSION = 1;
	
	private static final ObjectType[]
	
	OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.GROUP,
		CommonObjectType.GROUP_MAP,
		CommonObjectType.GROUP_USER,
		CommonObjectType.GROUP_USER_MAP,
		CommonObjectType.OBJECT,
		CommonObjectType.PERMISSION,
		CommonObjectType.PERMISSION_MAP,
		CommonObjectType.TIMED_TICK,
	},
	
	OBJECT_TYPES_V1 = new ObjectType[]
	{
		CommonObjectType.GROUP,
		CommonObjectType.GROUP_GROUP,
		CommonObjectType.GROUP_GROUP_MAP,
		CommonObjectType.GROUP_MAP,
		CommonObjectType.GROUP_USER,
		CommonObjectType.GROUP_USER_MAP,
		CommonObjectType.OBJECT,
		CommonObjectType.PERMISSION,
		CommonObjectType.PERMISSION_MAP,
		CommonObjectType.TIMED_TICK,
		CommonObjectType.UUID,
	};
	
	@Getter
	private GroupMap memoryGroupMap;
	
	public GroupMapFiler(Common plugin)
	{
		super(plugin, VERSION);
	}
	
	public void load()
	{
		if(memoryGroupMap != null)
		{
			throw new RuntimeException("GroupMapFiler is already loaded!");
		}
		
		memoryGroupMap = loadFromDisk().orElseGet(GroupMap::new);
	}
	
	public void save()
	{
		File tmp = null;
		
		try
		{
			File file = plugin.getCommonDataFileManager().createGroupsFileIfNotExists();
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try
			(
				FileOutputStream fileOut = new FileOutputStream(tmp);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
				DataOutputStream out = new DataOutputStream(bufferedOut)
			)
			{
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(GroupMapWriter.class)
						.writeReferenceNotNull(memoryGroupMap);
				out.flush();
			}
			
			if(tmp.renameTo(file))
			{
				plugin.getLogger().info(MessageFormat.format("{0} saved! size={1} length={2}", file.getName(),
						memoryGroupMap.getGroups().size(), file.length()));
			}
			else
			{
				throw new IOException("Failed to replace the old groups file with the new one.");
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to create groups file.", e);
		}
		finally
		{
			FileUtil.deleteTempFile(tmp, plugin.getLogger());
		}
	}
	
	@SneakyThrows
	private Optional<GroupMap> loadFromDisk()
	{
		final File file = plugin.getCommonDataFileManager().getGroupsFile();
		
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
						GroupMap groupMap = Readers.of(plugin, in, readVersions(in))
								.getReader(GroupMapReader.class)
								.readReference();
						plugin.getLogger().info(MessageFormat.format(
								"(ObjectStream) {0} loaded from disk! {1} groups loaded!",
								file.getName(),
								groupMap.getGroups().size()));
						return Optional.of(groupMap);
					}
				}
				else
				{
					try(DataInputStream in = new DataInputStream(bufferedIn))
					{
						GroupMap groupMap = Readers.of(plugin, in, readVersions(in))
								.getReader(GroupMapReader.class)
								.readReference();
						plugin.getLogger().info(MessageFormat.format(
								"(InputStream) {0} loaded from disk! {1} groups loaded!",
								file.getName(),
								groupMap.getGroups().size()));
						return Optional.of(groupMap);
					}
				}
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
					"Failed to load {0}!",
					file.getName()), e);
			throw e;
		}
	}
	
	@Override
	protected ObjectType[] getObjectTypes(byte version) throws InvalidVersionException
	{
		return switch(version)
		{
			case 0 -> OBJECT_TYPES_V0;
			case 1 -> OBJECT_TYPES_V1;
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
