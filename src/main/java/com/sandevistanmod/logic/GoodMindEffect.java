package com.sandevistanmod.logic;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** Mente Boa: efeito do Imunossupressor (a regeneracao de Mente e controlada pelo servidor em tempo real). */
public class GoodMindEffect extends MobEffect {
    public GoodMindEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x39FF14);
    }
}
