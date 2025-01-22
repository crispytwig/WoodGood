package net.mehvahdjukaar.every_compat.modules.neoforge.create;

import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.WindowBlock;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

// SUPPORT: v0.5.1+
public class CreateModule extends SimpleModule {

//    public final SimpleEntrySet<WoodType, Block> windows;
    public final SimpleEntrySet<WoodType, Block> windowPanes;

    public CreateModule(String modId) {
        super(modId, "c");
        var tab = CreativeModeTabs.BUILDING_BLOCKS;

/*
        windows = SimpleEntrySet.builder(WoodType.class, "window",
                        getModBlock("oak_window"), () -> WoodTypeRegistry.OAK_TYPE, //AllPaletteBlocks.OAK_WINDOW
                        this::makeWindow)
                .addTag(BlockTags.IMPERMEABLE, Registries.BLOCK)
                .setTabKey(tab)
                .defaultRecipe()
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .createPaletteFromPlanks(p -> p.remove(p.getDarkest()))
                .addTextureM(modRes("block/palettes/oak_window"), EveryCompat.res("block/palettes/oak_window_m"))
                .addTextureM(modRes("block/palettes/oak_window_connected"), EveryCompat.res("block/palettes/oak_window_connected_m"))
                .build();
        this.addEntry(windows);
*/

        windowPanes = SimpleEntrySet.builder(WoodType.class, "window_pane",
                        getModBlock("oak_window_pane"), () -> WoodTypeRegistry.OAK_TYPE, //AllPaletteBlocks.OAK_WINDOW_PANE
                        s -> new ConnectedGlassPaneBlock(Utils.copyPropertySafe(Blocks.GLASS_PANE)))
//                .addTag(Tags.Items.GLASS_PANES, Registries.BLOCK)
                .setTabKey(tab)
                .defaultRecipe()
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .build();
        this.addEntry(windowPanes);

    }

    private WindowBlock makeWindow(WoodType w) {
        return new WindowBlock(Utils.copyPropertySafe(Blocks.GLASS)
                .isValidSpawn((s, l, ps, t) -> false).isRedstoneConductor((s, l, ps) -> false)
                .isSuffocating((s, l, ps) -> false).isViewBlocking((s, l, ps) -> false), false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientSetup() {
        super.onClientSetup();
/*        windows.blocks.forEach((w, b) -> {
            String path = "block/" + shortenedId() + "/" + w.getNamespace() + "/palettes/" + w.getTypeName() + "_window";

            CTSpriteShiftEntry spriteShift = CTSpriteShifter.getCT(AllCTTypes.VERTICAL,
                    EveryCompat.res(path), EveryCompat.res(path + "_connected"));

            CreateClient.MODEL_SWAPPER.getCustomBlockModels().register(Utils.getID(b),
                    (model) -> new CTModel(model, new HorizontalCTBehaviour(spriteShift)));
            CreateClient.MODEL_SWAPPER.getCustomBlockModels().register(Utils.getID(windowPanes.blocks.get(w)),
                    (model) -> new CTModel(model, new GlassPaneCTBehaviour(spriteShift)));
        });*/
    }

}