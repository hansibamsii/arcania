package arcania.modid.block;

import arcania.modid.Arcania;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ArcaniaBlocks {

    public static final Block SPELL_TABLE = registerBlock("spell_table", new Block(AbstractBlock.Settings.create().strength(4f)
            .sounds(BlockSoundGroup.WOOD)));

    public static final Block RED_ORB_ORE = registerBlock("red_orb_ore", new ExperienceDroppingBlock(
            UniformIntProvider.create(2, 5),
            AbstractBlock.Settings.create().strength(3f).requiresTool()));

    public static final Block GREEN_ORB_ORE = registerBlock("green_orb_ore", new ExperienceDroppingBlock(
            UniformIntProvider.create(3, 6),
            AbstractBlock.Settings.create().strength(3f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Arcania.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Arcania.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));

    }

    public static void registerModBlocks() {
        Arcania.LOGGER.info("Registriert Blöcke für "+Arcania.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(ArcaniaBlocks.SPELL_TABLE);
            fabricItemGroupEntries.add(ArcaniaBlocks.RED_ORB_ORE);
            fabricItemGroupEntries.add(ArcaniaBlocks.GREEN_ORB_ORE);

        });
    }
}
