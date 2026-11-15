package com.alrexu.parcool.compat.client;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Tracks when the base player model is being posed and drawn. Since 26.x that happens in the deferred
 * ModelFeatureRenderer pass, separately from armor and other feature-layer models.
 */
public final class PlayerBaseRenderState {
    private static final ThreadLocal<Deque<Boolean>> SCOPES = ThreadLocal.withInitial(ArrayDeque::new);

    private PlayerBaseRenderState() {
    }

    public static void begin(boolean basePlayerModel) {
        SCOPES.get().push(basePlayerModel);
    }

    public static void end() {
        Deque<Boolean> scopes = SCOPES.get();
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
        if (scopes.isEmpty()) {
            SCOPES.remove();
        }
    }

    public static boolean isRenderingBaseModel() {
        Deque<Boolean> scopes = SCOPES.get();
        return !scopes.isEmpty() && scopes.peek();
    }
}
