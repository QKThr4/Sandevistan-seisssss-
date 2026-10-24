package com.sandevistanmod.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import net.neoforged.fml.loading.FMLPaths;

/** Preferencia do jogador: em que canto da tela fica o bloco da Mente (muda na aba J). */
public final class ClientLayout {
    private ClientLayout() {}

    /** Escala do bloco do HUD. */
    public static final float SCALE = 0.8f;
    public static final int HUD_W = 120;
    public static final int HUD_H = 60;

    /** 0 em cima a esquerda, 1 em cima a direita, 2 embaixo a esquerda, 3 embaixo a direita. */
    public static int corner = 0;

    static {
        load();
    }

    public static void nextCorner() {
        corner = (corner + 1) % 4;
        save();
    }

    public static int x(int screenW) {
        int w = Math.round(HUD_W * SCALE);
        return (corner == 1 || corner == 3) ? screenW - w - 6 : 6;
    }

    public static int y(int screenH) {
        int h = Math.round(HUD_H * SCALE);
        return (corner == 2 || corner == 3) ? screenH - h - 6 : 6;
    }

    private static Path file() {
        return FMLPaths.CONFIGDIR.get().resolve("sandevistan-client.properties");
    }

    public static void load() {
        try {
            Path f = file();
            if (!Files.exists(f)) return;
            Properties p = new Properties();
            try (InputStream in = Files.newInputStream(f)) {
                p.load(in);
            }
            corner = Math.max(0, Math.min(3, Integer.parseInt(p.getProperty("corner", "0").trim())));
        } catch (Exception ignored) {
            // sem arquivo ou arquivo invalido: fica com o padrao
        }
    }

    public static void save() {
        try {
            Properties p = new Properties();
            p.setProperty("corner", Integer.toString(corner));
            try (OutputStream out = Files.newOutputStream(file())) {
                p.store(out, "Sandevistan client settings");
            }
        } catch (Exception ignored) {
            // nao foi possivel salvar: vale so ate fechar o jogo
        }
    }
}
