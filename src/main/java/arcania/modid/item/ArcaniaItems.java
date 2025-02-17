package arcania.modid.item;

import  arcania.modid.Arcania;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static arcania.modid.Arcania.MOD_ID;


public class ArcaniaItems {

    public static Item RED_ORB = registerItem("red_orb", new Item(new Item.Settings()));
    public static Item BLUE_ORB = registerItem("blue_orb", new Item(new Item.Settings()));
    public static Item GREEN_ORB = registerItem("green_orb", new Item(new Item.Settings()));
    public static Item SPELL = registerItem("spell", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Arcania.MOD_ID, name), item);
    }

    public static void registerModItems(){
        Arcania.LOGGER.info("Registriert Items für "+ MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(RED_ORB);
            fabricItemGroupEntries.add(BLUE_ORB);
            fabricItemGroupEntries.add(GREEN_ORB);
            fabricItemGroupEntries.add(SPELL);
        });
    }
}
