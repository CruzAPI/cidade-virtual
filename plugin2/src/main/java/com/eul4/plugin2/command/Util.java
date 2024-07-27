package com.eul4.plugin2.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Util {
    public static Component arruma(String message)
    {
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }
}
