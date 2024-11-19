package net.mehvahdjukaar.every_compat.modules.villagers_plus.client;

import com.finallion.villagersplus.blockentities.HorticulturistTableBlockEntity;
import com.finallion.villagersplus.blocks.HorticulturistTableBlock;
import com.finallion.villagersplus.init.ModTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.every_compat.modules.villagers_plus.VillagersPlusModule;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CompatTubBlockEntityRenderer implements BlockEntityRenderer<VillagersPlusModule.CompatTubBlockEntity> {
    private final BlockRenderDispatcher manager;

    public CompatTubBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.manager = ctx.getBlockRenderDispatcher();
    }

    public void render(VillagersPlusModule.CompatTubBlockEntity blockEntity, float f, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource vertexConsumerProvider, int i, int j) {
        BlockState blockState = blockEntity.getBlockState();
        BlockPos pos = blockEntity.getBlockPos();
        NonNullList<ItemStack> defaultedList = blockEntity.getItems();
        if (blockState.getBlock() instanceof VillagersPlusModule.CompatTubBlock) {
            matrixStack.pushPose();
            if (blockState.getValue(VillagersPlusModule.CompatTubBlock.IS_TALL_FLOWER)) {
                Block flower = Block.byItem(defaultedList.get(0).getItem());
                if (flower instanceof DoublePlantBlock) {
                    Vec3 offset = flower.defaultBlockState().getOffset(Objects.requireNonNull(blockEntity.getLevel()), pos);
                    matrixStack.translate(-offset.x, -offset.y + 0.95, -offset.z);
                    this.renderTallFlower(flower.defaultBlockState().getBlock(), blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, true, j);
                    matrixStack.translate(-offset.x, -offset.y + (double)1.0F, -offset.z);
                    this.renderTallFlower(flower.defaultBlockState().getBlock(), blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, false, j);
                } else {
                    Block flowerOne = Block.byItem(defaultedList.get(0).getItem());
                    if (flowerOne.defaultBlockState().is(Blocks.CACTUS)) {
                        matrixStack.scale(0.75F, 0.75F, 0.75F);
                        matrixStack.translate(0.15, 0.15, 0.15);
                    }

                    Vec3 offset = flowerOne.defaultBlockState().getOffset(Objects.requireNonNull(blockEntity.getLevel()), pos);
                    matrixStack.translate(-offset.x, -offset.y + 0.95, -offset.z);
                    this.renderFlower(Block.byItem(defaultedList.get(0).getItem()), blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                }
            } else {
                switch (blockState.getValue(VillagersPlusModule.CompatTubBlock.FLOWERS)) {
                    case 1:
                        Block flowerOne = Block.byItem(defaultedList.get(0).getItem());
                        Vec3 offset = flowerOne.defaultBlockState().getOffset(Objects.requireNonNull(blockEntity.getLevel()), pos);
                        matrixStack.translate(-offset.x, -offset.y + 0.95, -offset.z);
                        this.renderFlower(flowerOne, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        break;
                    case 2:
                        flowerOne = Block.byItem(defaultedList.get(0).getItem());
                        Block flowerTwo = Block.byItem(defaultedList.get(1).getItem());
                        offset = flowerOne.defaultBlockState().getOffset(Objects.requireNonNull(blockEntity.getLevel()), pos);
                        Vec3 offset2 = flowerTwo.defaultBlockState().getOffset(blockEntity.getLevel(), pos);
                        matrixStack.translate(-offset.x + 0.15, -offset.y + 0.95, -offset.z + 0.15);
                        if (flowerOne.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.75F, 0.75F, 0.75F);
                            matrixStack.translate(0.0F, 0.15, 0.0F);
                        }

                        this.renderFlower(flowerOne, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        matrixStack.translate(-offset2.x - 0.3, -offset2.y, -offset2.z - 0.3 - 0.05);
                        if (flowerTwo.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.75F, 0.75F, 0.75F);
                            matrixStack.translate(0.0F, 0.15, 0.0F);
                        }

                        this.renderFlower(flowerTwo, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        break;
                    case 3:
                        flowerOne = Block.byItem(defaultedList.get(0).getItem());
                        flowerTwo = Block.byItem(defaultedList.get(1).getItem());
                        Block flowerThree = Block.byItem(defaultedList.get(2).getItem());
                        offset = flowerOne.defaultBlockState().getOffset(Objects.requireNonNull(blockEntity.getLevel()), pos);
                        offset2 = flowerTwo.defaultBlockState().getOffset(blockEntity.getLevel(), pos);
                        Vec3 offset3 = flowerThree.defaultBlockState().getOffset(blockEntity.getLevel(), pos);
                        matrixStack.translate(-offset.x + 0.15, -offset.y + 0.95, -offset.z);
                        if (flowerOne.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.6F, 0.6F, 0.6F);
                            matrixStack.translate(0.0F, 0.2, 0.0F);
                        }

                        this.renderFlower(flowerOne, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        matrixStack.translate(-offset2.x - 0.3, -offset2.y - 0.05, -offset2.z - 0.15);
                        if (flowerTwo.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.6F, 0.6F, 0.6F);
                            matrixStack.translate(0.0F, 0.2, 0.0F);
                        }

                        this.renderFlower(flowerTwo, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        matrixStack.translate(-offset3.x - 0.05, -offset3.y + (double)0.0F, -offset3.z + 0.3);
                        if (flowerThree.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.6F, 0.6F, 0.6F);
                            matrixStack.translate(0.0F, 0.2, 0.0F);
                        }

                        this.renderFlower(flowerThree, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        break;
                    case 4:
                        flowerOne = Block.byItem(defaultedList.get(0).getItem());
                        flowerTwo = Block.byItem(defaultedList.get(1).getItem());
                        flowerThree = Block.byItem(defaultedList.get(2).getItem());
                        Block flowerFour = Block.byItem(defaultedList.get(3).getItem());
                        offset = flowerOne.defaultBlockState().getOffset(Objects.requireNonNull(blockEntity.getLevel()), pos);
                        offset2 = flowerTwo.defaultBlockState().getOffset(blockEntity.getLevel(), pos);
                        offset3 = flowerThree.defaultBlockState().getOffset(blockEntity.getLevel(), pos);
                        Vec3 offset4 = flowerFour.defaultBlockState().getOffset(blockEntity.getLevel(), pos);
                        matrixStack.translate(-offset.x + 0.15, -offset.y + 0.95, -offset.z + 0.15);
                        if (flowerOne.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.5F, 0.5F, 0.5F);
                            matrixStack.translate(0.0F, 0.25F, 0.0F);
                        }

                        this.renderFlower(flowerOne, blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        matrixStack.translate(-offset2.x, -offset2.y - 0.05, -offset2.z - 0.3 - 0.05);
                        if (flowerTwo.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.5F, 0.5F, 0.5F);
                            matrixStack.translate(0.0F, 0.25F, 0.0F);
                        }

                        this.renderFlower(Block.byItem(defaultedList.get(1).getItem()), blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        matrixStack.translate(-offset3.x - 0.3, -offset3.y + (double)0.0F, -offset3.z);
                        if (flowerThree.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.5F, 0.5F, 0.5F);
                            matrixStack.translate(0.0F, 0.25F, 0.0F);
                        }

                        this.renderFlower(Block.byItem(defaultedList.get(2).getItem()), blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                        matrixStack.translate(-offset4.x - 0.05, -offset4.y - 0.05, -offset4.z + 0.37);
                        if (flowerFour.defaultBlockState().is(ModTags.SCALABLE_BLOCKS)) {
                            matrixStack.scale(0.5F, 0.5F, 0.5F);
                            matrixStack.translate(0.0F, 0.25F, 0.0F);
                        }

                        this.renderFlower(Block.byItem(defaultedList.get(3).getItem()), blockEntity.getLevel(), pos, matrixStack, vertexConsumerProvider, j);
                }
            }

            matrixStack.popPose();
        }

    }

    @SuppressWarnings("deprecation")
    private void renderFlower(Block flower, Level world, BlockPos pos, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int overlay) {
        this.manager.getModelRenderer().tesselateBlock(world, this.manager.getBlockModel(flower.defaultBlockState()), flower.defaultBlockState(), pos, matrixStack, vertexConsumerProvider.getBuffer(RenderType.cutoutMipped()), false, new Random(), flower.defaultBlockState().getSeed(pos), overlay);
    }

    private void renderTallFlower(Block flower, Level world, BlockPos pos, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, boolean lower, int overlay) {
        if (lower) {
            this.manager.getModelRenderer().tesselateBlock(world, this.manager.getBlockModel(flower.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)), flower.defaultBlockState(), pos, matrixStack, vertexConsumerProvider.getBuffer(RenderType.cutoutMipped()), false, new Random(), flower.defaultBlockState().getSeed(pos), overlay, EmptyModelData.INSTANCE);
        } else {
            this.manager.getModelRenderer().tesselateBlock(world, this.manager.getBlockModel(flower.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)), flower.defaultBlockState(), pos, matrixStack, vertexConsumerProvider.getBuffer(RenderType.cutoutMipped()), false, new Random(), flower.defaultBlockState().getSeed(pos), overlay, EmptyModelData.INSTANCE);
        }

    }

}
