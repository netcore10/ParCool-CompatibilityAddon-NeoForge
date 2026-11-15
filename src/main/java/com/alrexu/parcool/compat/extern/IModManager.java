package com.alrexu.parcool.compat.extern;

import net.neoforged.bus.api.IEventBus;

public interface IModManager {
    void init(IEventBus modBus, IEventBus forgeBus);

    // These are called after `init` method
    default void initInClient(IEventBus modBus, IEventBus forgeBus) {
    }

    default void initInDedicatedServer(IEventBus modBus, IEventBus forgeBus) {
    }

    boolean isInstalled();

    String getModID();
}
