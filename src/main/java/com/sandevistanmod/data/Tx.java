package com.sandevistanmod.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** Textos traduziveis do mod: portugues (pt_br/pt_pt) para quem joga em portugues e ingles para os demais idiomas. */
public final class Tx {
    private Tx() {}

    public static MutableComponent t(String key, Object... args) {
        return Component.translatable("sandevistan." + key, args);
    }

    public static MutableComponent t(ChatFormatting color, String key, Object... args) {
        return Component.translatable("sandevistan." + key, args).withStyle(color);
    }
}
