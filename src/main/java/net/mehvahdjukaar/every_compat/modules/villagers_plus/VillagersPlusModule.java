package net.mehvahdjukaar.every_compat.modules.villagers_plus;

import com.finallion.villagersplus.VillagersPlus;
import com.finallion.villagersplus.blocks.HorticulturistTableBlock;
import com.finallion.villagersplus.init.ModTags;
import net.mehvahdjukaar.every_compat.WoodGood;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.modules.villagers_plus.client.CompatTubBlockEntityRenderer;
import net.mehvahdjukaar.selene.block_set.wood.WoodType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

//SUPPORT: v1.2.1-FINAL
public class VillagersPlusModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> tubs;

    public VillagersPlusModule(String modId) {
        super(modId, "vp");

        tubs = SimpleEntrySet.builder(WoodType.class, "horticulturist_table",
                        () -> getModBlock("oak_horticulturist_table"), () -> WoodType.OAK_WOOD_TYPE,
                       ifHasChild(
                               w -> new CompatTubBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5F).noOcclusion()),
                               "stripped_log" //REASON: recipes & textures
                       )
                )
                .addTile(CompatTubBlockEntity::new)
//                .createPaletteFromChild(p -> {}, "stripped_log", SpriteHelper.LOOKS_LIKE_SIDE_LOG_TEXTURE)
                .addTextureM(modRes("block/oak_horticulturist_workstation"),
                        WoodGood.res("block/vp/oak_horticulturist_workstation_m"))
                .setTab(() -> VillagersPlus.GROUP)
                .defaultRecipe()
                .build();
        this.addEntry(tubs);

    }

    @Override
    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    @OnlyIn(Dist.CLIENT)
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer((BlockEntityType<CompatTubBlockEntity>) tubs.getTileHolder().tile, CompatTubBlockEntityRenderer::new);
    }

    //!!====================================================================================================================

    public class CompatTubBlock extends HorticulturistTableBlock {
        public CompatTubBlock(Properties properties) {
            super(properties);
        }

        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new CompatTubBlockEntity(pos, state);
        }

        public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            ItemStack itemStack = player.getItemInHand(hand);
            BlockEntity var9 = world.getBlockEntity(pos);
            if (var9 instanceof CompatTubBlockEntity blockEntity) {
                if (state.getValue(FLOWERS) < 4) {
                    if (itemStack.is(ModTags.TALL_PLANTABLE_BLOCKS) && state.getValue(FLOWERS) == 0) {
                        blockEntity.insertFlower(itemStack, state.getValue(FLOWERS));
                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                        }

                        if (!world.isClientSide()) {
                            world.setBlock(pos, state.setValue(FLOWERS, 4).setValue(IS_TALL_FLOWER, true), 3);
                            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                        }

                        return InteractionResult.sidedSuccess(world.isClientSide());
                    }

                    if (itemStack.is(ModTags.SMALL_PLANTABLE_BLOCKS)) {
                        blockEntity.insertFlower(itemStack, state.getValue(FLOWERS));
                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                        }

                        if (!world.isClientSide()) {
                            world.setBlock(pos, state.setValue(FLOWERS, state.getValue(FLOWERS) + 1).setValue(IS_TALL_FLOWER, false), 3);
                            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                        }

                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
            }
            return InteractionResult.PASS;
        }
    }

    public class CompatTubBlockEntity extends BlockEntity implements Container, WorldlyContainer {
        private NonNullList<ItemStack> inventory;
        private static final int[] SLOTS = new int[]{0, 1, 2, 3};

        public CompatTubBlockEntity(BlockPos pos, BlockState state) {
            super(Objects.requireNonNull(tubs.getTileHolder()).tile, pos, state);
            this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        }

        @Override
        public @NotNull BlockEntityType<?> getType() {
            return Objects.requireNonNull(tubs.getTileHolder()).tile;
        }

        public int getContainerSize() {
            return this.inventory.size();
        }

        public @NotNull CompoundTag getUpdateTag() {
            CompoundTag compoundtag = new CompoundTag();
            ContainerHelper.saveAllItems(compoundtag, this.inventory, true);
            return compoundtag;
        }

        @SuppressWarnings("UnusedReturnValue")
        public boolean insertFlower(ItemStack flower, int slot) {
            ItemStack itemStack = this.inventory.get(slot);
            if (itemStack.isEmpty()) {
                this.inventory.set(slot, flower.split(1));
                this.markUpdated();
                return true;
            } else {
                return false;
            }
        }

        public NonNullList<ItemStack> getItems() {
            return this.inventory;
        }

        public boolean isEmpty() {
            for(ItemStack itemStack : this.inventory) {
                if (!itemStack.isEmpty()) {
                    return false;
                }
            }

            return true;
        }

        private void markUpdated() {
            this.setChanged();
            Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }

        public ClientboundBlockEntityDataPacket getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }

        public void load(@NotNull CompoundTag nbt) {
            super.load(nbt);
            this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(nbt, this.inventory);
        }

        protected void saveAdditional(@NotNull CompoundTag nbt) {
            super.saveAdditional(nbt);
            ContainerHelper.saveAllItems(nbt, this.inventory);
        }

        public @NotNull ItemStack getItem(int slot) {
            return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
        }

        public @NotNull ItemStack removeItem(int slot, int amount) {
            return ContainerHelper.removeItem(this.inventory, slot, amount);
        }

        public @NotNull ItemStack removeItemNoUpdate(int slot) {
            return ContainerHelper.takeItem(this.inventory, slot);
        }

        public void setItem(int slot, @NotNull ItemStack stack) {
            if (slot >= 0 && slot < this.inventory.size()) {
                this.inventory.set(slot, stack);
            }

        }

        @SuppressWarnings("DataFlowIssue")
        public boolean stillValid(@NotNull Player player) {
            if (this.level.getBlockEntity(this.worldPosition) != this) {
                return false;
            } else {
                return !(player.distanceToSqr((double)this.worldPosition.getX() + (double)0.5F, (double)this.worldPosition.getY() + (double)0.5F, (double)this.worldPosition.getZ() + (double)0.5F) > (double)64.0F);
            }
        }

        public void clearContent() {
            this.inventory.clear();
        }

        public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
            return SLOTS;
        }

        public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
            return this.canPlaceItem(slot, stack);
        }

        public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction dir) {
            return false;
        }
    }
}
