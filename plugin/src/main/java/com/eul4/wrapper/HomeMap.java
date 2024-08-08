package com.eul4.wrapper;

import com.eul4.Main;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Getter
public class HomeMap extends LinkedHashMap<String, Location>
{
	private final Main plugin;
}
