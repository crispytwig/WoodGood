package net.mehvahdjukaar.every_compat.modules.red_bits;

import net.darktree.redbits.RedBits;
import net.darktree.redbits.blocks.LargeButtonBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.mixin.itemgroup.ItemGroupAccessor;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.PushReaction;

//SUPPORT v1.16.1+
//NOTE: Could be supported in NEOFORGE via Sinytra Connector
public class RedBitsModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> large_buttons;

    public RedBitsModule(String modId) {
        super(modId, "rb");

        large_buttons = SimpleEntrySet.builder(WoodType.class, "large_button",
                        () -> RedBits.OAK_LARGE_BUTTON, () -> WoodTypeRegistry.OAK_TYPE,
                        wood -> new LargeButtonBlock(true, new BlockSetType(wood.getTypeName()), BlockBehaviour.Properties.of()
                                .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY))
                )
                .requiresChildren("button") // Recipes
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("large_buttons"), Registries.BLOCK)
                .addTag(modRes("large_wooden_buttons"), Registries.BLOCK)
                .setTabKey(CreativeModeTabs.REDSTONE_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(large_buttons);


    }
}