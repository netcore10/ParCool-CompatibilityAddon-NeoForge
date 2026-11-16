package com.alrexu.parcool.compat.mixin;

import net.neoforged.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ParCoolCompatAddonMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PACKAGE = "com.alrexu.parcool.compat.mixin.client.";
    private static final Set<String> PAL_MIXINS = Set.of(
            PACKAGE + "PlayerModelPalCompositionMixin",
            PACKAGE + "VaultStepUpPalMixin"
    );
    private static final Set<String> EMF_MIXINS = Set.of(
            PACKAGE + "PlayerModelEmfCompatMixin",
            PACKAGE + "ModelFeatureRendererPlayerBaseStateMixin"
    );

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (PAL_MIXINS.contains(mixinClassName)) {
            return log("ParCool 3 + Player Animation Library composition mixin", mixinClassName, isLoaded("player_animation_library"));
        }
        if (EMF_MIXINS.contains(mixinClassName)) {
            return log("ParCool 3 + EMF render-scope mixin", mixinClassName, isLoaded("entity_model_features"));
        }
        return true;
    }

    private static boolean log(String what, String mixinClassName, boolean modLoaded) {
        boolean enabled = modLoaded && isParCool3Loaded();
        LOGGER.info("{} {} {}", enabled ? "Enabling" : "Skipping", what, mixinClassName);
        return enabled;
    }

    private static boolean isLoaded(String modId) {
        return FMLLoader.getCurrent().getLoadingModList().getModFileById(modId) != null;
    }

    private static boolean isParCool3Loaded() {
        return FMLLoader.getCurrent().getLoadingModList().getMods().stream()
                .filter(info -> "parcool".equals(info.getModId()))
                .map(info -> info.getVersion().toString())
                .map(ParCoolCompatAddonMixinPlugin::parseMajorVersion)
                .anyMatch(major -> major == 3);
    }

    private static int parseMajorVersion(String version) {
        int separator = version.indexOf('.');
        String major = separator >= 0 ? version.substring(0, separator) : version;
        try {
            return Integer.parseInt(major);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
