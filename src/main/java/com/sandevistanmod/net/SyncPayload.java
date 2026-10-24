package com.sandevistanmod.net;

import com.sandevistanmod.SandevistanMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Dados do proprio jogador (implante, ligado, Mente) enviados ao cliente dele. */
public record SyncPayload(CompoundTag tag) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "sync"));
    public static final StreamCodec<ByteBuf, SyncPayload> CODEC =
            ByteBufCodecs.COMPOUND_TAG.map(SyncPayload::new, SyncPayload::tag);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
