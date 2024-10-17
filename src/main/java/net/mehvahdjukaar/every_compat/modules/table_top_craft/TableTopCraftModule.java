package net.mehvahdjukaar.every_compat.modules.table_top_craft;

import andrews.table_top_craft.TableTopCraft;
import andrews.table_top_craft.objects.blocks.ChessBlock;
import andrews.table_top_craft.tile_entities.ChessTileEntity;
import andrews.table_top_craft.tile_entities.render.ChessTileEntityRenderer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.selene.block_set.BlockType;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

//SUPPORT: FORGE-v2.2.0+
public class TableTopCraftModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> CHESS_BOARDS;

    public TableTopCraftModule(String modId) {
        super(modId, "ttc");

        CHESS_BOARDS = SimpleEntrySet.builder(WoodType.class, "chess",
                        () -> getModBlock("oak_chess"), () -> WoodType.OAK_WOOD_TYPE,
                        w -> new CompatChessBlock(w.getMaterial(), getSound(w)))
                .addTile(CompatChessBlockTile::new)
                .addTag(modRes("chess_boards"), Registry.ITEM_REGISTRY)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registry.BLOCK_REGISTRY)
                .setTab(() -> TableTopCraft.TABLE_TOP_CRAFT_GROUP)
                .defaultRecipe()
                .build();
        this.addEntry(CHESS_BOARDS);
    }

    /*
     * We need the sound type for the constructors, this code
     * retrieves the proper SoundType from the given BlockType.
     * (This method already exists in BlockType, in 1.20.1+)
     */
    @SuppressWarnings("deprecation")
    private SoundType getSound(BlockType type) {
        ItemLike itemLike = type.mainChild();
        if (itemLike instanceof Block block)
            return block.getSoundType(block.defaultBlockState());
        return SoundType.STONE;
    }

    @Override
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        super.registerEntityRenderers(event);
        event.registerBlockEntityRenderer((BlockEntityType<? extends ChessTileEntity>) CHESS_BOARDS.getTileHolder().get(),
                ChessTileEntityRenderer::new);
    }

// CLASSES
    public class CompatChessBlock extends ChessBlock {

        public CompatChessBlock(Material material, SoundType soundType) {
            super(material, soundType);
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new CompatChessBlockTile(pos, state);
        }
    }

    public class CompatChessBlockTile extends ChessTileEntity {

        public CompatChessBlockTile(BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        @SuppressWarnings("DataFlowIssue")
        public @NotNull BlockEntityType<?> getType() {
            return CHESS_BOARDS.getTileHolder().get();
        }
    }
}