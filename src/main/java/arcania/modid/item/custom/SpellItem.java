package arcania.modid.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class SpellItem extends Item {
    public SpellItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) { // Only execute on the server

            // Fireball direction
            double x = user.getRotationVec(1.0F).x;
            double y = user.getRotationVec(1.0F).y;
            double z = user.getRotationVec(1.0F).z;

            // Create Fireball entity
            FireballEntity fireball = new FireballEntity(world, user, new Vec3d(x, y, z), 7);
            fireball.refreshPositionAndAngles(user.getX(), user.getEyeY(), user.getZ(), user.getYaw(), user.getPitch());
            fireball.setVelocity(x * 1.5, y * 1.5, z * 1.5); // Adjust speed as needed
            fireball.setOwner(user); // Sets the player as the owner
            fireball.getWorld().spawnEntity(fireball);


        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
