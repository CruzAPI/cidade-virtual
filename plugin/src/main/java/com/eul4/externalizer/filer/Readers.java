package com.eul4.externalizer.filer;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.externalizer.reader.AdminReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Readers
{
	private final ObjectInput in;
	private final Versions versions;
	
	private final Map<Type, ObjectReader<?>> readers = new HashMap<>();
	
	public ObjectReader<?> getOrCreate(Type type) throws InvalidVersionException
	{
		return readers.computeIfAbsent(type, (key) -> type.newInstanceSneaky(this));
	}
	
	@RequiredArgsConstructor
	public enum Type
	{
		ADMIN(AdminReader.class, AdminReader::new),
		;
		
		private final Class<? extends ObjectReader<?>> type;
		private final ReaderConstructor readerConstructor;
		
		@SneakyThrows
		public ObjectReader<?> newInstanceSneaky(Readers readers)
		{
			return readerConstructor.newInstance(readers);
		}
	}
	
	@FunctionalInterface
	private interface ReaderConstructor
	{
		ObjectReader<?> newInstance(Readers readers) throws InvalidVersionException;
	}
}
