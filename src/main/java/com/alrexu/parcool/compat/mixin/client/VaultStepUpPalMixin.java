package com.alrexu.parcool.compat.mixin.client;

import com.alrex.parcool.common.action.impl.Vault;
import com.alrex.parcool.common.attachment.client.Animation;
import com.alrex.parcool.common.attachment.common.Parkourability;
import com.alrexu.parcool.compat.client.StepUpAnimations;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

/**
 * Plays the keyframed step-up animation (Player Animation Library) instead of ParCool's coded StepUpAnimator, for the
 * local player and for other players. Only when ParCool would animate it at all (its animation config allows it).
 */
@Mixin(value = Vault.class, remap = false)
public abstract class VaultStepUpPalMixin {
    @Shadow
    private Vault.AnimationType currentAnimation;
    @Shadow
    private boolean rightLeadsNext;

    @Inject(method = {"onStartInLocalClient", "onStartInOtherClient"}, at = @At("TAIL"))
    private void parcoolCompat$keyframedStepUp(Player player, Parkourability parkourability, ByteBuffer startData, CallbackInfo ci) {
        if (currentAnimation != Vault.AnimationType.STEP_UP) return;
        Animation animation = Animation.get(player);
        if (animation == null || !animation.hasAnimator()) return; // ParCool animation disabled for this move
        animation.removeAnimator();
        // ParCool flips rightLeadsNext after starting a step, so the step just started used the opposite leg
        StepUpAnimations.play(player, !rightLeadsNext);
    }
}
