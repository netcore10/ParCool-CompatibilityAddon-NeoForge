package com.alrexu.parcool.compat.mixin.client;

import com.alrexu.parcool.compat.client.PlayerBaseRenderState;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Scopes the deferred pose + draw of the base PlayerModel, where EMF animates it. Armor and other
 * layer models go through the same method with non-PlayerModel models, so they keep their EMF variants.
 */
@Mixin(value = ModelFeatureRenderer.class, priority = 2000)
public abstract class ModelFeatureRendererPlayerBaseStateMixin {
    @Inject(method = "prepareModel", at = @At("HEAD"))
    private <S> void parcoolCompatAddon$beginBasePlayerModel(ModelFeatureRenderer.Submit<S> submit, CallbackInfo callback) {
        PlayerBaseRenderState.begin(submit.model() instanceof PlayerModel);
    }

    @Inject(method = "prepareModel", at = @At("RETURN"))
    private <S> void parcoolCompatAddon$endBasePlayerModel(ModelFeatureRenderer.Submit<S> submit, CallbackInfo callback) {
        PlayerBaseRenderState.end();
    }
}
