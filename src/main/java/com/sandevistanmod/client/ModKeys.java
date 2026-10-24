package com.sandevistanmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class ModKeys {
    private ModKeys() {}

    public static final String CATEGORY = "key.categories.sandevistan";

    public static final KeyMapping SCREEN = key("key.sandevistan.screen", GLFW.GLFW_KEY_J);
    public static final KeyMapping TOGGLE = key("key.sandevistan.toggle", GLFW.GLFW_KEY_V);

    private static KeyMapping key(String name, int code) {
        return new KeyMapping(name, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, code, CATEGORY);
    }
}
