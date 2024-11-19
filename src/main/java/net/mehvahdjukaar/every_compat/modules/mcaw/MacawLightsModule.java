package net.mehvahdjukaar.every_compat.modules.mcaw;

import com.mcwlights.kikoz.MacawsLights;
import com.mcwlights.kikoz.init.BlockInit;
import com.mcwlights.kikoz.objects.LightBaseShort;
import com.mcwlights.kikoz.objects.TikiTorch;
import net.mehvahdjukaar.every_compat.WoodGood;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.ToIntFunction;

//SUPPORT: v1.1.0+
public class MacawLightsModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> soul_tiki_torches;
    public final SimpleEntrySet<WoodType, Block> tiki_torches;
    public final SimpleEntrySet<WoodType, Block> ceiling_fans;

    public MacawLightsModule(String modId) {
        super(modId, "mcl");
        CreativeModeTab tab = MacawsLights.LightsItemGroup;

        soul_tiki_torches = SimpleEntrySet.builder(WoodType.class, "tiki_torch", "soul",
                        BlockInit.SOUL_OAK_TIKI_TORCH, () -> WoodType.OAK_WOOD_TYPE,
                        w -> new TikiTorch(copyProperties(15), ParticleTypes.SOUL_FIRE_FLAME)
                )
                //TEXTURES: logs
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setRenderType(() -> RenderType::cutout)
                .defaultRecipe()
                .setTab(() -> tab)
                .build();
        this.addEntry(soul_tiki_torches);


        tiki_torches = SimpleEntrySet.builder(WoodType.class, "tiki_torch",
                        BlockInit.OAK_TIKI_TORCH, () -> WoodType.OAK_WOOD_TYPE,
                        w -> new TikiTorch(copyProperties(16), ParticleTypes.FLAME)
                )
                //TEXTURES: logs
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setRenderType(() -> RenderType::cutout)
                .defaultRecipe()
                .setTab(() -> tab)
                .build();
        this.addEntry(tiki_torches);

        ceiling_fans = SimpleEntrySet.builder(WoodType.class, "ceiling_fan_light",
                        BlockInit.OAK_CEILING_FAN_LIGHT, () -> WoodType.OAK_WOOD_TYPE,
                        w -> new LightBaseShort(copyProperties(15))
                )
                .addTextureM(modRes("block/oak_ceiling_fan"),
                        WoodGood.res("block/mcaw/lights/oak_ceiling_fan_m"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setRenderType(() -> RenderType::cutout)
                .defaultRecipe()
                .setTab(() -> tab)
                .build();
        this.addEntry(ceiling_fans);

    }

    @SuppressWarnings("SameParameterValue")
    private BlockBehaviour.Properties copyProperties(int num) {
        return BlockBehaviour.Properties.of(Material.WOOD)
                .color(MaterialColor.WOOD)
                .strength(0.2f, 2.5f)
                .lightLevel(blockOffLightValue(num))
                .sound(SoundType.WOOD)
                .noOcclusion();
    }

    private static ToIntFunction<BlockState> blockOffLightValue(int num) {
        return (state) -> (Boolean)state.getValue(BlockStateProperties.LIT) ? num : 0;
    }

}
