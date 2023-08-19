package net.mehvahdjukaar.every_compat.dynamicpack;

import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.configs.EarlyConfigs;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

public class ServerDynamicResourcesHandler extends DynServerResourcesGenerator {

    public static final ServerDynamicResourcesHandler INSTANCE = new ServerDynamicResourcesHandler();

    public ServerDynamicResourcesHandler() {
        super(new DynamicDataPack(EveryCompat.res("generated_pack")));
        //needed for tags
        getPack().addNamespaces("minecraft");
        getPack().addNamespaces("forge");
        getPack().addNamespaces(EveryCompat.MOD_ID);
        this.dynamicPack.setGenerateDebugResources(PlatHelper.isDev() || EarlyConfigs.DEBUG_RESOURCES.get());
    }

    @Override
    public Logger getLogger() {
        return EveryCompat.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return EarlyConfigs.SPEC != null && EarlyConfigs.DEPEND_ON_PACKS.get();
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        EveryCompat.forAllModules(m -> {
            try {
                m.addDynamicServerResources(this, manager);
            } catch (Exception e) {
                getLogger().error("Failed to generate server dynamic assets for module {}: {}", m, e);
            }
        });
    }

}
