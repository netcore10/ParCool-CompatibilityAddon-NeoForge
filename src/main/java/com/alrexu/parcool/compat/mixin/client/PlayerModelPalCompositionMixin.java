package com.alrexu.parcool.compat.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.zigythebird.playeranim.accessors.IAvatarAnimationState;
import com.zigythebird.playeranim.accessors.IBoneUpdater;
import com.zigythebird.playeranim.animation.AvatarAnimManager;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * PAL applies its animation at the RETURN of PlayerModel#setupAnim, after ParCool's TAIL pass, so the two
 * already compose. But ParCool cancels setupAnim at HEAD for its charge-jump animators, which skips PAL's
 * RETURN pass and drops e.g. a Better Combat attack. When that happens, run PAL's own per-part update.
 */
@Mixin(value = PlayerModel.class, priority = 2002)
public abstract class PlayerModelPalCompositionMixin {
    @Unique
    private boolean parcoolCompatAddon$reachedReturn;
    @Unique
    private final PlayerAnimBone parcoolCompatAddon$head = new PlayerAnimBone("head");
    @Unique
    private final PlayerAnimBone parcoolCompatAddon$torso = new PlayerAnimBone("torso");
    @Unique
    private final PlayerAnimBone parcoolCompatAddon$rightArm = new PlayerAnimBone("right_arm");
    @Unique
    private final PlayerAnimBone parcoolCompatAddon$leftArm = new PlayerAnimBone("left_arm");
    @Unique
    private final PlayerAnimBone parcoolCompatAddon$rightLeg = new PlayerAnimBone("right_leg");
    @Unique
    private final PlayerAnimBone parcoolCompatAddon$leftLeg = new PlayerAnimBone("left_leg");

    // A HEAD-cancelled setupAnim returns early and never reaches RETURN injectors (verified in the mixin dump)
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("RETURN"))
    private void parcoolCompatAddon$markReturn(AvatarRenderState state, CallbackInfo callback) {
        parcoolCompatAddon$reachedReturn = true;
    }

    @WrapMethod(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V")
    private void parcoolCompatAddon$applyPalAfterCancel(AvatarRenderState state, Operation<Void> original) {
        parcoolCompatAddon$reachedReturn = false;
        original.call(state);
        if (parcoolCompatAddon$reachedReturn) return;

        if (!(state instanceof IAvatarAnimationState animState)) return;
        AvatarAnimManager emote = animState.playerAnimLib$getAnimManager();
        if (emote == null || !emote.isActive()) return;

        PlayerModel model = (PlayerModel) (Object) this;
        IBoneUpdater updater = (IBoneUpdater) this;
        updater.pal$updatePart(emote, model.head, parcoolCompatAddon$head);
        updater.pal$updatePart(emote, model.rightArm, parcoolCompatAddon$rightArm);
        updater.pal$updatePart(emote, model.leftArm, parcoolCompatAddon$leftArm);
        updater.pal$updatePart(emote, model.rightLeg, parcoolCompatAddon$rightLeg);
        updater.pal$updatePart(emote, model.leftLeg, parcoolCompatAddon$leftLeg);
        updater.pal$updatePart(emote, model.body, parcoolCompatAddon$torso);

        // Same first-person visibility PAL applies in its RETURN pass
        if (animState.playerAnimLib$isFirstPersonPass()) {
            model.head.visible = false;
            model.body.visible = false;
            model.leftLeg.visible = false;
            model.rightLeg.visible = false;
            FirstPersonConfiguration config = emote.getFirstPersonConfiguration();
            model.rightArm.visible = config.isShowRightArm();
            model.leftArm.visible = config.isShowLeftArm();
        }
    }
}
