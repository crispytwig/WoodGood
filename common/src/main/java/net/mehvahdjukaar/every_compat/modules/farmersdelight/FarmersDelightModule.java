package net.mehvahdjukaar.every_compat.modules.farmersdelight;

import com.google.gson.JsonObject;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.api.TabAddMode;
import net.mehvahdjukaar.every_compat.dynamicpack.ServerDynamicResourcesHandler;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

// SUPPORT: v1.2.6+
// SUPPORT: FABRIC-v2.2.7+
public class FarmersDelightModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> cabinets;

    public FarmersDelightModule(String modId) {
        super(modId, "fd");

        cabinets = SimpleEntrySet.builder(WoodType.class, "cabinet",
                        getModBlock("oak_cabinet"), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL))
                )
                .addCustomItem((w, block, p) -> new FuelBlockItem(block, ModItems.basicItem(), 300))
//                .addTag(modRes("cabinets"), Registries.BLOCK)
                .addTag(modRes("cabinets"), Registries.ITEM)
                .addTag(modRes("cabinets/wooden"), Registries.ITEM)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .defaultRecipe()
                .addTile(getModTile("cabinet"))
                .setTabKey(modRes( "farmersdelight"))
                .setTabMode(TabAddMode.AFTER_SAME_TYPE)
                .createPaletteFromPlanks(Palette::increaseDown)
                .addTexture(EveryCompat.res("block/oak_cabinet_front"))
                .addTexture(EveryCompat.res("block/oak_cabinet_side"))
                .addTexture(EveryCompat.res("block/oak_cabinet_top"))
                .addTextureM(EveryCompat.res("block/oak_cabinet_front_open"), EveryCompat.res("block/oak_cabinet_front_open_m"))
                .build();
        this.addEntry(cabinets);
    }

    @Override
    // Recipes
    public void addDynamicServerResources(ServerDynamicResourcesHandler handler, ResourceManager manager) {
        super.addDynamicServerResources(handler, manager);

        // Creating cutting_board recipes
        for (var woodType : WoodTypeRegistry.getTypes()) {
            if (woodType.isVanilla()) continue;

            createCuttingRecipe("door", woodType.getBlockOfThis("door"), woodType.planks,
                    woodType, handler, manager);
            createCuttingRecipe("hanging_sign", woodType.getBlockOfThis("hanging_sign"), woodType.planks,
                    woodType, handler, manager);
            createCuttingRecipe("sign", woodType.getBlockOfThis("sign"), woodType.planks,
                    woodType, handler, manager);
            createCuttingRecipe("trapdoor", woodType.getBlockOfThis("trapdoor"), woodType.planks,
                    woodType, handler, manager);
            createCuttingRecipe("log", woodType.log, woodType.getBlockOfThis("stripped_log"),
                    woodType, handler, manager);
            createCuttingRecipe("wood", woodType.getBlockOfThis("wood"), woodType.getBlockOfThis("stripped_wood"),
                    woodType, handler, manager);
        }

//        cabinets.items.forEach(((woodType, item) -> {
//
//
//        }));
    }

    public void createCuttingRecipe(String recipeType, Block input, Block output,
                                    WoodType woodType, ServerDynamicResourcesHandler handler, ResourceManager manager) {

        if (Objects.nonNull(input) && Objects.nonNull(output)) {
            ResourceLocation recipeLocation = ResType.RECIPES.getPath(modRes("cutting/oak_"+recipeType));

            try (InputStream recipeStream = manager.getResource(recipeLocation)
                    .orElseThrow(() -> new FileNotFoundException(recipeLocation.toString())).open()) {
                JsonObject recipe = RPUtils.deserializeJson(recipeStream);

                // EDITING RECIPE
                JsonObject underIngredients = recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject();
                underIngredients.addProperty("item", Utils.getID(input).toString());

                JsonObject underResult = recipe.getAsJsonArray("result").get(0).getAsJsonObject().getAsJsonObject("item");
                underResult.addProperty("id", Utils.getID(output).toString());

                // Adding to ResourceLocation
                String path = this.shortenedId() + "/cutting/" + woodType.getAppendableId() +"_"+ recipeType;

                handler.dynamicPack.addJson(EveryCompat.res(path), recipe, ResType.RECIPES);
            } catch (IOException e) {
                handler.getLogger().error("Failed to generate the cutting recipe for {} - {}", Utils.getID(output), e);
            }
        }
    }

}
