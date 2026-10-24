package com.sandevistanmod.logic;

import com.sandevistanmod.data.Tx;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/** Estabilizador Neural: botao direito para tomar. Da Mente Boa por 1 minuto (regenera Mente e baixa a ciberpsicose). */
public class StabilizerItem extends Item {
    public StabilizerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            if (!SandevistanLogic.takeRemedy(sp)) return InteractionResultHolder.fail(stack);
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1f, 1f);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (stack.isEmpty()) return InteractionResultHolder.sidedSuccess(bottle, false);
                if (!player.getInventory().add(bottle)) player.drop(bottle, false);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Tx.t(ChatFormatting.GREEN, "tooltip.stabilizer.1"));
        tooltip.add(Tx.t(ChatFormatting.GRAY, "tooltip.stabilizer.2"));
    }
}
