package com.alrexu.parcool.compat.client;

import com.alrexu.parcool.compat.ParCoolCompatAddon;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * Keyframed step-up animations (assets/parcool_compat_addon/player_animations/step_up.json, made in Blockbench) for
 * ParCool's quick step-up on block staircases. Only loaded when Player Animation Library is installed.
 */
public final class StepUpAnimations {
    private static final Identifier LAYER = Identifier.fromNamespaceAndPath(ParCoolCompatAddon.MOD_ID, "step_up");
    private static final Identifier RIGHT = Identifier.fromNamespaceAndPath(ParCoolCompatAddon.MOD_ID, "step_up_right");
    private static final Identifier LEFT = Identifier.fromNamespaceAndPath(ParCoolCompatAddon.MOD_ID, "step_up_left");
    // Tuning: the step-up itself is 5 ticks and staircases chain them back to back.
    private static final float SPEED = 1.0f;     // playback speed; the authored animation (0.375s) already spans the gap to the next step
    private static final int FADE_IN_TICKS = 3;  // crossfade from the previous step's pose instead of snapping

    private StepUpAnimations() {
    }

    /** Must run in FMLClientSetupEvent#enqueueWork (PAL docs). 1500+ = gameplay priority. */
    public static void register() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(LAYER, 1500,
                player -> {
                    PlayerAnimationController controller = new PlayerAnimationController(player, (c, state, setter) -> PlayState.STOP);
                    controller.getModifiers().add(new SpeedModifier(SPEED));
                    return controller;
                });
    }

    public static void play(Player player, boolean rightLeads) {
        if (PlayerAnimationAccess.getPlayerAnimationLayer(player, LAYER) instanceof PlayerAnimationController controller) {
            controller.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(FADE_IN_TICKS, EasingType.EASE_IN_OUT_SINE),
                    rightLeads ? RIGHT : LEFT);
        }
    }
}
