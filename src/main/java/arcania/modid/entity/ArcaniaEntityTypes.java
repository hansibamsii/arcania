package arcania.modid.entity;

import arcania.modid.Arcania;
import arcania.modid.projectile.FireBallEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ArcaniaEntityTypes {
    public static final EntityType<FireBallEntity> FIREBALL = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Arcania.MOD_ID, "fireball"),
            FabricEntityTypeBuilder.<FireBallEntity>create(SpawnGroup.MISC, FireBallEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void registerEntityTypes() {
        Arcania.LOGGER.info("Registering Entity Types for " + Arcania.MOD_ID);
    }
}
