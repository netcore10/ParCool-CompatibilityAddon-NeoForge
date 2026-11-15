package com.alrexu.parcool.compat.extern.carryon;

import com.alrex.parcool.api.unstable.animation.ParCoolAnimationInfoEvent;
import com.alrexu.parcool.compat.extern.ExternalModManager;
import net.neoforged.bus.api.SubscribeEvent;

/**
 * Client only: ParCoolAnimationInfoEvent references client classes. @OnlyIn no longer strips members on
 * NeoForge 26.x, so this lives in its own class that is only registered on the client.
 */
public class ClientEventHandlerForCarryOn {
    @SubscribeEvent
    public static void onUpdateParCoolAnimInfo(ParCoolAnimationInfoEvent event) {
        if (ExternalModManager.get(CarryOnManager.class).isCarrying(event.getPlayer())) {
            event.getOption().cancelAnimation();
        }
    }
}
