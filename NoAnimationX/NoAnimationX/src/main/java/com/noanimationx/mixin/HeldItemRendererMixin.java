package com.noanimationx.mixin;

import com.noanimationx.NoAnimationXClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    private static boolean noanimationx$isTargetHeld() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return false;

        ItemStack main = client.player.getMainHandStack();
        ItemStack off = client.player.getOffHandStack();
        return NoAnimationXClient.isTargetItem(main) || NoAnimationXClient.isTargetItem(off);
    }

    @ModifyVariable(
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 2
    )
    private float noanimationx$removeSwingAnimation(float swingProgress) {
        if (noanimationx$isTargetHeld() && NoAnimationXClient.shouldLockHand()) {
            return 0.0F;
        }
        return swingProgress;
    }

    @ModifyVariable(
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 3
    )
    private float noanimationx$lockEquipPosition(float equipProgress) {
        if (noanimationx$isTargetHeld() && NoAnimationXClient.shouldLockItem()) {
            return 1.0F;
        }
        return equipProgress;
    }
}
