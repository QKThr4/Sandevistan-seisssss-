package com.sandevistanmod;

import com.sandevistanmod.registry.ModRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SandevistanMod.MODID)
public class SandevistanMod {
    public static final String MODID = "sandevistan";

    public SandevistanMod(IEventBus modBus) {
        ModRegistries.ATTACHMENTS.register(modBus);
        ModRegistries.SOUNDS.register(modBus);
        ModRegistries.EFFECTS.register(modBus);
        ModRegistries.ITEMS.register(modBus);
        ModRegistries.TABS.register(modBus);
    }
}
