package arcania.modid.item;

import arcania.modid.Arcania;
import arcania.modid.block.ArcaniaBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArcaniaItemGroup {

    public static final ItemGroup ARCANIA_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Arcania.MOD_ID, "arcania_group"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ArcaniaItems.GREEN_ORB))
                    .displayName(Text.translatable("itemgroup.arcania.arcania_group"))
                    .entries((displayContext, entries) -> {
                        entries.add(ArcaniaItems.RED_ORB);
                        entries.add(ArcaniaItems.BLUE_ORB);
                        entries.add(ArcaniaItems.GREEN_ORB);
                        entries.add(ArcaniaBlocks.SPELL_TABLE);
                        entries.add(ArcaniaBlocks.RED_ORB_ORE);
                        entries.add(ArcaniaBlocks.GREEN_ORB_ORE);
                        entries.add(ArcaniaItems.SPELL);
                    }).build());


    public static void registerItemGroup() {
        Arcania.LOGGER.info("Registriere Itemgruppe für "+Arcania.MOD_ID);
    }
}
