package net.mehvahdjukaar.every_compat.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.configs.ModEntriesConfigs;
import net.mehvahdjukaar.moonlight.api.resources.BlockTypeResTransformer;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicResourcePack;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.function.Consumer;

public class ResourcesUtils {

    public static <B extends Block, T extends BlockType> void addStandardResources(
            String modId, ResourceManager manager, DynClientResourcesGenerator pack, Map<T, B> blocks, T baseType) {
        addStandardResources(modId, manager, pack, blocks, baseType, null);
    }

    public static <B extends Block, T extends BlockType> void addStandardResources(
            String modId, ResourceManager manager, DynClientResourcesGenerator d,
            Map<T, B> blocks, T baseType, @Nullable Consumer<BlockTypeResTransformer<T>> extraTransform) {

        if (blocks.isEmpty()) return;

        //finds one entry. used so we can grab the oak equivalent
        var first = blocks.entrySet().stream().findFirst().get();
        Block oakBlock = BlockType.changeBlockType(first.getValue(), first.getKey(), baseType);

        String baseBlockName = baseType.getTypeName();

        if (oakBlock == null) {
            EveryCompat.LOGGER.error("Failed to generate some assets");
            return;
        }
        ResourceLocation oldBlockId = Utils.getID(oakBlock);

        BlockTypeResTransformer<T> modifier = BlockTypeResTransformer.create(modId, manager);
        modifier.IDReplaceType(baseBlockName).replaceBlockType(baseBlockName);
        if (extraTransform != null) extraTransform.accept(modifier); //idk about this

        BlockTypeResTransformer<T> modelModifier = standardModelTransformer(modId, manager, baseType, baseBlockName, extraTransform);

        Set<String> modelsLoc = new HashSet<>();

        Item oakItem = oakBlock.asItem();


        //!! models/item
        if (oakItem != Items.AIR) {
            try {
                //we cant use this since it might override partent too. Custom textured items need a custom model added manually with addBlockResources
                // modelModifier.replaceItemType(baseBlockName);

                BlockTypeResTransformer<T> itemModifier = standardModelTransformer(modId, manager, baseType, baseBlockName, extraTransform);

                StaticResource oldItemModel = StaticResource.getOrFail(manager,
                        ResType.ITEM_MODELS.getPath(Utils.getID(oakItem)));

                JsonObject itemModelContent = RPUtils.deserializeJson(new ByteArrayInputStream(oldItemModel.data));
                //adds models referenced from here. not recursive
                modelsLoc.addAll(RPUtils.findAllResourcesInJsonRecursive(itemModelContent, s -> s.equals("model") || s.equals("parent")));

                if (itemModelContent.has("parent")) {
                    String parent = itemModelContent.get("parent").getAsString();
                    if (parent.contains("item/generated")) {
                        itemModifier.replaceItemType(baseBlockName);
                    }
                }

                blocks.forEach((blockType, block) -> {
                    ResourceLocation blockId = Utils.getID(block);
                    try {
                        StaticResource newRes = itemModifier.transform(oldItemModel, blockId, blockType);
                        assert newRes.location != oldItemModel.location : "ids cant be the same";
                        d.addResourceIfNotPresent(manager, newRes);
                    } catch (Exception e) {
                        EveryCompat.LOGGER.error("Failed to add {} item model json file:", block, e);
                    }
                });
            } catch (Exception e) {
                EveryCompat.LOGGER.error("Could not find item model for {}", oakBlock);
            }
        } else {
            EveryCompat.LOGGER.warn("Found block with no item {}, this could be a bug", oakBlock);
        }

        //!! blockstate
        try {
            StaticResource oldBlockstate = StaticResource.getOrFail(manager, ResType.BLOCKSTATES.getPath(oldBlockId));
            // Blockstate's content
            JsonElement blockstateContent = RPUtils.deserializeJson(new ByteArrayInputStream(oldBlockstate.data));

            modelsLoc.addAll(RPUtils.findAllResourcesInJsonRecursive(blockstateContent, s -> s.equals("model")));
            List<StaticResource> baseTypeModels = new ArrayList<>();

            for (var m : modelsLoc) {
                //remove the ones from mc namespace
                ResourceLocation modelRes = ResourceLocation.parse(m);
                if (!modelRes.getNamespace().equals("minecraft")) {
                    StaticResource model = StaticResource.getOrLog(manager, ResType.MODELS.getPath(m));
                    if (model != null) baseTypeModels.add(model);
                }
            }

            blocks.forEach((blockType, block) -> {
                ResourceLocation blockId = Utils.getID(block);
                try {
                    //noinspection PointlessBooleanExpression
                    if (true || ModEntriesConfigs.isEntryEnabled(blockType, block)) { //generating all the times otherwise we get log spam
                        //creates blockstate
                        StaticResource newBlockState = modifier.transform(oldBlockstate, blockId, blockType);
                        assert newBlockState.location != oldBlockstate.location : "ids cant be the same";
                        d.addResourceIfNotPresent(manager, newBlockState);

                        //creates models/block files
                        for (StaticResource model : baseTypeModels) {
                            try {
                                StaticResource newModel = modelModifier.transform(model, blockId, blockType);
                                assert newModel.location != model.location : "ids cant be the same";
                                d.addResourceIfNotPresent(manager, newModel);
                            } catch (Exception exception) {
                                EveryCompat.LOGGER.error("Failed to add {} block model file:", block, exception);
                            }
                        }
                    } else {
                        //dummy blockstate so we don't generate models for this
                        d.getPack().addJson(blockId, DUMMY_BLOCKSTATE, ResType.BLOCKSTATES);
                    }

                } catch (Exception e) {
                    EveryCompat.LOGGER.error("Failed to add {} blockstate file:", blockId, e);
                }
            });
        } catch (Exception e) {
            EveryCompat.LOGGER.error("Could not find blockstate definition for {}", oakBlock);
        }

    }


    //same as above just with just item models. a bunch of copy paste here... ugly
    public static <I extends Item, T extends BlockType> void addItemModels(
            String modId, ResourceManager manager, DynClientResourcesGenerator d,
            Map<T, I> items, T baseType, @Nullable Consumer<BlockTypeResTransformer<T>> extraTransform) {

        if (items.isEmpty()) return;

        //finds one entry. used so we can grab the oak equivalent
        var first = items.entrySet().stream().findFirst().get();
        Item baseTypeItem = BlockType.changeItemType(first.getValue(), first.getKey(), baseType);

        String baseItemname = baseType.getTypeName();

        if (baseTypeItem == null) {
            EveryCompat.LOGGER.error("Failed to modify models/item");
            return;
        }
        BlockTypeResTransformer<T> modifier = BlockTypeResTransformer.create(modId, manager);
        modifier.IDReplaceType(baseItemname).replaceBlockType(baseItemname);
        if (extraTransform != null) extraTransform.accept(modifier); //idk about this

        BlockTypeResTransformer<T> modelModifier = standardModelTransformer(modId, manager, baseType, baseItemname, extraTransform);

        Set<String> modelsLoc = new HashSet<>();

        //!! models/item
        try {
            //we cant use this since it might override partent too. Custom textured items need a custom model added manually with addBlockResources
            // modelModifier.replaceItemType(baseItemname);

            BlockTypeResTransformer<T> itemModifier = standardModelTransformer(modId, manager, baseType, baseItemname, extraTransform);

            StaticResource oldItemModel = StaticResource.getOrFail(manager,
                    ResType.ITEM_MODELS.getPath(Utils.getID(baseTypeItem)));

            JsonObject json = RPUtils.deserializeJson(new ByteArrayInputStream(oldItemModel.data));
            //adds models referenced from here. not recursive
            modelsLoc.addAll(RPUtils.findAllResourcesInJsonRecursive(json, s -> s.equals("model") || s.equals("parent")));

            if (json.has("parent")) {
                String parent = json.get("parent").getAsString();
                if (parent.contains("item/generated")) {
                    itemModifier.replaceItemType(baseItemname);
                }
            }

            items.forEach((w, b) -> {
                ResourceLocation id = Utils.getID(b);
                try {
                    StaticResource newRes = itemModifier.transform(oldItemModel, id, w);
                    assert newRes.location != oldItemModel.location : "ids cant be the same";
                    d.addResourceIfNotPresent(manager, newRes);
                } catch (Exception e) {
                    EveryCompat.LOGGER.error("Failed to add {} item model file:", b, e);
                }
            });
        } catch (Exception e) {
            EveryCompat.LOGGER.error("Could not find item model of {}", baseTypeItem);
        }


        //blockstate
        //models
        List<StaticResource> oldModels = new ArrayList<>();

        for (var m : modelsLoc) {
            ResourceLocation modelRes = ResourceLocation.parse(m);
            //remove the ones from mc namespace
            if (!modelRes.getNamespace().equals("minecraft")) {
                StaticResource model = StaticResource.getOrLog(manager, ResType.MODELS.getPath(m));
                if (model != null) oldModels.add(model);
            }
        }

        items.forEach((blockType, item) -> {
            ResourceLocation blockId = Utils.getID(item);

            //noinspection PointlessBooleanExpression
            if (true || ModEntriesConfigs.isEntryEnabled(blockType, item)) { //generating all the times otherwise we get log spam

                //creates item model
                for (StaticResource model : oldModels) {
                    try {
                        StaticResource newModel = modelModifier.transform(model, blockId, blockType);
                        assert newModel.location != model.location : "ids cant be the same";
                        d.addResourceIfNotPresent(manager, newModel);
                    } catch (Exception exception) {
                        EveryCompat.LOGGER.error("Failed to add {} model json file:", item, exception);
                    }
                }
            }
        });
    }

    @NotNull
    public static <T extends BlockType> BlockTypeResTransformer<T> standardModelTransformer(
            String modId, ResourceManager manager, T baseType, String oldTypeName, @Nullable Consumer<BlockTypeResTransformer<T>> extraTransform) {
        BlockTypeResTransformer<T> modelModifier = BlockTypeResTransformer.create(modId, manager);
        if (extraTransform != null) extraTransform.accept(modelModifier);
        modelModifier.IDReplaceType(oldTypeName);
        if (baseType instanceof LeavesType leavesType) {
            SpriteHelper.replaceLeavesTextures(modelModifier, leavesType);
            var woodT = leavesType.getWoodType();
            if (woodT != null) {
                SpriteHelper.replaceWoodTextures(modelModifier, woodT);
            }
        } else if (baseType instanceof WoodType woodType) {
            SpriteHelper.replaceWoodTextures(modelModifier, woodType);
        }

        modelModifier.replaceGenericType(oldTypeName, "block");
        //modelModifier.replaceBlockType(oldTypeName);
        return modelModifier;
    }


    //creates and add new jsons based off the ones at the given resources with the provided modifiers
    public static <B extends Block, T extends BlockType> void addBlockResources(String modId, ResourceManager manager, DynamicResourcePack pack,
                                                                                Map<T, B> blocks, String typeName, ResourceLocation... jsonsLocations) {
        addBlockResources(modId, manager, pack, blocks,
                BlockTypeResTransformer.<T>create(modId, manager)
                        .replaceSimpleType(typeName)
                        .IDReplaceType(typeName),
                jsonsLocations);
    }


    public static <B extends Block, T extends BlockType> void addBlockResources(String modId, ResourceManager manager, DynamicResourcePack pack,
                                                                                Map<T, B> blocks,
                                                                                BlockTypeResTransformer<T> modifier, ResourceLocation... jsonsLocations) {
        List<StaticResource> original = Arrays.stream(jsonsLocations).map(s -> StaticResource.getOrLog(manager, s)).toList();

        blocks.forEach((wood, value) -> {
            if (ModEntriesConfigs.isEntryEnabled(wood, value)) {
                for (var res : original) {

                    try {
                        StaticResource newRes = modifier.transform(res, Utils.getID(value), wood);

                        assert newRes.location != res.location : "ids cant be the same";

                        pack.addResource(newRes);
                    } catch (Exception e) {
                        if (res != null) {
                            EveryCompat.LOGGER.error("Failed to generate json resource from {}", res.location);
                        }
                    }
                }
            }
        });
    }

    //creates and add new recipes based off the one at the given resource

    /**
     * Adds recipes based off an oak leaves based one
     */
    public static void addLeavesRecipes(String modId, ResourceManager manager, DynamicDataPack pack,
                                        Map<LeavesType, Item> blocks, String oakRecipe) {
        addBlocksRecipes(modId, manager, pack, blocks, oakRecipe, LeavesTypeRegistry.OAK_TYPE);
    }

    /**
     * Adds recipes based off an oak planks based one
     */
    public static <B extends Item> void addWoodRecipes(String modId, ResourceManager manager, DynamicDataPack pack,
                                                       Map<WoodType, B> blocks, String oakRecipe) {
        addBlocksRecipes(modId, manager, pack, blocks, oakRecipe, WoodTypeRegistry.OAK_TYPE);
    }

    /**
     * Adds recipes based off a given one
     */
    public static <B extends Item, T extends BlockType> void addBlocksRecipes(String modId, ResourceManager manager, DynamicDataPack pack,
                                                                              Map<T, B> blocks, String oakRecipe, T fromType) {
        addBlocksRecipes(manager, pack, blocks, ResourceLocation.fromNamespaceAndPath(modId, oakRecipe), fromType, 0);
    }

    public static <B extends Item, T extends BlockType> void addBlocksRecipes(ResourceManager manager, DynamicDataPack pack,
                                                                              Map<T, B> items, ResourceLocation oakRecipe, T fromType,
                                                                              int index) {
        Recipe<?> template = RPUtils.readRecipe(manager, oakRecipe);
        items.forEach((w, i) -> {

            if (ModEntriesConfigs.isEntryEnabled(w, i)) {
                try {
                    //check for disabled ones. Will actually crash if its null since vanilla recipe builder expects a non-null one
                    ResourceLocation id = RecipeBuilder.getDefaultRecipeId(i);
                    RecipeHolder<?> newR;
                    if (index != 0) {
                        id = id.withSuffix("_" + index);
                    }
                    newR = RPUtils.makeSimilarRecipe(template, fromType, w, id);
                    //not even needed
                    //newR = ForgeHelper.copyRecipeConditions(template, newR.value());
                    pack.addRecipe(newR);
                } catch (Exception e) {
                    EveryCompat.LOGGER.error("Failed to generate recipe for {}: {}", i, e.getMessage());
                }
            }
        });
    }


    private static final JsonObject DUMMY_BLOCKSTATE;

    static {
        DUMMY_BLOCKSTATE = new JsonObject();
        DUMMY_BLOCKSTATE.addProperty("parent", "block/cube_all");
        JsonObject t = new JsonObject();
        t.addProperty("all", "everycomp:block/disabled");
        DUMMY_BLOCKSTATE.add("textures", t);
    }


    public static <T extends BlockType> Ingredient convertIngredient(Ingredient ingredient, T originalMat, T destinationMat) {
        Ingredient newIng = ingredient;
        for (var in : ingredient.getItems()) {
            Item it = in.getItem();
            if (it != Items.BARRIER) {
                ItemLike i = BlockType.changeItemType(it, originalMat, destinationMat);
                if (i != null) {
                    //converts first ingredient it finds
                    newIng = Ingredient.of(i);
                    break;
                }
            }
        }
        return newIng;
    }
}
