package net.mehvahdjukaar.every_compat.modules.mcaw;

import com.mcwstairs.kikoz.MacawsStairs;
import com.mcwstairs.kikoz.objects.BalconyRailing;
import com.mcwstairs.kikoz.objects.StairPlatform;
import com.mcwstairs.kikoz.objects.StairRailing;
import com.mcwstairs.kikoz.objects.stair_types.*;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

//SUPPORT: v1.0.0+
public class MacawStairsModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> terrace_stairs;
    public final SimpleEntrySet<WoodType, Block> skyline_stairs;
    public final SimpleEntrySet<WoodType, Block> compact_stairs;
    public final SimpleEntrySet<WoodType, Block> bulk_stairs;
    public final SimpleEntrySet<WoodType, Block> loft_stairs;
    public final SimpleEntrySet<WoodType, Block> balconies;
    public final SimpleEntrySet<WoodType, Block> railings;
    public final SimpleEntrySet<WoodType, Block> platforms;

    public MacawStairsModule(String modId) {
        super(modId, "mws");
        var tab = MacawsStairs.StairsItemGroup;

        terrace_stairs = SimpleEntrySet.builder(WoodType.class, "terrace_stairs",
                        () -> getModBlock("oak_terrace_stairs"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new TerraceStairs(copyProperties()), "stripped_log") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(terrace_stairs);

        skyline_stairs = SimpleEntrySet.builder(WoodType.class, "skyline_stairs",
                        () -> getModBlock("oak_skyline_stairs"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new SkylineStairs(copyProperties()), "stripped_log") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(skyline_stairs);

        compact_stairs = SimpleEntrySet.builder(WoodType.class, "compact_stairs",
                        () -> getModBlock("oak_compact_stairs"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new CompactStairs(copyProperties()), "stripped_log") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(compact_stairs);

        bulk_stairs = SimpleEntrySet.builder(WoodType.class, "bulk_stairs",
                        () -> getModBlock("oak_bulk_stairs"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new BulkStairs(copyProperties()), "stripped_log") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(bulk_stairs);

        loft_stairs = SimpleEntrySet.builder(WoodType.class, "loft_stairs",
                        () -> getModBlock("oak_loft_stairs"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new LoftStairs(copyProperties()), "stripped_log") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(loft_stairs);

        balconies = SimpleEntrySet.builder(WoodType.class, "balcony",
                        () -> getModBlock("oak_balcony"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new BalconyRailing(copyProperties()), "fence") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(balconies);

        railings = SimpleEntrySet.builder(WoodType.class, "railing",
                        () -> getModBlock("oak_railing"), () -> WoodType.OAK_WOOD_TYPE,
                        w -> {
                            if (balconies.blocks.get(w) == null) return null; //REASON: recipes
                            return new StairRailing(copyProperties());
                        }
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(railings);

        platforms = SimpleEntrySet.builder(WoodType.class, "platform",
                        () -> getModBlock("oak_platform"), () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new StairPlatform(copyProperties()), "slab") //REASON: recipes
                )
                .setRenderType(() -> RenderType::cutoutMipped)
                //TEXTURES: oak_log
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(platforms);

    }

    // METHODS
    public BlockBehaviour.Properties copyProperties() {
        return BlockBehaviour.Properties.of(Material.WOOD)
                .strength(2.0F, 2.3F)
                .sound(SoundType.WOOD);
    }
}