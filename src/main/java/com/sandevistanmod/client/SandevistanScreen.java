package com.sandevistanmod.client;

import com.sandevistanmod.data.SandevistanData;
import com.sandevistanmod.data.Tx;
import com.sandevistanmod.net.ActionPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Aba especial do Sandevistan: implante, Mente, o que ele faz e as acoes. */
public class SandevistanScreen extends Screen {
    private Button remove;
    private Button corner;

    public SandevistanScreen() {
        super(Tx.t("screen.title"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 2 + 36;
        remove = addRenderableWidget(Button.builder(Tx.t("btn.remove"),
                        b -> PacketDistributor.sendToServer(new ActionPayload(1)))
                .bounds(cx - 110, y, 220, 20).build());
        corner = addRenderableWidget(Button.builder(cornerLabel(), b -> {
                    ClientLayout.nextCorner();
                    b.setMessage(cornerLabel());
                })
                .bounds(cx - 110, y + 22, 220, 20).build());
        addRenderableWidget(Button.builder(Tx.t("btn.close"), b -> onClose())
                .bounds(cx - 110, y + 44, 220, 20).build());
        refresh();
    }

    private static Component cornerLabel() {
        return Tx.t("btn.corner", Tx.t("corner." + ClientLayout.corner));
    }

    private void refresh() {
        SandevistanData d = ClientData.DATA;
        if (remove == null) return;
        remove.active = d.implanted && !d.active;
    }

    @Override
    public void tick() {
        refresh();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ModKeys.SCREEN.matches(keyCode, scanCode)) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static String time(int seconds) {
        return (seconds / 60) + ":" + String.format("%02d", seconds % 60);
    }

    @Override
    public void renderBackground(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(gg, mouseX, mouseY, partialTick);
        SandevistanData d = ClientData.DATA;
        int cx = this.width / 2;
        int cy = this.height / 2;

        gg.fill(cx - 140, cy - 120, cx + 140, cy + 108, 0xC0101410);
        gg.fill(cx - 140, cy - 120, cx + 140, cy - 118, 0xFF39FF14);
        gg.drawCenteredString(this.font, Tx.t("screen.title"), cx, cy - 113, 0x39FF14);

        int y = cy - 98;
        MutableComponent implant = d.implanted ? Tx.t("screen.implanted") : Tx.t("screen.noimplant");
        gg.drawCenteredString(this.font, implant, cx, y, d.implanted ? 0x55FF55 : 0xAAAAAA);
        y += 12;

        if (d.implanted) {
            gg.drawCenteredString(this.font, d.active ? Tx.t("screen.on") : Tx.t("screen.off"), cx, y,
                    d.active ? 0x39FF14 : 0xAAAAAA);
            y += 14;
            ClientOverlay.bar(gg, cx - 100, y, 200, 9, d.mind / SandevistanData.MAX_MIND,
                    ClientOverlay.mindColor(d.mind));
            ClientOverlay.lostMark(gg, cx - 100, y, 200, 9, SandevistanData.MAX_MIND - d.maxMind());
            y += 12;
            gg.drawCenteredString(this.font,
                    Tx.t("screen.mind", (int) d.mind, (int) d.maxMind(), time(d.secondsLeft())), cx, y,
                    d.mind < 20f ? 0xFF5555 : 0x88FFAA);
            y += 14;
            MutableComponent key = Component.empty().append(ModKeys.TOGGLE.getTranslatedKeyMessage());
            gg.drawCenteredString(this.font, Tx.t("screen.key", key), cx, y, 0xDDDDDD);
            y += 14;
        } else {
            gg.drawCenteredString(this.font, Tx.t("screen.howto"), cx, y + 4, 0xBBBBBB);
            y += 30;
        }

        gg.drawCenteredString(this.font, Tx.t("screen.fx.1", SandevistanData.SLOW_TICK_RATE), cx, y, 0x88DDFF);
        y += 11;
        gg.drawCenteredString(this.font, Tx.t("screen.fx.2"), cx, y, 0xBBBBBB);
        y += 11;
        gg.drawCenteredString(this.font, Tx.t("screen.fx.3"), cx, y, 0xBBBBBB);
        y += 11;
        gg.drawCenteredString(this.font, Tx.t("screen.fx.4"), cx, y, 0xBBBBBB);
    }
}
