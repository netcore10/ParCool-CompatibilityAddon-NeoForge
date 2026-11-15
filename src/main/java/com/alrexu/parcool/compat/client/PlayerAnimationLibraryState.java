package com.alrexu.parcool.compat.client;

import com.zigythebird.playeranim.accessors.IAnimatedAvatar;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** Isolated so Player Animation Library classes only load when PAL is installed. */
public final class PlayerAnimationLibraryState {
    private static final boolean LOADED = ModList.get().isLoaded("player_animation_library");

    private PlayerAnimationLibraryState() {
    }

    public static boolean isAnimating(Player player) {
        return LOADED && Access.isAnimating(player);
    }

    private static final class Access {
        private static boolean isAnimating(Player player) {
            if (!(player instanceof IAnimatedAvatar avatar)) return false;
            var manager = avatar.playerAnimLib$getAnimManager();
            return manager != null && manager.isActive();
        }
    }
}
