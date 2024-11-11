package net.mehvahdjukaar.every_compat;


import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.mehvahdjukaar.every_compat.api.CompatModule;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.configs.ModConfigs;
import net.mehvahdjukaar.every_compat.configs.ModEntriesConfigs;
import net.mehvahdjukaar.every_compat.dynamicpack.ServerDynamicResourcesHandler;
import net.mehvahdjukaar.every_compat.misc.AllWoodItem;
import net.mehvahdjukaar.every_compat.modules.another_furniture.AnotherFurnitureModule;
import net.mehvahdjukaar.every_compat.modules.beautiful_campfires.BeautifulCampfiresModule;
import net.mehvahdjukaar.every_compat.modules.camp_chair.CampChairModule;
import net.mehvahdjukaar.every_compat.modules.chipped.ChippedModule;
import net.mehvahdjukaar.every_compat.modules.dawn_of_time.DawnOfTimeModule;
import net.mehvahdjukaar.every_compat.modules.decorative_blocks.DecorativeBlocksModule;
import net.mehvahdjukaar.every_compat.modules.exlines.BarkCarpetsModule;
import net.mehvahdjukaar.every_compat.modules.farmersdelight.FarmersDelightModule;
import net.mehvahdjukaar.every_compat.modules.friendsandfoes.FriendsAndFoesModule;
import net.mehvahdjukaar.every_compat.modules.furnish.FurnishModule;
import net.mehvahdjukaar.every_compat.modules.handcrafted.HandcraftedModule;
import net.mehvahdjukaar.every_compat.modules.hearth_and_home.HearthAndHomeModule;
import net.mehvahdjukaar.every_compat.modules.mrcrayfish.BackpackedModule;
import net.mehvahdjukaar.every_compat.modules.mrcrayfish.RefurbishedFurnitureModule;
import net.mehvahdjukaar.every_compat.modules.quark.QuarkModule;
import net.mehvahdjukaar.every_compat.modules.table_top_craft.TableTopCraftModule;
import net.mehvahdjukaar.every_compat.modules.twigs.TwigsModule;
import net.mehvahdjukaar.every_compat.modules.valhelsia_furniture.ValhelsiaFurnitureModule;
import net.mehvahdjukaar.every_compat.modules.villagers_plus.VillagersPlusModule;
import net.mehvahdjukaar.every_compat.type.StoneType;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: MehVahdJukaar
 */
public abstract class EveryCompat {
    public static final String MOD_ID = "everycomp";

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final Logger LOGGER = LogManager.getLogger("Every Compat");

    public static final Map<String, CompatModule> ACTIVE_MODULES = new TreeMap<>();

    public static final List<CompatMod> COMPAT_MODS = new ArrayList<>();
    // all mod that EC directly or indirectly depends on
    public static final Set<String> DEPENDENCIES = new HashSet<>();

    //these are the names of the block types we add wooden variants for
    public static final Map<Class<? extends BlockType>, Set<String>> ENTRY_TYPES = new Object2ObjectOpenHashMap<>();
    public static final Map<Object, CompatModule> ITEMS_TO_MODULES = new Object2ObjectOpenHashMap<>();


    private static final UnsafeModuleDisabler MODULE_DISABLER = new UnsafeModuleDisabler();

    public static void forAllModules(Consumer<CompatModule> action) {
        ACTIVE_MODULES.values().forEach(action);
    }


    protected void commonInit() {


        ModConfigs.init();
        ECNetworking.init();

        ServerDynamicResourcesHandler.INSTANCE.register();
        RegHelper.addItemsToTabsRegistration(this::registerItemsToTabs);
        PlatHelper.addCommonSetup(this::commonSetup);

        BlockSetAPI.addDynamicBlockRegistration(this::registerWoodStuff, WoodType.class);
        BlockSetAPI.addDynamicBlockRegistration(this::registerLeavesStuff, LeavesType.class);
        //StoneTypeRegistry.init();
        //BlockSetAPI.addDynamicBlockRegistration(this::registerStonesStuff, StoneType.class);

        BlockSetAPI.addDynamicRegistration((r, c) -> this.registerItems(r), WoodType.class, BuiltInRegistries.ITEM);
        BlockSetAPI.addDynamicRegistration((r, c) -> this.registerTiles(r), WoodType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE);
        BlockSetAPI.addDynamicRegistration((r, c) -> this.registerEntities(r), WoodType.class, BuiltInRegistries.ENTITY_TYPE);


//!!================================================ Add Other Compat Mods ========================================== \\
        addOtherCompatMod("compatoplenty", "biomesoplenty", List.of("twigs", "farmersdelight", "quark", "woodworks"));
        addOtherCompatMod("compat_makeover", "biomemakeover", List.of("habitat", "farmersdelight", "quark", "decorative_blocks"));
        addOtherCompatMod("decorative_compat", "biomesoplenty", List.of("decorative_blocks"));
        addOtherCompatMod("storagedrawersunlimited", "biomesoplenty", List.of("storagedrawers"));
        addOtherCompatMod("lolmcvbop", "biomesoplenty", List.of("lolmcv"));
        addOtherCompatMod("lolmcvbmo", "biomemakeover", List.of("lolmcv"));
        addOtherCompatMod("natures_delight", "natures_spirit", List.of("farmersdelight"));
        addOtherCompatMod("arts_and_crafts_compat", "arts_and_crafts", List.of("twigs", "decorative_blocks", "farmersdelight", "dramaticdoors"));

        //!! Macaw's Addon
        addOtherCompatMod("macawsbridgesbop", "biomesoplenty", List.of("mcwbridges"));
        addOtherCompatMod("macawbridgesbyg", "byg", List.of("mcwbridges"));
        addOtherCompatMod("mcwfencesbop", "biomesoplenty", List.of("mcwfences"));
        addOtherCompatMod("mcwfencesbyg", "byg", List.of("mcwfences"));
        addOtherCompatMod("macawsroofsbop", "biomesoplenty", List.of("mcwroofs"));
        addOtherCompatMod("macawsroofsbyg", "byg", List.of("mcwroofs"));

        //!! Abnormals Delight
        addOtherCompatMod("abnormals_delight", List.of("autumnity", "upgrade_aquatic",
                "environmental", "atmospheric", "endergetic", "caves_and_chasms"), List.of("farmersdelight"));

//!!=============================================== Add Modules ==================================================== \\
        addModule("another_furniture", () -> AnotherFurnitureModule::new);
        addModule("dawnoftimebuilder", () -> DawnOfTimeModule::new);
        addModule("backpacked", () -> BackpackedModule::new);
        addModule("barkcarpets", () -> BarkCarpetsModule::new); // Exline's
        addModule("bc", () -> BeautifulCampfiresModule::new);
        addModule("campchair", () -> CampChairModule::new);
        addModule("chipped", () -> ChippedModule::new);
        addModule("decorative_blocks", () -> DecorativeBlocksModule::new);
        addModule("friendsandfoes", () -> FriendsAndFoesModule::new);
        addModule("furnish", () -> FurnishModule::new);
        addModule("hearth_and_home", () -> HearthAndHomeModule::new);
        addModule("quark", () -> QuarkModule::new);
        addModule("twigs", () -> TwigsModule::new);
        addModule("refurbished_furniture", () -> RefurbishedFurnitureModule::new);
        addModule("farmersdelight", () -> FarmersDelightModule::new);
        addModule("handcrafted", () -> HandcraftedModule::new);
        addModule("valhelsia_furniture", () -> ValhelsiaFurnitureModule::new);
        addModule("villagersplus", () -> VillagersPlusModule::new);
        addModule("table_top_craft", () -> TableTopCraftModule::new);

//!!==================================================== OTHERS ===================================================== \\
        forAllModules(m -> EveryCompat.LOGGER.info("Loaded {}", m.toString()));


        MODULE_DISABLER.save();

    }

    public static <T extends BlockType> void addEntryType(Class<T> type, String childId) {
        ENTRY_TYPES.computeIfAbsent(type, t -> new HashSet<>()).add(childId);
    }

    private void addOtherCompatMod(String modId, String woodFrom, List<String> blocksFrom) {
        addOtherCompatMod(modId, List.of(woodFrom), blocksFrom);
    }

    private void addOtherCompatMod(String modId, List<String> woodFrom, List<String> blocksFrom) {
        COMPAT_MODS.add(new CompatMod(modId, woodFrom, blocksFrom));
        DEPENDENCIES.add(modId);
        DEPENDENCIES.addAll(woodFrom);
        DEPENDENCIES.addAll(blocksFrom);
    }

    public static boolean OLD_FD = false;

    protected void addModule(String modId, Supplier<Function<String, CompatModule>> moduleFactory) {
        if (PlatHelper.isModLoaded(modId) && MODULE_DISABLER.isModuleOn(modId)) {

            if (modId.equals("farmersdelight")) {
                try {
                    Class.forName("vectorwing.farmersdelight.FarmersDelight");
                } catch (Exception e) {
                    EveryCompat.LOGGER.error("Farmers Delight Refabricated is not installed. Disabling Farmers Delight Module");
                    OLD_FD = true;
                    return;
                }
            }

            CompatModule module = moduleFactory.get().apply(modId);

            EveryCompatAPI.registerModule(module);

            DEPENDENCIES.add(modId);
            DEPENDENCIES.addAll(module.getAlreadySupportedMods());
        }
    }

    public static Collection<CompatMod> getCompatMods() {
        return COMPAT_MODS;
    }

    public static final Supplier<AllWoodItem> ALL_WOODS = RegHelper.registerItem(res("all_woods"), AllWoodItem::new);

    @Nullable
    public static final RegSupplier<CreativeModeTab> MOD_TAB =
            ModConfigs.TAB_ENABLED.get() ?
                    RegHelper.registerCreativeModeTab(res(MOD_ID),
                            true,
                            builder -> builder.icon(() -> ALL_WOODS.get().getDefaultInstance())
                                    .backgroundTexture(CreativeModeTab.createTextureLocation("item_search"))
                                    //.alignedRight()
                                    .title(Component.translatable("itemGroup.everycomp.everycomp"))
                                    .build()) : null;


    public void commonSetup() {
        if (PlatHelper.isModLoaded("chipped")) {
            EveryCompat.LOGGER.warn("Chipped is installed. The mod on its own adds a ludicrous amount of blocks. With Every Compat this can easily explode. You have been warned");
        }
        //log registered stuff size
        int newSize = BuiltInRegistries.BLOCK.size();
        int am = newSize - prevRegSize;
        float p = (am / (float) newSize) * 100f;
        EveryCompat.LOGGER.info("Registered {} compat blocks making up {}% of total blocks registered", am, String.format("%.2f", p));
        if (p > 33) {
            CompatModule bloated = ACTIVE_MODULES.values().stream()
                    .max(Comparator.comparing(CompatModule::bloatAmount)).get();
            EveryCompat.LOGGER.error("Every Compat registered blocks make up more than one third of your registered blocks, taking up memory and load time.");
            EveryCompat.LOGGER.error("You might want to uninstall some mods, biggest offender was {} ({} blocks)", bloated.getModName().toUpperCase(Locale.ROOT), bloated.bloatAmount());
        }

        if (am == 0) {
            EveryCompat.LOGGER.error("EVERY COMPAT REGISTERED 0 BLOCKS! This means that you dont need the mod and should remove it!");
        }
        forAllModules(CompatModule::onModSetup);

    }

    private int prevRegSize;

    public void registerWoodStuff(Registrator<Block> event, Collection<WoodType> woods) {
        ModEntriesConfigs.initEarlyButNotSuperEarly(); // add wood stuff once its ready
        prevRegSize = BuiltInRegistries.BLOCK.size();
        LOGGER.info("Registering Compat Wood Blocks");
        forAllModules(m -> m.registerWoodBlocks(event, woods));
    }


    public void registerLeavesStuff(Registrator<Block> event, Collection<LeavesType> leaves) {
        LOGGER.info("Registering Compat Leaves Blocks");
        forAllModules(m -> m.registerLeavesBlocks(event, leaves));
    }

    public void registerStonesStuff(Registrator<Block> event, Collection<StoneType> stones) {
        LOGGER.info("Registering Compat Stones Blocks");
        forAllModules(m -> m.registerStonesBlocks(event, stones));
    }

    protected void registerItems(Registrator<Item> event) {
        forAllModules(m -> m.registerItems(event));
    }

    protected void registerTiles(Registrator<BlockEntityType<?>> event) {
        forAllModules(m -> m.registerTiles(event));
    }

    protected void registerEntities(Registrator<EntityType<?>> event) {
        forAllModules(m -> m.registerEntities(event));
    }


    public record CompatMod(String modId, List<String> woodsFrom, List<String> blocksFrom) {

        public CompatMod(String modId, String woodFrom, List<String> blocksFrom) {
            this(modId, List.of(woodFrom), blocksFrom);
        }

    }

    private void registerItemsToTabs(RegHelper.ItemToTabEvent event) {
        if (ModConfigs.TAB_ENABLED.get()) {
            ResourceKey<CreativeModeTab> tab = EveryCompat.MOD_TAB.getHolder().unwrapKey().get();
            Map<BlockType, List<Item>> typeToEntrySet = new LinkedHashMap<>();
            for (var r : BlockSetAPI.getRegistries()) {
                for (var type : r.getValues()) {
                    forAllModules(m -> {
                        typeToEntrySet.computeIfAbsent(type, j -> new ArrayList<>())
                                .addAll(m.getAllItemsOfType(type));
                    });
                }
            }
            for (var e : typeToEntrySet.values()) {
                LinkedHashSet<ItemLike> list  = new LinkedHashSet<>(e);
                event.add(tab, list.toArray(ItemLike[]::new));
            }
        } else {
            forAllModules(m -> m.registerItemsToExistingTabs(event));
        }
    }

    public static boolean doChildrenExistFor(WoodType w, String... blockTypes) {
        for (String type : blockTypes) {
            if (w.getBlockOfThis(type) == null) return false;
        }
        return true;
    }

    public static boolean doChildrenExistFor(WoodType w, SimpleEntrySet<WoodType, ?> blockType) {
        return (blockType.blocks.get(w) != null);
    }
}
