package com.sandevistanmod.client;

import com.sandevistanmod.SandevistanMod;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = SandevistanMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent e) {
        e.register(ModKeys.SCREEN);
        e.register(ModKeys.TOGGLE);
    }

    @SubscribeEvent
    public static void guiLayers(RegisterGuiLayersEvent e) {
        e.registerBelow(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "vision"), ClientOverlay::renderVision);
        e.registerBelow(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "psychosis"), PsychosisFx::renderTint);
        e.registerAboveAll(ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "hud"), ClientOverlay::renderHud);
        e.registerAboveAll(ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "psychosis_hud"),
                PsychosisFx::renderHud);
    }

    /** Coloca o implante nas costas dos dois modelos de jogador (wide e slim). */
    @SubscribeEvent
    public static void playerLayers(EntityRenderersEvent.AddLayers e) {
        for (var skin : e.getSkins()) {
            var renderer = e.getSkin(skin);
            if (renderer instanceof PlayerRenderer pr) {
                pr.addLayer(new BackLayer(pr));
            }
        }
    }
}
