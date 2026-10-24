package com.sandevistanmod.net;

import com.sandevistanmod.SandevistanMod;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Estado visivel de um jogador para todos: bit 1 tem o implante nas costas, bit 2 esta com o Sandevistan ligado. */
public record StatePayload(UUID uuid, int code) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StatePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "state"));
    public static final StreamCodec<ByteBuf, StatePayload> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, StatePayload::uuid,
            ByteBufCodecs.VAR_INT, StatePayload::code,
            StatePayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
