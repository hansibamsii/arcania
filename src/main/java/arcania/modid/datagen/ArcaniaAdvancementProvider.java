package arcania.modid.datagen;

import arcania.modid.Arcania;
import arcania.modid.item.ArcaniaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ArcaniaAdvancementProvider extends FabricAdvancementProvider {

    public ArcaniaAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry fireElement;
        AdvancementEntry earthElement;
        AdvancementEntry iceElement;
        fireElement = Advancement.Builder.create()
                .display(
                        ArcaniaItems.RED_ORB,
                        Text.literal("Arcane Beginnings"),
                        Text.literal("Ooh sparks!!!"),
                        Identifier.ofVanilla("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                    .criterion("got_red_orb", InventoryChangedCriterion.Conditions.items(ArcaniaItems.RED_ORB))
                    .build(consumer, "arcania:fire_element");
        earthElement = Advancement.Builder.create()
                .parent(fireElement)
                .display(
                        ArcaniaItems.GREEN_ORB,
                        Text.literal("Elemental Novice"),
                        Text.literal("Me personally, I dig it"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("got_green", InventoryChangedCriterion.Conditions.items(ArcaniaItems.GREEN_ORB))
                .build(consumer, "arcania:earth_element");
        iceElement = Advancement.Builder.create()
                .parent(earthElement)
                .display(
                        ArcaniaItems.BLUE_ORB,
                        Text.literal("Elemental Master"),
                        Text.literal("Shit that's cold"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("got_blue", InventoryChangedCriterion.Conditions.items(ArcaniaItems.BLUE_ORB))
                .build(consumer, "arcania:ice_element");


    };

    }

