package net.mehvahdjukaar.every_compat.modules.decorative_blocks;


import com.mojang.datafixers.util.Pair;
import lilypuree.decorative_blocks.blocks.types.VanillaWoodTypes;
import lilypuree.decorative_blocks.blocks.types.WoodDecorativeBlockTypes;
import lilypuree.decorative_blocks.core.DBBlocks;
import lilypuree.decorative_blocks.items.SeatItem;
import lilypuree.decorative_blocks.items.SupportItem;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.core.misc.McMetaFile;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecorativeBlocksModule extends SimpleModule {

    public final Map<WoodType, DBWoodType> wtConversion = new HashMap<>();
    public final SimpleEntrySet<WoodType, Block> beams;
    public final SimpleEntrySet<WoodType, Block> palisades;
    public final SimpleEntrySet<WoodType, Block> supports;
    public final SimpleEntrySet<WoodType, Block> seats;

    public DecorativeBlocksModule(String modId) {
        super(modId, "db");

        ResourceLocation tab = modRes(modId);

        beams = SimpleEntrySet.builder(WoodType.class, "beam",
                        () -> DBBlocks.BEAMS.get(VanillaWoodTypes.OAK).get(), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> DBBlocks.createDecorativeBlock(wtConversion.get(w), WoodDecorativeBlockTypes.BEAM))
                .addTag(modRes("beams"), Registries.BLOCK)
                .addTag(modRes("beams"), Registries.ITEM)
                .addTag(modRes("beams_that_burn"), Registries.ITEM)
                .addTag(modRes("beams_that_burn"), Registries.BLOCK)
                .addTag(modRes("logs_that_burn"), Registries.BLOCK)
                .addTag(modRes("logs_that_burn"), Registries.ITEM)
                .defaultRecipe()
                .setTabKey(tab)
                .setPalette(this::makeDBPalette)
                .addTexture(modRes("block/oak_beam_end"))
                .addTexture(modRes("block/oak_beam_side"))
                .build();

        this.addEntry(beams);


        palisades = SimpleEntrySet.builder(WoodType.class, "palisade",
                        () -> DBBlocks.PALISADES.get(VanillaWoodTypes.OAK).get(), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> DBBlocks.createDecorativeBlock(wtConversion.get(w), WoodDecorativeBlockTypes.PALISADE))
                .addTag(modRes("palisades"), Registries.BLOCK)
                .addTag(modRes("palisades"), Registries.ITEM)
                .defaultRecipe()
                .setTabKey(tab)
                .setPalette(this::makeDBPalette)
                .addTexture(modRes("block/oak_palisade_end"))
                .addTexture(modRes("block/oak_palisade_side"))
                .build();

        this.addEntry(palisades);


        supports = SimpleEntrySet.builder(WoodType.class, "support",
                        () -> DBBlocks.SUPPORTS.get(VanillaWoodTypes.OAK).get(), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> DBBlocks.createDecorativeBlock(wtConversion.get(w), WoodDecorativeBlockTypes.SUPPORT))
                .addTag(modRes("supports"), Registries.BLOCK)
                .addTag(modRes("supports"), Registries.ITEM)
                .addCustomItem((w, b, p) -> new SupportItem(b, p))
                .defaultRecipe()
                .setTabKey(tab)
                .setPalette(this::makeDBPalette)
                .addTexture(modRes("block/oak_support_end"))
                .addTexture(modRes("block/oak_support_side"))
                .build();


        this.addEntry(supports);


        seats = SimpleEntrySet.builder(WoodType.class, "seat",
                        () -> DBBlocks.SEATS.get(VanillaWoodTypes.OAK).get(), () -> WoodTypeRegistry.OAK_TYPE,
                        w -> DBBlocks.createDecorativeBlock(wtConversion.get(w), WoodDecorativeBlockTypes.SEAT))
                .addTag(modRes("seats"), Registries.BLOCK)
                .addTag(modRes("seats"), Registries.ITEM)
                .defaultRecipe()
                .addCustomItem((w, b, p) -> new SeatItem(b, p))
                .setTabKey(tab)
                .setPalette(this::makeDBPalette)
                .addTexture(modRes("block/oak_seat"))
                .build();

        this.addEntry(seats);

    }

    public Pair<List<Palette>, McMetaFile> makeDBPalette(WoodType woodType, ResourceManager manager) {
        try (TextureImage plankTexture = TextureImage.open(manager,
                RPUtils.findFirstBlockTextureLocation(manager, woodType.planks))) {

            List<Palette> targetPalette = SpriteUtils.extrapolateSignBlockPalette(plankTexture);
            return Pair.of(targetPalette, plankTexture.getMcMeta());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T extends BlockType> void registerBlocks(Class<T> typeClass,
                                                     Registrator<Block> registry, Collection<T> types) {
        if (typeClass == WoodType.class) {
            types.forEach(w -> wtConversion.put((WoodType) w, new DBWoodType((WoodType) w)));
        }
        super.registerBlocks(typeClass, registry, types);
    }

}