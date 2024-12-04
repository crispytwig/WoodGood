package net.mehvahdjukaar.every_compat.neoforge;

import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.EveryCompatClient;
import net.mehvahdjukaar.every_compat.api.CompatModule;
import net.mehvahdjukaar.every_compat.configs.ModConfigs;

import net.mehvahdjukaar.every_compat.modules.farmersdelight.FarmersDelightModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.abnormal.BoatLoadModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.abnormal.WoodworksModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.absent_by_design.AbsentByDesignModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.architect_palette.ArchitectsPaletteModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.beautify_decorate.BeautifyDecorateModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.builders_delight.BuildersDelightModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.buildersaddition.BuildersAdditionModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.building_but_better.BuildingButBetterModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.corail_pillar.CorailPillarModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.decoration_delight.DecorationDelightModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.dramaticdoors.DramaticDoorsMacawModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.dramaticdoors.DramaticDoorsModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.functional_storage.FunctionalStorageModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.infinitybuttons.InfinityButtonsModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.just_a_raft.JustARaftModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.lauchs.LauchsShuttersModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.lieonlion.MoreChestVariantsModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.lightmans_currency.LightmansCurrencyModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.mcaw.*;
import net.mehvahdjukaar.every_compat.modules.neoforge.missing_wilds.MissingWildModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.more_crafting_tables_forge.MoreCraftingTablesForForgeModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.mosaic_carpentry.MosaicCarpentryModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.mrcrayfish_furniture.MightyMailModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.oreberries_replanted.OreberriesReplantedModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.pokecube.PokecubeAOIModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.premium_wood.PremiumWoodModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.redeco.ReDecoModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.regions_unexplored.RegionsUnexploredModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.storagedrawers.StorageDrawersModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.timber_frames.TimberFramesModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.tropicraft.TropicraftModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.twilightforest.TwilightForestModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.valhelsia.ValhelsiaStructuresModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.variants.VariantCraftingTablesModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.variants.VariantVanillaBlocksModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.woodster.WoodsterModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.workshop.WorkshopForHandsomeAdventurerModule;
import net.mehvahdjukaar.every_compat.modules.neoforge.xerca.XercaModule;
import net.mehvahdjukaar.every_compat.modules.stylish_stiles.StylishStilesModule;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerNegotiationEvent;

import java.lang.ref.WeakReference;

/**
 * Author: MehVahdJukaar
 */
@Mod(EveryCompat.MOD_ID)
public class EveryCompatForge extends EveryCompat {
    private static WeakReference<IEventBus> BUS = new WeakReference<>(null);

    public EveryCompatForge(IEventBus bus) {
        RegHelper.startRegisteringFor(bus);
        BUS = new WeakReference<>(bus);
        this.commonInit();

//!!================================================ Add Modules ==================================================== \\
        addIfLoaded("absentbydesign", () -> AbsentByDesignModule::new);
        addIfLoaded("architects_palette", () -> ArchitectsPaletteModule::new);
        addIfLoaded("beautify", () -> BeautifyDecorateModule::new);
        addIfLoaded("boatload", () -> BoatLoadModule::new);
        addIfLoaded("buildersaddition", () -> BuildersAdditionModule::new);
        addIfLoaded("bbb", () -> BuildingButBetterModule::new);
        addIfLoaded("buildersdelight", () -> BuildersDelightModule::new);
        addIfLoaded("corail_pillar", () -> CorailPillarModule::new);
//        addModule("create", () -> CreateModule::new); //!! NOT AVAILABLE
        addIfLoaded("decoration_delight", () -> DecorationDelightModule::new);
        addIfLoaded("dramaticdoors", () -> DramaticDoorsModule::new);
        addIfLoaded("farmersdelight", () -> FarmersDelightModule::new);
        addIfLoaded("functionalstorage", () -> FunctionalStorageModule::new);
        addIfLoaded("infinitybuttons", () -> InfinityButtonsModule::new);
        addIfLoaded("justaraftmod", () -> JustARaftModule::new);
        addIfLoaded("lolmcv", () -> MoreChestVariantsModule::new);
        addIfLoaded("mctb", () -> MoreCraftingTablesForForgeModule::new);
        addIfLoaded("mighty_mail", () -> MightyMailModule::new);
        addIfLoaded("missingwilds", () -> MissingWildModule::new);
        addIfLoaded("mosaic_carpentry", () -> MosaicCarpentryModule::new);
        addIfLoaded("oreberriesreplanted", () -> OreberriesReplantedModule::new);
        addIfLoaded("lightmanscurrency", () -> LightmansCurrencyModule::new);
        addIfLoaded("pokecube_legends", () -> PokecubeAOIModule::new);
        addIfLoaded("premium_wood", () -> PremiumWoodModule::new);
        addIfLoaded("redeco", () -> ReDecoModule::new);
        addIfLoaded("regions_unexplored", () -> RegionsUnexploredModule::new);
        addIfLoaded("shutter", () -> LauchsShuttersModule::new);
        addIfLoaded("stylishstiles", () -> StylishStilesModule::new);
        addIfLoaded("storagedrawers", () -> StorageDrawersModule::new);
        addIfLoaded("timber_frames", () -> TimberFramesModule::new);
        addIfLoaded("tropicraft", () -> TropicraftModule::new);
        addIfLoaded("twilightforest", () -> TwilightForestModule::new);
        addIfLoaded("valhelsia_structures", () -> ValhelsiaStructuresModule::new);
        addIfLoaded("variantvanillablocks", () -> VariantVanillaBlocksModule::new);
        addIfLoaded("vct", () -> VariantCraftingTablesModule::new);
        addIfLoaded("woodster", () -> WoodsterModule::new);
        addIfLoaded("woodworks", () -> WoodworksModule::new);
        addIfLoaded("workshop_for_handsome_adventurer", () -> WorkshopForHandsomeAdventurerModule::new);
        addIfLoaded("xercamod", () -> XercaModule::new);
        if (PlatHelper.isModLoaded("mcwdoors")) {
            addIfLoaded("dramaticdoors", () -> DramaticDoorsMacawModule::new);
        }

        // ========================================= Macaw's ======================================================== \\
        addIfLoaded("mcwbridges", () -> MacawBridgesModule::new);
        addIfLoaded("mcwdoors", () -> MacawDoorsModule::new);
        addIfLoaded("mcwfences", () -> MacawFencesModule::new);
        addIfLoaded("mcwfurnitures", () -> MacawFurnitureModule::new);
        addIfLoaded("mcwlights", () -> MacawLightsModule::new);
        addIfLoaded("mcwpaths", () -> MacawPathsModule::new);
        addIfLoaded("mcwroofs", () -> MacawRoofsModule::new);
        addIfLoaded("mcwtrpdoors", () -> MacawTrapdoorsModule::new);
        addIfLoaded("mcwwindows", () -> MacawWindowsModule::new);
        addIfLoaded("mcwstairs", () -> MacawStairsModule::new);

//!!============================================= DISABLED FOR A REASON ============================================= \\

//        addModule("graveyard", () -> GraveyardModule::new); // Disabled until custom block models work
//        addModule("productivebees", () -> ProductiveBeesModule::new); //WIP: class for both beehive have major changes

//!!================================================= OTHERS ======================================================== \\
        NeoForge.EVENT_BUS.register(this);

        forAllModules(CompatModule::onModInit);

        if (PlatHelper.getPhysicalSide().isClient()) {
            EveryCompatClient.init();
        }
    }

    public static IEventBus getModEventBus() {
        return BUS.get();
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemTooltipEvent(ItemTooltipEvent event) {
        EveryCompatClient.onItemTooltip(event.getItemStack(), event.getContext(), event.getFlags(), event.getToolTip());
    }

    /*
    @SubscribeEvent
    public void onRemap(MissingMappingsEvent event) {
        for (var mapping : event.getMappings(Registries.BLOCK_ENTITY_TYPE, EveryCompat.MOD_ID)) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();
            for (var m : EveryCompat.ACTIVE_MODULES) {
                if (path.startsWith(m.shortenedId() + "_")) {
                    String newPath = path.substring((m.shortenedId() + "_").length());
                    ResourceLocation newId = ResourceLocation.parse(m.getModId(), newPath);
                    Optional<BlockEntityType<?>> optional = BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(newId);
                    optional.ifPresent(mapping::remap);
                    break;
                }
            }
        }
    }*/


    @SubscribeEvent
    public void onPlayerNegotiation(PlayerNegotiationEvent playerNegotiationEvent) {
        if (ModConfigs.CHECK_PACKET.get()) {
            //TODO: add back
            /*
            ((ChannelHandlerImpl) ECNetworking.CHANNEL).channel.sendTo(new ECNetworking.S2CModVersionCheckMessage(),
                    playerNegotiationEvent.getConnection(),
                    NetworkDirection.LOGIN_TO_CLIENT
            );*/
        }
    }
}
