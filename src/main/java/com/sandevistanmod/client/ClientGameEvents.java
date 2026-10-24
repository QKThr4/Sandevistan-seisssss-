package com.sandevistanmod.client;

import com.sandevistanmod.SandevistanMod;
import com.sandevistanmod.net.ActionPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = SandevistanMod.MODID, value = Dist.CLIENT)
public final class ClientGameEvents {
    private ClientGameEvents() {}

    /** Queda: velocidade vertical nova = velocidade * FALL_MULT - FALL_ADD, limitada a FALL_CAP (blocos por tick). */
    private static final double FALL_MULT = 3.0;
    private static final double FALL_ADD = 1.0;
    private static final double FALL_CAP = 16.0;

    private static boolean wasActive;

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            wasActive = false;
            return;
        }
        boolean on = ClientState.active(mc.player.getUUID());
        if (on) {
            // cair bem mais rapido (a gravidade do jogo ja esta no maximo, entao aceleramos a descida)
            Vec3 m = mc.player.getDeltaMovement();
            if (!mc.player.onGround() && !mc.player.getAbilities().flying && !mc.player.isFallFlying()
                    && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.onClimbable() && m.y < 0) {
                mc.player.setDeltaMovement(m.x, Math.max(-FALL_CAP, m.y * FALL_MULT - FALL_ADD), m.z);
            }
        } else if (wasActive) {
            // desligou: para no lugar
            mc.player.setDeltaMovement(Vec3.ZERO);
        }
        wasActive = on;
        while (ModKeys.SCREEN.consumeClick()) {
            if (mc.screen == null) mc.setScreen(new SandevistanScreen());
        }
        while (ModKeys.TOGGLE.consumeClick()) {
            PacketDistributor.sendToServer(new ActionPayload(0));
        }
    }

    /**
     * Com a velocidade absurda o campo de visao abriria ao maximo o tempo todo: com o Sandevistan ligado
     * ele fica num valor fixo e suave.
     */
    @SubscribeEvent
    public static void fov(ComputeFovModifierEvent e) {
        if (ClientState.active(e.getPlayer().getUUID())) {
            e.setNewFovModifier(1.12f);
        }
    }

    /** Com a ciberpsicose a camera treme. */
    @SubscribeEvent
    public static void cameraShake(ViewportEvent.ComputeCameraAngles e) {
        if (!PsychosisFx.shaking()) return;
        float psy = PsychosisFx.psy();
        e.setRoll(e.getRoll() + PsychosisFx.shake(psy, 90.0) * 0.8f);
        e.setPitch(e.getPitch() + PsychosisFx.shake(psy, 57.0) * 0.4f);
        e.setYaw(e.getYaw() + PsychosisFx.shake(psy, 73.0) * 0.4f);
    }

    /** Ao sair do mundo, limpa o estado do cliente para nao vazar para o proximo mundo. */
    @SubscribeEvent
    public static void loggingOut(ClientPlayerNetworkEvent.LoggingOut e) {
        ClientState.clear();
        Afterimages.clear();
        ClientData.DATA.deserializeNBT(RegistryAccess.EMPTY, new CompoundTag());
    }
}
