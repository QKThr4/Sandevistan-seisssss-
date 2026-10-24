package com.sandevistanmod.net;

import com.sandevistanmod.SandevistanMod;
import com.sandevistanmod.client.ClientState;
import com.sandevistanmod.data.SandevistanData;
import com.sandevistanmod.logic.SandevistanLogic;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = SandevistanMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class Net {
    private Net() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar("1");
        reg.playToClient(SyncPayload.TYPE, SyncPayload.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> ClientNet.handleSync(payload)));
        reg.playToClient(StatePayload.TYPE, StatePayload.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> ClientState.set(payload.uuid(), payload.code())));
        reg.playToServer(ActionPayload.TYPE, ActionPayload.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> {
                    if (ctx.player() instanceof ServerPlayer sp) {
                        SandevistanLogic.handleAction(sp, payload.action());
                    }
                }));
    }

    public static void sync(ServerPlayer sp) {
        SandevistanData d = SandevistanData.get(sp);
        PacketDistributor.sendToPlayer(sp, new SyncPayload(d.serializeNBT(sp.level().registryAccess())));
    }
}
