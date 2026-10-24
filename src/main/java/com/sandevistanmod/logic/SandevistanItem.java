package com.sandevistanmod.logic;

import com.sandevistanmod.data.Tx;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/** O implante Sandevistan: botao direito (duas vezes, com aviso de dano) para implantar. */
public class SandevistanItem extends Item {
    public SandevistanItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            if (SandevistanLogic.tryImplant(sp) && !sp.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Tx.t(ChatFormatting.GREEN, "tooltip.1"));
        tooltip.add(Tx.t(ChatFormatting.GRAY, "tooltip.2"));
        tooltip.add(Tx.t(ChatFormatting.RED, "tooltip.3", SandevistanLogic.IMPLANT_HEARTS));
    }
}
