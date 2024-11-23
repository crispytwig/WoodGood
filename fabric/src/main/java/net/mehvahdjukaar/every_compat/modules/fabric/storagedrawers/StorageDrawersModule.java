package net.mehvahdjukaar.every_compat.modules.fabric.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.ModConstants;
import com.jaquadro.minecraft.storagedrawers.block.*;
import com.jaquadro.minecraft.storagedrawers.block.framed.BlockFramedStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.client.renderer.BlockEntityDrawersRenderer;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.dynamicpack.ClientDynamicResourcesHandler;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class StorageDrawersModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, BlockStandardDrawers> FULL_DRAWERS_1;
    public final SimpleEntrySet<WoodType, BlockStandardDrawers> FULL_DRAWERS_2;
    public final SimpleEntrySet<WoodType, BlockStandardDrawers> FULL_DRAWERS_4;
    public final SimpleEntrySet<WoodType, BlockStandardDrawers> HALF_DRAWERS_1;
    public final SimpleEntrySet<WoodType, BlockStandardDrawers> HALF_DRAWERS_2;
    public final SimpleEntrySet<WoodType, BlockStandardDrawers> HALF_DRAWERS_4;
    public final SimpleEntrySet<WoodType, BlockTrim> TRIMS;

    public StorageDrawersModule(String modId) {
        super(modId, "sd");
        ResourceLocation tab = modRes(ModConstants.MOD_ID);

        FULL_DRAWERS_1 = SimpleEntrySet.builder(WoodType.class, "full_drawers_1",
                        ModBlocks.OAK_FULL_DRAWERS_1, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new BlockStandardDrawers(1, false, Utils.copyPropertySafe(ModBlocks.OAK_FULL_DRAWERS_1.get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.BLOCK)
                .addTag(modRes("full_drawers"), Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.ITEM)
                .addTag(modRes("full_drawers"), Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .addTile(getModTile("standard_drawers_1"))
                .createPaletteFromOak(this::drawersPalette)
                .addTexture(modRes("block/drawers_oak_front_1"))
                .addTexture(modRes("block/drawers_oak_side"))
                .addTexture(modRes("block/drawers_oak_sort"))
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();
        this.addEntry(FULL_DRAWERS_1);

        FULL_DRAWERS_2 = SimpleEntrySet.builder(WoodType.class, "full_drawers_2",
                        ModBlocks.OAK_FULL_DRAWERS_2, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CompatStandardDrawers(2, false, Utils.copyPropertySafe(ModBlocks.OAK_FULL_DRAWERS_2.get())))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .addTile(CompatStandardDrawersEntity2::new)
                .createPaletteFromOak(this::drawersPalette)
                .addTexture(modRes("block/drawers_oak_front_2"))
                .addTexture(modRes("block/drawers_oak_side"))
                .addTexture(modRes("block/drawers_oak_sort"))
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();
        this.addEntry(FULL_DRAWERS_2);

        FULL_DRAWERS_4 = SimpleEntrySet.builder(WoodType.class, "full_drawers_4",
                        ModBlocks.OAK_FULL_DRAWERS_4, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CompatStandardDrawers(4, false, Utils.copyPropertySafe(ModBlocks.OAK_FULL_DRAWERS_4.get())))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .addTile(CompatStandardDrawersEntity4::new)
                .createPaletteFromOak(this::drawersPalette)
                .addTexture(modRes("block/drawers_oak_front_4"))
                .addTexture(modRes("block/drawers_oak_side"))
                .addTexture(modRes("block/drawers_oak_sort"))
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();

        this.addEntry(FULL_DRAWERS_4);

        HALF_DRAWERS_1 = SimpleEntrySet.builder(WoodType.class, "half_drawers_1",
                        ModBlocks.OAK_HALF_DRAWERS_1, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CompatStandardDrawers(1, true, Utils.copyPropertySafe(ModBlocks.OAK_HALF_DRAWERS_1.get())))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .addTile(CompatHalfDrawersEntity1::new)
                .createPaletteFromOak(this::drawersPalette)
                .addTexture(modRes("block/drawers_oak_front_1"))
                .addTexture(modRes("block/drawers_oak_side"))
                .addTexture(modRes("block/drawers_oak_side_h"))
                .addTexture(modRes("block/drawers_oak_side_v"))
                .addTexture(modRes("block/drawers_oak_sort"))
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();
        this.addEntry(HALF_DRAWERS_1);

        HALF_DRAWERS_2 = SimpleEntrySet.builder(WoodType.class, "half_drawers_2",
                        ModBlocks.OAK_HALF_DRAWERS_2, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CompatStandardDrawers(2, true, Utils.copyPropertySafe(ModBlocks.OAK_HALF_DRAWERS_2.get())))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .addTile(CompatHalfDrawersEntity2::new)
                .createPaletteFromOak(this::drawersPalette)
                .addTexture(modRes("block/drawers_oak_front_2"))
                .addTexture(modRes("block/drawers_oak_side"))
                .addTexture(modRes("block/drawers_oak_side_h"))
                .addTexture(modRes("block/drawers_oak_side_v"))
                .addTexture(modRes("block/drawers_oak_sort"))
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();
        this.addEntry(HALF_DRAWERS_2);

        HALF_DRAWERS_4 = SimpleEntrySet.builder(WoodType.class, "half_drawers_4",
                        ModBlocks.OAK_HALF_DRAWERS_4, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CompatStandardDrawers(4, true, Utils.copyPropertySafe(ModBlocks.OAK_HALF_DRAWERS_4.get())))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.BLOCK)
                .addTag(modRes("drawers"), Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .addTile(CompatHalfDrawersEntity4::new)
                .createPaletteFromOak(this::drawersPalette)
                .addTexture(modRes("block/drawers_oak_front_4"))
                .addTexture(modRes("block/drawers_oak_side"))
                .addTexture(modRes("block/drawers_oak_side_h"))
                .addTexture(modRes("block/drawers_oak_side_v"))
                .addTexture(modRes("block/drawers_oak_sort"))
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();
        this.addEntry(HALF_DRAWERS_4);

        TRIMS = SimpleEntrySet.builder(WoodType.class, "trim",
                        ModBlocks.OAK_TRIM, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new BlockTrim(Utils.copyPropertySafe(ModBlocks.OAK_TRIM.get())))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTabKey(tab)
                .defaultRecipe()
                .createPaletteFromOak(this::trimPalette)
                .addTexture(modRes("block/drawers_oak_trim"))
                .addModelTransform(m -> m.replaceGenericType("oak", "blocks"))
                .build();
        this.addEntry(TRIMS);
    }

    @Override
    public void onModSetup() {
        super.onModSetup();
        var label = FULL_DRAWERS_1.getBaseBlock().labelGeometry;
        var count = FULL_DRAWERS_1.getBaseBlock().countGeometry;
        var indbase = FULL_DRAWERS_1.getBaseBlock().indGeometry;
        var ind = FULL_DRAWERS_1.getBaseBlock().indGeometry;

        EveryCompat.LOGGER.warn("INFO: \n-label: {} \n-count: {} \n-indbase: {} \n-ind: {}", Arrays.toString(label), Arrays.toString(count), Arrays.toString(indbase), Arrays.toString(ind));
    }

    @Override
    public void addDynamicClientResources(ClientDynamicResourcesHandler handler, ResourceManager manager) {
        super.addDynamicClientResources(handler, manager);
        initDrawerClientData(FULL_DRAWERS_1.blocks.values(), FULL_DRAWERS_1.getBaseBlock());
        initDrawerClientData(FULL_DRAWERS_2.blocks.values(), FULL_DRAWERS_2.getBaseBlock());
        initDrawerClientData(FULL_DRAWERS_4.blocks.values(), FULL_DRAWERS_4.getBaseBlock());
        initDrawerClientData(HALF_DRAWERS_1.blocks.values(), HALF_DRAWERS_1.getBaseBlock());
        initDrawerClientData(HALF_DRAWERS_2.blocks.values(), HALF_DRAWERS_2.getBaseBlock());
        initDrawerClientData(HALF_DRAWERS_4.blocks.values(), HALF_DRAWERS_4.getBaseBlock());
    }

    private void initDrawerClientData(Collection<? extends BlockDrawers> drawers, BlockDrawers base) {
        drawers.forEach((b) -> {
            System.arraycopy(base.labelGeometry, 0, b.labelGeometry, 0, base.labelGeometry.length);
            System.arraycopy(base.countGeometry, 0, b.countGeometry, 0, base.countGeometry.length);
            System.arraycopy(base.indBaseGeometry, 0, b.indBaseGeometry, 0, base.indBaseGeometry.length);
            System.arraycopy(base.slotGeometry, 0, b.slotGeometry, 0, base.slotGeometry.length);
            System.arraycopy(base.indGeometry, 0, b.indGeometry, 0, base.indGeometry.length);
        });
    }

    private void drawersPalette(Palette p) {
        p.remove(p.getLightest());
        p.increaseInner();
        p.increaseInner();
        p.increaseInner();
        p.increaseUp();
    }

    private void trimPalette(Palette p) {
        p.remove(p.getLightest());
        p.increaseInner();
        p.increaseUp();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerBlockEntityRenderers(ClientHelper.BlockEntityRendererEvent event) {
//        FULL_DRAWERS_1.registerTileRenderer(event, BlockEntityDrawersRenderer::new);
        FULL_DRAWERS_2.registerTileRenderer(event, BlockEntityDrawersRenderer::new);
        FULL_DRAWERS_4.registerTileRenderer(event, BlockEntityDrawersRenderer::new);
        HALF_DRAWERS_1.registerTileRenderer(event, BlockEntityDrawersRenderer::new);
        HALF_DRAWERS_2.registerTileRenderer(event, BlockEntityDrawersRenderer::new);
        HALF_DRAWERS_4.registerTileRenderer(event, BlockEntityDrawersRenderer::new);
    }

    @Override
    public void onClientSetup() {
        super.onClientSetup();
        ModDrawersGeometry.loadGeometryData(this);
    }

    private class CompatStandardDrawers extends BlockStandardDrawers {
        public CompatStandardDrawers(int drawerCount, boolean halfDepth, BlockBehaviour.Properties properties) {
            super(drawerCount, halfDepth, properties);
        }

        public BlockEntityDrawers newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
            if (this.isHalfDepth()) {
                return switch (this.getDrawerCount()) {
                    case 1 -> new CompatHalfDrawersEntity1(pos, state);
                    case 2 -> new CompatHalfDrawersEntity2(pos, state);
                    case 4 -> new CompatHalfDrawersEntity4(pos, state);
                    default -> null;
                };
            }
            return switch (this.getDrawerCount()) {
                case 1 -> new CompatStandardDrawersEntity1(pos, state);
                case 2 -> new CompatStandardDrawersEntity2(pos, state);
                case 4 -> new CompatStandardDrawersEntity4(pos, state);
                default -> null;
            };
        }
    }

    class CompatStandardDrawersEntity1 extends BlockEntityDrawersStandard.Slot1 {

        public CompatStandardDrawersEntity1(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return FULL_DRAWERS_1.getTile(BlockEntity.class);
        }
    }

    class CompatStandardDrawersEntity2 extends BlockEntityDrawersStandard.Slot2 {

        public CompatStandardDrawersEntity2(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return FULL_DRAWERS_2.getTile();
        }
    }

    class CompatStandardDrawersEntity4 extends BlockEntityDrawersStandard.Slot4 {

        public CompatStandardDrawersEntity4(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return FULL_DRAWERS_4.getTile();
        }

    }

    class CompatHalfDrawersEntity1 extends BlockEntityDrawersStandard.Slot1 {

        public CompatHalfDrawersEntity1(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return HALF_DRAWERS_1.getTile();
        }
    }

    class CompatHalfDrawersEntity2 extends BlockEntityDrawersStandard.Slot2 {

        public CompatHalfDrawersEntity2(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return HALF_DRAWERS_2.getTile();
        }
    }

    class CompatHalfDrawersEntity4 extends BlockEntityDrawersStandard.Slot4 {

        public CompatHalfDrawersEntity4(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return HALF_DRAWERS_4.getTile();
        }

    }



    private <B extends Block> Stream<B> getBlocksOfType(Class<B> blockClass) {
        Stream<Block> allBlocks = this.getEntries().stream().map(e-> ((SimpleEntrySet<?, B>) e)
                .blocks.values()).flatMap(Collection::stream);
        Objects.requireNonNull(blockClass);
        allBlocks = allBlocks.filter(blockClass::isInstance);
        Objects.requireNonNull(blockClass);
        return allBlocks.map(blockClass::cast);
    }

    public <BD extends BlockDrawers> Stream<BD> getDrawersOfType(Class<BD> drawerClass) {
        return getBlocksOfType(drawerClass);
    }

    public <BD extends BlockDrawers> Stream<BD> getDrawersOfTypeAndSize(Class<BD> drawerClass, int size) {
        return getDrawersOfType(drawerClass).filter((blockDrawers) -> {
            return blockDrawers.getDrawerCount() == size;
        });
    }

    public <BD extends BlockDrawers> Stream<BD> getDrawersOfTypeAndSizeAndDepth(Class<BD> drawerClass, int size, boolean halfDepth) {
        return getDrawersOfTypeAndSize(drawerClass, size).filter((blockDrawers) -> {
            return blockDrawers.isHalfDepth() == halfDepth;
        });
    }
}