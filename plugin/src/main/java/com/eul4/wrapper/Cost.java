package com.eul4.wrapper;

import com.eul4.Price;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.Map;

@RequiredArgsConstructor
@Builder
@Getter
public class Cost
{
	private final Price price;
	private final Map<Material, Integer> resources;
}
