package com.alrexu.parcool.compat.extern.carryon;

import com.alrex.parcool.api.unstable.action.ParCoolActionEvent;
import com.alrexu.parcool.compat.extern.ExternalModManager;
import net.neoforged.bus.api.SubscribeEvent;

public class EventHandlerForCarryOn {
    @SubscribeEvent
    public static void onTryToStart(ParCoolActionEvent.TryToStart event) {
        if (ExternalModManager.get(CarryOnManager.class).isCarrying(event.getPlayer())) {
            event.setCanceled(true);
        }
    }
}
