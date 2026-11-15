package com.alrexu.parcool.compat.mixin.client;

import com.alrexu.parcool.compat.client.EntityModelFeaturesCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Selects vanilla geometry for the base player model without changing EMF armor model variants. */
@Mixin(value = PlayerModel.class, priority = 2000)
public abstract class PlayerModelEmfCompatMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
    private void parcoolCompatAddon$prepareEmfPlayerModel(AvatarRenderState state, CallbackInfo callback) {
        var level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(state.id) instanceof Player player) {
            EntityModelFeaturesCompat.preparePlayerModel((PlayerModel) (Object) this, player);
        }
    }
}
