package arcania.modid.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SpellItem extends Item {
    public SpellItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) { // Only execute on the server

            // Fireball direction
            double x = user.getRotationVec(1.0F).x;
            double y = user.getRotationVec(1.0F).y;
            double z = user.getRotationVec(1.0F).z;

            // Create Fireball entity
            FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, world);
            fireball.refreshPositionAndAngles(user.getX(), user.getEyeY(), user.getZ(), user.getYaw(), user.getPitch());
            fireball.setVelocity(x * 1.5, y * 1.5, z * 1.5); // Adjust speed as needed
            fireball.setOwner(user); // Sets the player as the owner

            world.spawnEntity(fireball);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
