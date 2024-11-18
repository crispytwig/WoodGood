package net.mehvahdjukaar.every_compat.modules.oreberries_replanted;

import com.mrbysco.oreberriesreplanted.block.OreBerryBushBlock;
import com.mrbysco.oreberriesreplanted.block.VatBlock;
import com.mrbysco.oreberriesreplanted.blockentity.VatBlockEntity;
import com.mrbysco.oreberriesreplanted.client.ber.VatBER;
import com.mrbysco.oreberriesreplanted.registry.OreBerryRegistry;
import com.mrbysco.oreberriesreplanted.registry.OreBerryTab;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.IForgeRegistry;

//SUPPORT: v0.1.10-FINAL
public class OreberriesReplantedModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> vats;

    public OreberriesReplantedModule(String modId) {
        super(modId, "or");

        vats = SimpleEntrySet.builder(WoodType.class, "vat",
                        OreBerryRegistry.OAK_VAT, () -> WoodType.OAK_WOOD_TYPE,
                        w -> new CompatVatBlock(BlockBehaviour.Properties.of(Material.LEAVES)
                                .sound(SoundType.SWEET_BERRY_BUSH)
                                .noOcclusion()
                                .isSuffocating(OreBerryBushBlock::isntSolid)
                                .isViewBlocking(OreBerryBushBlock::isntSolid))
                )
                //TEXTURES: ?
                .addTile(VatBlockEntity::new)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> OreBerryTab.TAB)
                .defaultRecipe()
                .build();
        this.addEntry(vats);

    }

    @Override
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        super.registerEntityRenderers(event);
        event.registerBlockEntityRenderer((BlockEntityType<VatBlockEntity>) vats.getTileHolder().tile, VatBER::new);
    }

    @Override
    public void registerTiles(IForgeRegistry<BlockEntityType<?>> registry) {
        super.registerTiles(registry);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "DataFlowIssue"})
    public class CompatVatBlock extends VatBlock {
        public CompatVatBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new VatBlockEntity(vats.getTileHolder().tile, pos, state);
        }

        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
            return createVatTicker(level, entityType, (BlockEntityType) vats.getTileHolder().tile);
        }
    }

}