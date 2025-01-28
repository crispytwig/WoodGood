package net.mehvahdjukaar.every_compat.modules.neoforge.mrcrayfish;

import com.mrcrayfish.furniture.refurbished.block.*;
import com.mrcrayfish.furniture.refurbished.client.renderer.blockentity.CeilingFanBlockEntityRenderer;
import com.mrcrayfish.furniture.refurbished.crafting.StackedIngredient;
import com.mrcrayfish.furniture.refurbished.crafting.WorkbenchContructingRecipe;
import com.mrcrayfish.furniture.refurbished.item.MailboxItem;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.dynamicpack.ClientDynamicResourcesHandler;
import net.mehvahdjukaar.every_compat.misc.SpriteHelper;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.resources.RecipeTemplate;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.recipe.BlockTypeSwapIngredient;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//SUPPORT: v1.0.8+
//+ IMPORTANT:
// Creating/loading a world where a crash happened is due to Line 509.
// The reason is unknown. Separated into 2 FORGE/FABRIC folders fixed the problem for now.
public class RefurbishedFurnitureModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> chairs;
    public final SimpleEntrySet<WoodType, Block> tables;
    public final SimpleEntrySet<WoodType, Block> darkFans;
    public final SimpleEntrySet<WoodType, Block> lightFans;
    public final SimpleEntrySet<WoodType, Block> toilets;
    public final SimpleEntrySet<WoodType, Block> crates;
    public final SimpleEntrySet<WoodType, Block> mailboxes;
    public final SimpleEntrySet<WoodType, Block> jars;
    public final SimpleEntrySet<WoodType, Block> kitchen_cabinetry;
    public final SimpleEntrySet<WoodType, Block> kitchen_drawer;
    public final SimpleEntrySet<WoodType, Block> kitchen_sink;
    public final SimpleEntrySet<WoodType, Block> kitchen_storage_cabinet;
    public final SimpleEntrySet<WoodType, Block> storage_cabinet;
    public final SimpleEntrySet<WoodType, Block> basin;
    public final SimpleEntrySet<WoodType, Block> bath;
    public final SimpleEntrySet<WoodType, Block> lattice_fence;
    public final SimpleEntrySet<WoodType, Block> lattice_fence_gate;
    public final SimpleEntrySet<WoodType, Block> desk;
    public final SimpleEntrySet<WoodType, Block> cutting_board;
    public final SimpleEntrySet<WoodType, Block> drawer;
    public final SimpleEntrySet<LeavesType, Block> hedges;


    public RefurbishedFurnitureModule(String modId) {
        super(modId, "rfm");
        ResourceLocation tab = modRes("creative_tab");

        chairs = SimpleEntrySet.builder(WoodType.class, "chair",
                        getModBlock("oak_chair"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new ChairBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of().strength(2.0F))
                        )
                )
                .addRecipe(modRes("constructing/oak_chair"))
                .setTabKey(tab)
                .addTexture(modRes("block/oak_chair"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("general"), Registries.ITEM)
                .build();
        this.addEntry(chairs);

        tables = SimpleEntrySet.builder(WoodType.class, "table",
                        getModBlock("oak_table"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new TableBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of()
                                .strength(2.0F))
                        )
                )
                .addRecipe(modRes("constructing/oak_table"))
                .setTabKey(tab)
                .addTexture(modRes("block/oak_table"))
                .addTexture(modRes("block/oak_particle"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("tuckable"), Registries.BLOCK)
                .addTag(modRes("general"), Registries.ITEM)
                .build();
        this.addEntry(tables);

        darkFans = SimpleEntrySet.builder(WoodType.class, "dark_ceiling_fan",
                        getModBlock("oak_dark_ceiling_fan"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CeilingFanBlock(w.toVanillaOrOak(),
                                MetalType.DARK,
                                BlockBehaviour.Properties.of().mapColor(w.planks.defaultMapColor())
                                        .strength(0.8F).sound(w.getSound()).lightLevel(CeilingFanBlock::light)
                        )
                )
                .defaultRecipe()
                .addRecipe(modRes("constructing/oak_dark_ceiling_fan"))
                .addTile(getModTile("ceiling_fan"))
                .setTabKey(tab)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("electronics"), Registries.ITEM)
                .addTag(modRes("bedroom"), Registries.ITEM)
                .addTextureM(modRes("block/oak_dark_ceiling_fan"),
                        EveryCompat.res("block/rfm/oak_ceiling_fan_m"))
                .setRenderType(RenderLayer.TRANSLUCENT)
                .build();
        this.addEntry(darkFans);

        lightFans = SimpleEntrySet.builder(WoodType.class, "light_ceiling_fan",
                        getModBlock("oak_light_ceiling_fan"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CeilingFanBlock(w.toVanillaOrOak(),
                                MetalType.DARK,
                                BlockBehaviour.Properties.of().mapColor(w.planks.defaultMapColor())
                                        .strength(0.8F).sound(w.getSound()).lightLevel(CeilingFanBlock::light)
                        )
                )
                .addRecipe(modRes("constructing/oak_light_ceiling_fan"))
                .addTile(getModTile("ceiling_fan"))
                .setTabKey(tab)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("electronics"), Registries.ITEM)
                .addTag(modRes("bedroom"), Registries.ITEM)
                .addTextureM(modRes("block/oak_light_ceiling_fan"),
                        EveryCompat.res("block/rfm/oak_ceiling_fan_m"))
                .setRenderType(RenderLayer.TRANSLUCENT)
                .build();
        this.addEntry(lightFans);


        crates = SimpleEntrySet.builder(WoodType.class, "crate",
                        getModBlock("oak_crate"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CrateBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of()
                                .forceSolidOn().strength(2.5F))
                        )
                )
                .addRecipe(modRes("constructing/oak_crate"))
                .setTabKey(tab)
                .addTile(getModTile("crate"))
                .addTexture(modRes("block/oak_crate"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("storage"), Registries.ITEM)
                .addTag(modRes("outdoors"), Registries.ITEM)
                .build();
        this.addEntry(crates);

        mailboxes = SimpleEntrySet.builder(WoodType.class, "mail_box",
                        getModBlock("oak_mail_box"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new MailboxBlock(w.toVanillaOrOak(), addWoodPropNoFire(w, BlockBehaviour.Properties.of()
                                .strength(2.5F))
                        )
                )
                .addRecipe(modRes("constructing/oak_mail_box"))
                .addCustomItem((woodType, block, properties) -> new MailboxItem(block, properties))
                .setTabKey(tab)
                .addTile(getModTile("mail_box"))
                .addTextureM(modRes("block/oak_mail_box"),
                        EveryCompat.res("block/rfm/oak_mail_box_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("outdoors"), Registries.ITEM)
                .addTag(modRes("storage"), Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .build();
        this.addEntry(mailboxes);

        toilets = SimpleEntrySet.builder(WoodType.class, "toilet",
                        getModBlock("oak_toilet"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenToiletBlock(w.toVanillaOrOak(), BlockBehaviour.Properties.of().mapColor(w.planks.defaultMapColor())
                                .strength(3.5f).sound(SoundType.STONE))
                )
                .addRecipe(modRes("constructing/oak_toilet"))
                .setTabKey(tab)
                .addTile(getModTile("toilet"))
                .addTextureM(modRes("block/oak_toilet"),
                        EveryCompat.res("block/rfm/oak_toilet_m"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("wooden_toilets"), Registries.ITEM)
                .addTag(modRes("bathroom"), Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .build();
        this.addEntry(toilets);

        jars = SimpleEntrySet.builder(WoodType.class, "storage_jar",
                        getModBlock("oak_storage_jar"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new StorageJarBlock(w.toVanillaOrOak(), BlockBehaviour.Properties.of()
                                .mapColor(w.planks.defaultMapColor())
                                .instrument(NoteBlockInstrument.HAT).strength(1.0F).sound(SoundType.GLASS))
                )
                .addRecipe(modRes("constructing/oak_storage_jar"))
                .setTabKey(tab)
                .addTile(getModTile("storage_jar"))
                .addTextureM(modRes("block/oak_storage_jar"),
                        EveryCompat.res("block/rfm/oak_storage_jar_m"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("storage"), Registries.ITEM)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .build();
        this.addEntry(jars);

        kitchen_cabinetry = SimpleEntrySet.builder(WoodType.class, "kitchen_cabinetry",
                        getModBlock("oak_kitchen_cabinetry"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenKitchenCabinetryBlock(w.toVanillaOrOak(),
                                addWoodProp(w, BlockBehaviour.Properties.of()).forceSolidOn().strength(2.0f))
                )
                .addRecipe(modRes("constructing/oak_kitchen_cabinetry"))
                .setTabKey(tab)
                .addTextureM(modRes("block/oak_kitchen_cabinetry"),
                        EveryCompat.res("block/rfm/oak_kitchen_cabinetry_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("wooden_kitchen_cabinetry"), Registries.ITEM)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .build();
        this.addEntry(kitchen_cabinetry);

        kitchen_drawer = SimpleEntrySet.builder(WoodType.class, "kitchen_drawer",
                        getModBlock("oak_kitchen_drawer"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenKitchenDrawerBlock(w.toVanillaOrOak(),
                                addWoodProp(w, BlockBehaviour.Properties.of()).forceSolidOn().strength(2.5f))
                )
                .addRecipe(modRes("constructing/oak_kitchen_drawer"))
                .setTabKey(tab)
                .addTile(getModTile("kitchen_drawer"))
                .addTextureM(modRes("block/oak_kitchen_drawer"),
                        EveryCompat.res("block/rfm/oak_kitchen_drawer_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("storage"), Registries.ITEM)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .addTag(modRes("wooden_kitchen_drawers"), Registries.ITEM)
                .build();
        this.addEntry(kitchen_drawer);

        kitchen_sink = SimpleEntrySet.builder(WoodType.class, "kitchen_sink",
                        getModBlock("oak_kitchen_sink"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenKitchenSinkBlock(w.toVanillaOrOak(),
                                addWoodProp(w, BlockBehaviour.Properties.of()).forceSolidOn().strength(2.5f))
                )
                .addRecipe(modRes("constructing/oak_kitchen_sink"))
                .setTabKey(tab)
                .addTile(getModTile("kitchen_sink"))
                .addTextureM(modRes("block/oak_kitchen_sink"),
                        EveryCompat.res("block/rfm/oak_kitchen_sink_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("wooden_kitchen_sinks"), Registries.ITEM)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .addTag(modRes("storage"), Registries.ITEM)
                .build();
        this.addEntry(kitchen_sink);

        kitchen_storage_cabinet = SimpleEntrySet.builder(WoodType.class, "kitchen_storage_cabinet",
                        getModBlock("oak_kitchen_storage_cabinet"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenKitchenStorageCabinetBlock(w.toVanillaOrOak(),
                                addWoodProp(w, BlockBehaviour.Properties.of()).forceSolidOn().strength(2.5f))
                )
                .addRecipe(modRes("constructing/oak_kitchen_storage_cabinet"))
                .setTabKey(tab)
                .addTile(getModTile("cabinet"))
                .addTextureM(modRes("block/oak_kitchen_storage_cabinet"),
                        EveryCompat.res("block/rfm/oak_kitchen_storage_cabinet_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("wooden_kitchen_storage_cabinets"), Registries.ITEM)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .addTag(modRes("storage"), Registries.ITEM)
                .build();
        this.addEntry(kitchen_storage_cabinet);

        storage_cabinet = SimpleEntrySet.builder(WoodType.class, "storage_cabinet",
                        getModBlock("oak_storage_cabinet"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenStorageCabinetBlock(w.toVanillaOrOak(),
                                addWoodProp(w, BlockBehaviour.Properties.of()).forceSolidOn().strength(2.5f))
                )
                .addRecipe(modRes("constructing/oak_storage_cabinet"))
                .setTabKey(tab)
                .addTile(getModTile("cabinet"))
                .addTextureM(modRes("block/oak_storage_cabinet"),
                        EveryCompat.res("block/rfm/oak_storage_cabinet_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("general"), Registries.ITEM)
                .addTag(modRes("bedroom"), Registries.ITEM)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .addTag(modRes("storage"), Registries.ITEM)
                .build();
        this.addEntry(storage_cabinet);

        basin = SimpleEntrySet.builder(WoodType.class, "basin",
                        getModBlock("oak_basin"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenBasinBlock(w.toVanillaOrOak(), BlockBehaviour.Properties.of()
                                .mapColor(w.planks.defaultMapColor())
                                .strength(3.5f).sound(SoundType.STONE)
                        )
                )
                .addRecipe(modRes("constructing/oak_basin"))
                .setTabKey(tab)
                .addTile(getModTile("basin"))
                .addTextureM(modRes("block/oak_basin"),
                        EveryCompat.res("block/rfm/oak_basin_m"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("wooden_basins"), Registries.ITEM)
                .addTag(modRes("bathroom"), Registries.ITEM)
                .build();
        this.addEntry(basin);

        bath = SimpleEntrySet.builder(WoodType.class, "bath",
                        getModBlock("oak_bath"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new WoodenBathBlock(w.toVanillaOrOak(), BlockBehaviour.Properties.of()
                                .mapColor(w.planks.defaultMapColor())
                                .strength(3.5f).sound(SoundType.STONE)
                        )
                )
                .addRecipe(modRes("constructing/oak_bath"))
                .setTabKey(tab)
                .addTile(getModTile("bath"))
                .addTextureM(modRes("block/oak_bath"),
                        EveryCompat.res("block/rfm/oak_bath_m"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("bathroom"), Registries.ITEM)
                .addTag(modRes("wooden_baths"), Registries.ITEM)
                .build();
        this.addEntry(bath);

        lattice_fence = SimpleEntrySet.builder(WoodType.class, "lattice_fence",
                        getModBlock("oak_lattice_fence"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new LatticeFenceBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of()
                                .strength(2.0f).forceSolidOn())
                        )
                )
                .addRecipe(modRes("constructing/oak_lattice_fence"))
                .setTabKey(tab)
                .addTile(getModTile("storage_jar"))
                .addTexture(modRes("block/oak_lattice_fence"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(BlockTags.FENCES, Registries.BLOCK)
                .addTag(BlockTags.WOODEN_FENCES, Registries.BLOCK)
                .addTag(modRes("outdoors"), Registries.ITEM)
                .addTag(BlockTags.FENCES, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .build();
        this.addEntry(lattice_fence);

        lattice_fence_gate = SimpleEntrySet.builder(WoodType.class, "lattice_fence_gate",
                        getModBlock("oak_lattice_fence_gate"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new LatticeFenceGateBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of())
                                .strength(2.0f)
                        )
                )
                .addRecipe(modRes("constructing/oak_lattice_fence_gate"))
                .setTabKey(tab)
                .addTile(getModTile("storage_jar"))
                .addTextureM(modRes("block/oak_lattice_fence_gate"),
                        EveryCompat.res("block/rfm/oak_lattice_fence_gate_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(BlockTags.FENCE_GATES, Registries.BLOCK)
                .addTag(BlockTags.UNSTABLE_BOTTOM_CENTER, Registries.BLOCK)
                .addTag(modRes("outdoors"), Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .build();
        this.addEntry(lattice_fence_gate);

        desk = SimpleEntrySet.builder(WoodType.class, "desk",
                        getModBlock("oak_desk"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new DeskBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of())
                                .strength(2.0f).forceSolidOn()
                        )
                )
                .addRecipe(modRes("constructing/oak_desk"))
                .setTabKey(tab)
                .addTexture(modRes("block/oak_desk"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("tuckable"), Registries.BLOCK)
                .addTag(modRes("general"), Registries.ITEM)
                .addTag(modRes("bedroom"), Registries.ITEM)
                .build();
        this.addEntry(desk);

        cutting_board = SimpleEntrySet.builder(WoodType.class, "cutting_board",
                        getModBlock("oak_cutting_board"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CuttingBoardBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of())
                                .strength(1.5f)
                        )
                )
                .addRecipe(modRes("constructing/oak_cutting_board"))
                .setTabKey(tab)
                .addTile(getModTile("cutting_board"))
                .addTexture(modRes("block/oak_cutting_board"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("kitchen"), Registries.ITEM)
                .build();
        this.addEntry(cutting_board);

        drawer = SimpleEntrySet.builder(WoodType.class, "drawer",
                        getModBlock("oak_drawer"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new DrawerBlock(w.toVanillaOrOak(), addWoodProp(w, BlockBehaviour.Properties.of())
                                .strength(2.5f).forceSolidOn()
                        )
                )
                .addRecipe(modRes("constructing/oak_drawer"))
                .setTabKey(tab)
                .addTile(getModTile("drawer"))
                .addTexture(modRes("block/oak_drawer"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("storage"), Registries.ITEM)
                .addTag(modRes("bedroom"), Registries.ITEM)
                .addTag(modRes("general"), Registries.ITEM)
                .build();
        this.addEntry(drawer);

        hedges = SimpleEntrySet.builder(LeavesType.class, "hedge",
                        getModBlock("oak_hedge"), () -> LeavesTypeRegistry.OAK_TYPE,
                        l -> new HedgeBlock(LeafType.OAK, BlockBehaviour.Properties.of().strength(0.5f)
                                .sound(SoundType.AZALEA_LEAVES))
                )
                .requiresChildren("leaves") // Textures
                .addModelTransform(m -> m.replaceWithTextureFromChild("minecraft:block/oak_leaves",
                        "leaves", SpriteHelper.LOOKS_LIKE_LEAF_TEXTURE))
                .addRecipe(modRes("constructing/oak_hedge"))
                .setTabKey(tab)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT_MIPPED)
                .copyParentTint()
                .build();
        this.addEntry(hedges);
    }

    @Override
    public void addDynamicClientResources(ClientDynamicResourcesHandler handler, ResourceManager manager) {
        super.addDynamicClientResources(handler, manager);

        // code copied from ResourceUtils.addStandardResources
        StaticResource darkBlade = StaticResource.getOrLog(manager, ResType.MODELS.getPath(
                modRes("extra/oak_dark_ceiling_fan_blade")
        ));
        addFanModels(handler, manager, darkBlade, darkFans);
        StaticResource lightBlade = StaticResource.getOrLog(manager, ResType.MODELS.getPath(
                modRes("extra/oak_light_ceiling_fan_blade")
        ));
        addFanModels(handler, manager, lightBlade, lightFans);

    }

    private void addFanModels(ClientDynamicResourcesHandler handler, ResourceManager manager, StaticResource darkBlade, SimpleEntrySet<WoodType, Block> darkFans) {
        darkFans.blocks.forEach((w, b) -> {
            try {
                handler.addSimilarJsonResource(manager, darkBlade, s ->
                        s.replace("oak", w.getAppendableId())
                                .replace("texture\": \"refurbished_furniture:block", "texture\": \"everycomp:block/rfm"));

            } catch (Exception exception) {
                EveryCompat.LOGGER.error("Failed to add {} model json file:", b, exception);
            }
        });
    }

    @Override
    public void onClientSetup() {
        super.onClientSetup();
        darkFans.blocks.forEach((key, value) -> {
            ResourceLocation res = EveryCompat.res("extra/" + key.getAppendableId() + "_dark_ceiling_fan_blade");
            CeilingFanBlockEntityRenderer.registerFanBlade(value, () ->
                    ClientHelper.getModel(Minecraft.getInstance().getModelManager(),
                            RenderUtil.getStandaloneModelLocation(res)));
        });
        lightFans.blocks.forEach((key, value) -> {
            ResourceLocation res = EveryCompat.res("extra/" + key.getAppendableId() + "_light_ceiling_fan_blade");
            CeilingFanBlockEntityRenderer.registerFanBlade(value, () ->
                    ClientHelper.getModel(Minecraft.getInstance().getModelManager(),
                            RenderUtil.getStandaloneModelLocation(res)));
        });
    }

    @Override
    public void onClientInit() {
        super.onClientInit();
        ClientHelper.addSpecialModelRegistration(event -> darkFans.blocks.keySet().forEach(w -> {
            event.register(EveryCompat.res("extra/" + w.getAppendableId() + "_dark_ceiling_fan_blade"));
            event.register(EveryCompat.res("extra/" + w.getAppendableId() + "_light_ceiling_fan_blade"));
        }));
    }

    public static BlockBehaviour.Properties addWoodProp(WoodType w, BlockBehaviour.Properties p) {
        if (w.canBurn()) p.ignitedByLava();
        p.mapColor(w.planks.defaultMapColor()).sound(w.getSound()).instrument(NoteBlockInstrument.BASS);
        return p;
    }

    public static BlockBehaviour.Properties addWoodPropNoFire(WoodType w, BlockBehaviour.Properties p) {
        p.mapColor(w.planks.defaultMapColor()).sound(w.getSound()).instrument(NoteBlockInstrument.BASS);
        return p;
    }

    @Override
    public void onModSetup() {
        super.onModSetup();

        RecipeTemplate.register(WorkbenchContructingRecipe.class, (old, from, to) -> {
            List<StackedIngredient> newList = convertStackedIngredients(old.getMaterials(), from, to);
            ItemStack originalResult = old.getResultItem(RegistryAccess.EMPTY);
            ItemStack newResult = RecipeTemplate.convertItemStack(originalResult, from, to);
            if (newResult == null) {
                throw new UnsupportedOperationException("Failed to convert recipe result");
            } else {
                NonNullList<StackedIngredient> ingredients = NonNullList.of(StackedIngredient.EMPTY, newList.toArray(StackedIngredient[]::new));
                return new WorkbenchContructingRecipe(ingredients, newResult, old.showNotification());
            }
        });
    }


    private static <R extends Recipe<?>, T extends BlockType> @NotNull List<StackedIngredient> convertStackedIngredients(
            NonNullList<StackedIngredient> or, T from, T to) {

        List<StackedIngredient> newList = new ArrayList<>();
        for (StackedIngredient si : or) {
            if (si.ingredient().isEmpty()) {
                newList.add(si);
            } else {
                newList.add(StackedIngredient.of(
                        BlockTypeSwapIngredient.create(si.ingredient(), from, to),
                        si.count()));
            }
        }
        return newList;
    }

}
