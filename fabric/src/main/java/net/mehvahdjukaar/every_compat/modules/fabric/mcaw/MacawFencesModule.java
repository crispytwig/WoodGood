package net.mehvahdjukaar.every_compat.modules.fabric.mcaw;

import net.kikoz.mcwfences.MacawsFences;
import net.kikoz.mcwfences.init.BlockInit;
import net.kikoz.mcwfences.objects.FenceHitbox;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.misc.SpriteHelper;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;

//SUPPORT v1.1.1+
public class MacawFencesModule extends SimpleModule {

    public final SimpleEntrySet<LeavesType, Block> hedges;
    public final SimpleEntrySet<WoodType, Block> highleyGates;
    public final SimpleEntrySet<WoodType, Block> horseFences;
    public final SimpleEntrySet<WoodType, Block> picketFences;
    public final SimpleEntrySet<WoodType, Block> pyramidGates;
    public final SimpleEntrySet<WoodType, Block> stockadeFences;
    public final SimpleEntrySet<WoodType, Block> wiredFences;

    public MacawFencesModule(String modId) {
        super(modId, "mcf");
        ResourceKey<CreativeModeTab> tab = MacawsFences.FENCESGROUP;

        picketFences = SimpleEntrySet.builder(WoodType.class, "picket_fence",
                        () -> BlockInit.OAK_PICKET_FENCE, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new FenceBlock(Utils.copyPropertySafe(w.planks)
                                .strength(1.4F, 2.0F).noOcclusion()))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .build();
        this.addEntry(picketFences);

        stockadeFences = SimpleEntrySet.builder(WoodType.class, "stockade_fence",
                        () -> BlockInit.OAK_STOCKADE_FENCE, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new FenceBlock(Utils.copyPropertySafe(w.planks)
                                .strength(1.4F, 2.0F).noOcclusion()))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .build();
        this.addEntry(stockadeFences);

        horseFences = SimpleEntrySet.builder(WoodType.class, "horse_fence",
                        () -> BlockInit.OAK_HORSE_FENCE, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new FenceBlock(Utils.copyPropertySafe(w.planks)
                                .strength(1.4F, 2.0F).noOcclusion()))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .build();
        this.addEntry(horseFences);

        wiredFences = SimpleEntrySet.builder(WoodType.class, "wired_fence",
                        () -> BlockInit.OAK_WIRED_FENCE, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WiredFence(Utils.copyPropertySafe(w.planks)
                                .strength(1.5F, 2.5F).noOcclusion()))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .setRenderType(RenderLayer.CUTOUT)
                .build();
        this.addEntry(wiredFences);


        pyramidGates = SimpleEntrySet.builder(WoodType.class, "pyramid_gate",
                        () -> BlockInit.OAK_PYRAMID_GATE, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new FenceGateBlock(w.toVanillaOrOak(), Utils.copyPropertySafe(w.planks)
                                .strength(1.4F, 2.0F).noOcclusion())
                )
                .addTag(BlockTags.FENCE_GATES, Registries.BLOCK)
                .addTag(BlockTags.UNSTABLE_BOTTOM_CENTER, Registries.BLOCK)
                .setTabKey(tab)
                .defaultRecipe()
                .build();
        this.addEntry(pyramidGates);

        highleyGates = SimpleEntrySet.builder(WoodType.class, "highley_gate",
                        () -> BlockInit.OAK_HIGHLEY_GATE, () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new FenceGateBlock(w.toVanillaOrOak(), Utils.copyPropertySafe(w.planks)
                                .strength(1.4F, 2.0F).noOcclusion())
                )
                .addTag(BlockTags.FENCE_GATES, Registries.BLOCK)
                .addTag(BlockTags.UNSTABLE_BOTTOM_CENTER, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTabKey(tab)
                .defaultRecipe()
                .build();
        this.addEntry(highleyGates);

        hedges = SimpleEntrySet.builder(LeavesType.class, "hedge",
                        () -> BlockInit.OAK_HEDGE, () -> LeavesTypeRegistry.OAK_TYPE,
                        l -> {
                            // Reason: Below have no leave texture
                            if (l.getId().toString().equals("regions_unexplored:flowering")) return null;
                            return new FenceHitbox(Utils.copyPropertySafe(l.leaves).lightLevel((s) -> 0)
                                    .strength(0.2F, 0.3F).noOcclusion()
                                    .mapColor(l.leaves.defaultMapColor()));
                        })
                .requiresChildren("leaves") // Reason: Recipes
                .addTag(BlockTags.MINEABLE_WITH_HOE, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.BLOCK)
                .addTag(BlockTags.WALLS, Registries.BLOCK)
                .addTag(ItemTags.WALLS, Registries.ITEM)
                .setTabKey(tab)
                .defaultRecipe()
                .copyParentTint()
                .addModelTransform(m -> m.replaceWithTextureFromChild("mcwfences:block/oak_leaves",
                        "leaves", SpriteHelper.LOOKS_LIKE_LEAF_TEXTURE))
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .build();
        this.addEntry(hedges);
    }

    public static class WiredFence extends FenceBlock {

        public WiredFence(Properties properties) {
            super(properties);
        }

        //changed. I couldn't help myself
        @Override
        public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entityIn) {
            if (state.getValue(NORTH) || state.getValue(SOUTH) || state.getValue(EAST) || state.getValue(WEST))
                entityIn.hurt(level.damageSources().generic(), 2.0F);
        }
    }

}
