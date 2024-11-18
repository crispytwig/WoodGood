package net.mehvahdjukaar.every_compat.modules.beautify_docorate;

import com.github.Pandarix.beautify.Beautify;
import com.github.Pandarix.beautify.common.block.OakBlinds;
import com.github.Pandarix.beautify.common.block.OakPictureFrame;
import com.github.Pandarix.beautify.common.block.OakTrellis;
import com.github.Pandarix.beautify.core.init.BlockInit;
import net.mehvahdjukaar.every_compat.WoodGood;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

//SUPPORT: v1.4.1-FINAL
@SuppressWarnings("deprecation")
public class BeautifyDecorateModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, OakTrellis> tellis;
    public final SimpleEntrySet<WoodType, OakBlinds> blinds;
    public final SimpleEntrySet<WoodType, OakPictureFrame> picture_frames;

    public BeautifyDecorateModule(String modId) {
        super(modId, "bd");
        CreativeModeTab tab = Beautify.BEAUTIFY_TAB;

        tellis = SimpleEntrySet.builder(WoodType.class, "trellis",
                        BlockInit.OAK_TRELLIS, () -> WoodType.OAK_WOOD_TYPE,
                        w -> new OakTrellis(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                                .strength(0.3F, 0.3F)
                                .sound(SoundType.BAMBOO).noOcclusion()
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setRenderType(() -> RenderType::cutout)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(tellis);

        blinds = SimpleEntrySet.builder(WoodType.class, "blinds",
                        BlockInit.OAK_BLINDS, () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new OakBlinds(WoodGood.copySafe(w.planks)
                                .noOcclusion().strength(0.4F, 0.4F)
                                .sound(SoundType.WOOD)
                                ),
                                "slab" //REASON: recipes
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(blinds);

        picture_frames = SimpleEntrySet.builder(WoodType.class, "picture_frame",
                        BlockInit.OAK_PICTURE_FRAME, () -> WoodType.OAK_WOOD_TYPE,
                        ifHasChild(w -> new OakPictureFrame(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                                .noOcclusion().strength(0.1F, 0.1F)
                                .sound(SoundType.WOOD).noOcclusion()
                                ), "slab" //REASON: recipes
                        )
                )
                .addModelTransform(m -> m.addModifier((s, resLoc, w) -> s.replace(
                        "\"beautify:blocks/oak_frame_texture\"",
                        "\""+WoodGood.MOD_ID + ":blocks/" + shortenedId() + "/" + w.getAppendableId() + "_frame_texture\""
                )))
                .addTexture(modRes("blocks/oak_frame_texture"))
                .setTab(() -> tab)
                .defaultRecipe()
                .build();
        this.addEntry(picture_frames);

    }

}