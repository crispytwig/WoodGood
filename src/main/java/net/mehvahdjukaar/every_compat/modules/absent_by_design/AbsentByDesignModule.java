package net.mehvahdjukaar.every_compat.modules.absent_by_design;

import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.dynamicpack.ClientDynamicResourcesHandler;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.mehvahdjukaar.selene.client.asset_generators.LangBuilder;
import net.mehvahdjukaar.selene.resourcepack.AfterLanguageLoadEvent;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.Objects;

//SUPPORT: v1.6.2-FINAL
public class AbsentByDesignModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> fence_logs;
    public final SimpleEntrySet<WoodType, Block> wall_logs;
    public final SimpleEntrySet<WoodType, Block> wall_stripped_logs;
    public final SimpleEntrySet<WoodType, Block> wall_planks;

    public AbsentByDesignModule(String modId) {
        super(modId, "abd");

        fence_logs = SimpleEntrySet.builder(WoodType.class, "", "fence_log",
                        () -> getModBlock("fence_log_oak"), () -> WoodType.OAK_WOOD_TYPE,
                        w -> new FenceBlock(copyProperties(w.log)
                        )
                )
                //TEXTURE: using logs
                .addTag(BlockTags.FENCES, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .addTag(ItemTags.FENCES, Registry.ITEM_REGISTRY)
                .defaultRecipe()
                .build();
        this.addEntry(fence_logs);

        wall_logs = SimpleEntrySet.builder(WoodType.class, "log", "wall",
                        () -> getModBlock("wall_oak_log"), () -> WoodType.OAK_WOOD_TYPE,
                        w -> new WallBlock(copyProperties(w.log)
                        )
                )
                //TEXTURE: using logs
                .addTag(BlockTags.WALLS, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registry.BLOCK_REGISTRY)
                .addTag(ItemTags.WALLS, Registry.ITEM_REGISTRY)
                .defaultRecipe()
                .build();
        this.addEntry(wall_logs);

        wall_stripped_logs = SimpleEntrySet.builder(WoodType.class, "log", "wall_stripped",
                        () -> getModBlock("wall_stripped_oak_log"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new WallBlock(copyProperties(
                                Objects.requireNonNull(w.getBlockOfThis("stripped_log"))
                            )
                        ), "stripped_log") //REASON: textures & recipes
                )
                //TEXTURE: using stripped_logs
                .addTag(BlockTags.WALLS, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registry.BLOCK_REGISTRY)
                .addTag(ItemTags.WALLS, Registry.ITEM_REGISTRY)
                .defaultRecipe()
                .build();
        this.addEntry(wall_stripped_logs);

        wall_planks = SimpleEntrySet.builder(WoodType.class, "planks", "wall",
                        () -> getModBlock("wall_oak_planks"), () -> WoodType.OAK_WOOD_TYPE,
                        w -> new WallBlock(copyProperties(w.planks)
                        )
                )
                //TEXTURE: using planks
                .addTag(BlockTags.WALLS, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registry.BLOCK_REGISTRY)
                .addTag(ItemTags.WALLS, Registry.ITEM_REGISTRY)
                .defaultRecipe()
                .build();
        this.addEntry(wall_planks);

    }

    private static BlockBehaviour.Properties copyProperties(Block block) {
        return BlockBehaviour.Properties.of(Material.WOOD)
                .color(block.defaultMaterialColor())
                .sound(block.defaultBlockState().getSoundType())
                .strength(block.defaultDestroyTime());
    }

    @Override
    public void addTranslations(ClientDynamicResourcesHandler clientDynamicResourcesHandler, AfterLanguageLoadEvent lang) {
        super.addTranslations(clientDynamicResourcesHandler, lang);

        // the key in en_us wasn't applied to fence_logs, below make sure it's applied
        fence_logs.blocks.forEach((wood, block) -> LangBuilder.addDynamicEntry(lang, "block_type.absentbydesign.fence_log", wood, block));
    }
}