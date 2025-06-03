package arcania.modid.item.custom;

import arcania.modid.projectile.FireBallEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellItem extends Item {
    public SpellItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL,
                0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        user.getItemCooldownManager().set(this, 20);

        if (!world.isClient) {
            // Create and shoot fireball
            FireBallEntity fireball = new FireBallEntity(world, user);

            // Get the direction the player is looking
            Vec3d lookDirection = user.getRotationVec(1.0F);

            // Set fireball properties
            fireball.setSpellLevel(1); // You can make this configurable later

            // Shoot the fireball
            fireball.shoot(lookDirection);

            // Add the fireball to the world
            world.spawnEntity(fireball);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}