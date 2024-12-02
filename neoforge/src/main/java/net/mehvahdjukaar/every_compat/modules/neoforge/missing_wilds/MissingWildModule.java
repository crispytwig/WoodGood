package net.mehvahdjukaar.every_compat.modules.neoforge.missing_wilds;

import me.ultrusmods.missingwilds.block.FallenLogBlock;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

//SUPPORT v1.3.2+
public class MissingWildModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> fallenLogs;

    public MissingWildModule(String modId) {
        super(modId, "msw");

        fallenLogs = SimpleEntrySet.builder(WoodType.class, "log", "fallen",
                        getModBlock("fallen_acacia_log"), () -> WoodTypeRegistry.getValue(ResourceLocation.parse("acacia")),
                        w -> new FallenLogBlock(Utils.copyPropertySafe(w.log).noOcclusion()))
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .addTag(modRes("fallen_logs"), Registries.BLOCK)
                .addTag(modRes("fallen_logs"), Registries.ITEM)
                .setTabKey(modRes("items"))
                .defaultRecipe()
                .build();
        this.addEntry(fallenLogs);
    }
}
