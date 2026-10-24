package com.sandevistanmod.data;

import com.sandevistanmod.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

/** Estado do Sandevistan de um jogador (salvo no jogador e copiado ao morrer). */
public class SandevistanData implements INBTSerializable<CompoundTag> {
    public static final float MAX_MIND = 100f;
    /** Tempo, em segundos reais, que a barra de Mente cheia dura com o Sandevistan ligado. */
    public static final float USE_SECONDS = 30f;
    /** Tempo, em segundos reais, para a barra encher do zero (1 de Mente a cada 2 segundos) com o Sandevistan desligado. */
    public static final float REGEN_SECONDS = 200f;
    /** Mente minima para conseguir ligar o Sandevistan. */
    public static final float MIN_TO_START = 10f;
    /** Tick rate do jogo inteiro enquanto o Sandevistan esta ligado. */
    public static final int SLOW_TICK_RATE = 2;

    /** Ciberpsicose maxima guardada (a barra mostra 100%, mas ela pode passar disso). */
    public static final float MAX_PSY = 300f;
    /** A cada 50 de Mente gasta (50% de uso) o jogador ganha 5 de ciberpsicose. */
    public static final float PSY_USE_STEP = 50f;
    public static final float PSY_PER_STEP = 5f;
    /** Segundos parado (sem ligar o Sandevistan) ate os efeitos negativos comecarem. */
    public static final float PSY_IDLE_SECONDS = 5f;
    /** Duracao do efeito Mente Boa do remedio e tempo (s) para regenerar 1 de Mente com ele. */
    public static final float GOOD_MIND_SECONDS = 60f;
    public static final float GOOD_MIND_REGEN_SECONDS = 30f;

    public boolean implanted;
    public boolean active;
    public float mind = MAX_MIND;
    /** Ciberpsicose (0 a 300; 100 = limite da barra) e Mente Boa restante (segundos reais). */
    public float psy;
    public float goodMind;
    /** Mente perdida PARA SEMPRE pela ciberpsicose (a Mente maxima cai); so o Imunossupressor recupera. */
    public float mindLost;
    /** Imunossupressor: bonus temporario de Mente maxima, doses tomadas e segundos restantes. */
    public float immuBonus;
    public int immuDoses;
    public float immuTime;
    /** Mente gasta ainda nao convertida em ciberpsicose. */
    public float useAcc;
    /** Sintomas ativos agora (parou de usar ha mais de 5 s): enviado ao cliente para os efeitos de tela. */
    public boolean psychotic;

    // temporizadores so do servidor (nao salvos)
    public transient float idle;
    public transient float decayT;
    public transient float drainT;
    public transient float secT;
    public transient float goodT;
    public transient float rampage;
    public transient float hitT;
    public transient boolean zeroWarned;
    public transient float zeroT;

    public static SandevistanData get(Player p) {
        return p.getData(ModRegistries.SANDEVISTAN);
    }

    /** Mente maxima atual (100 menos o que a ciberpsicose tirou de vez). */
    public float maxMind() {
        return Math.max(0f, Math.min(MAX_MIND, MAX_MIND - mindLost + immuBonus));
    }

    /** Segundos reais restantes de uso com a Mente atual. */
    public int secondsLeft() {
        return Math.max(0, (int) Math.ceil(mind / (MAX_MIND / USE_SECONDS)));
    }

    /** Codigo enviado aos outros jogadores: bit 1 implante nas costas, bit 2 ligado. */
    public int stateCode() {
        return (implanted ? 1 : 0) | (active && implanted ? 2 : 0);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag t = new CompoundTag();
        t.putBoolean("implanted", implanted);
        t.putBoolean("active", active);
        t.putFloat("mind", mind);
        t.putFloat("psy", psy);
        t.putFloat("lost", mindLost);
        t.putFloat("ib", immuBonus);
        t.putInt("id", immuDoses);
        t.putFloat("it", immuTime);
        t.putFloat("good", goodMind);
        t.putFloat("acc", useAcc);
        t.putBoolean("psychotic", psychotic);
        return t;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag t) {
        implanted = t.getBoolean("implanted");
        active = t.getBoolean("active");
        mind = t.contains("mind") ? t.getFloat("mind") : MAX_MIND;
        psy = t.getFloat("psy");
        mindLost = Math.max(0f, Math.min(MAX_MIND, t.getFloat("lost")));
        immuBonus = Math.max(0f, t.getFloat("ib"));
        immuDoses = Math.max(0, t.getInt("id"));
        immuTime = Math.max(0f, t.getFloat("it"));
        goodMind = t.getFloat("good");
        useAcc = t.getFloat("acc");
        psychotic = t.getBoolean("psychotic");
    }
}
