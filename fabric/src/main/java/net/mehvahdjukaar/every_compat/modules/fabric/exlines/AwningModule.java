package net.mehvahdjukaar.every_compat.modules.fabric.exlines;

import com.exline.exlineawnings.ExlineAwningsMain;
import com.exline.exlineawnings.block.AwningBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class AwningModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, AwningBlock> awnings;
    public final SimpleEntrySet<WoodType, AwningBlock> awnings_log;

    @SuppressWarnings("DataFlowIssue")
    public AwningModule(String modId) {
        super(modId, "ea");

        awnings = SimpleEntrySet.builder(WoodType.class, "awning",
                        getModBlock("oak_awning", AwningBlock.class), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new AwningBlock(w.getBlockOfThis("stairs").defaultBlockState(),
                                FabricBlockSettings.create()
                                        .mapColor(MapColor.SNOW)
                                        .strength(1.0F, 0.1F)
                                        .nonOpaque()
                                        .blockVision(AwningModule::never)
                        )
                )
                .requiresChildren("stairs")
                .setTabKey(ExlineAwningsMain.FURNITURE_GROUP)
                .addRecipe(modRes("oak_awning_recipe"))
                .build();
        this.addEntry(awnings);

        awnings_log = SimpleEntrySet.builder(WoodType.class, "log_awning",
                        getModBlock("oak_log_awning", AwningBlock.class), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new AwningBlock(w.getBlockOfThis("stairs").defaultBlockState(),
                                FabricBlockSettings.create()
                                        .mapColor(MapColor.SNOW)
                                        .strength(1.0F, 0.1F)
                                        .nonOpaque()
                                        .blockVision(AwningModule::never)
                        )
                )
                .requiresChildren("stairs")
                // terrestria:sakura_log's texture do not have the full 16x16x. Take a look, you'll understand why.
                .addCondition(w -> !w.getId().toString().equals("terrestria:sakura_log"))
                .setTabKey(ExlineAwningsMain.FURNITURE_GROUP)
                .addRecipe(modRes("oak_log_awning_recipe"))
                .build();
        this.addEntry(awnings_log);


    }

    private static boolean never(BlockState state, BlockGetter blockView, BlockPos blockPos) {
        return false;
    }
}