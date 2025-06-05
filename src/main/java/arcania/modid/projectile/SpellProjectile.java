package arcania.modid.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class SpellProjectile extends PersistentProjectileEntity {
    private final SpellProperties spellProperties;
    protected float damage = 6.0F;
    protected int maxAge = 200; // 10 seconds at 20 TPS
    protected double speed = 1.5D; // Faster than ghast fireball (1.0D)
    protected boolean hasGravity = false;
    protected int spellLevel = 1;

    public SpellProjectile(EntityType<? extends SpellProjectile> entityType, World world) {
        super(entityType, world);
        this.spellProperties = new SpellProperties();
        initializeGravity();
    }

    public SpellProjectile(EntityType<? extends SpellProjectile> entityType, LivingEntity owner, World world) {
        this(entityType, world);
        this.setOwner(owner);
        this.setNoGravity(true);
    }

    public SpellProjectile(EntityType<? extends SpellProjectile> entityType, double x, double y, double z, World world) {
        super(entityType, world);
        this.spellProperties = new SpellProperties();
        this.setPosition(x, y, z);
        initializeGravity();
    }

    private void initializeGravity() {
        this.setNoGravity(!spellProperties.hasGravity());
    }

    @Override
    public void tick() {
        // Age check first
        if (this.age >= maxAge) {
            this.discard();
            return;
        }

        // Check for collisions
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
            return;
        }

        // Update position based on velocity
        Vec3d velocity = this.getVelocity();
        this.setPosition(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);

        // Spawn particles on client side
        if (this.getWorld().isClient) {
            spawnTrailParticles();
        }

        // Apply gravity if enabled
        if (hasGravity) {
            this.setVelocity(velocity.x, velocity.y - 0.03D, velocity.z);
        }

        this.velocityDirty = true;
        this.age++;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) hitResult);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.onBlockHit((BlockHitResult) hitResult);
        }

        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    // Abstract methods for spell-specific behavior
    protected abstract void onEntityHit(EntityHitResult entityHitResult);
    protected abstract void onBlockHit(BlockHitResult blockHitResult);
    protected abstract void spawnTrailParticles();
    protected abstract void spawnImpactParticles(Vec3d pos);
    protected abstract ParticleEffect getTrailParticle();
    protected abstract SoundEvent getHitSound();

    // Utility methods
    protected void dealDamage(Entity target, float damageAmount) {
        if (target instanceof LivingEntity && this.getOwner() instanceof LivingEntity) {
            DamageSource damageSource = this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner());
            target.damage(damageSource, damageAmount * spellLevel);
        }
    }

    public void setSpellProperties(float damage, double speed, int spellLevel) {
        this.damage = damage;
        this.speed = speed;
        this.spellLevel = spellLevel;
    }

    public void shoot(Vec3d direction) {
        Vec3d normalizedDir = direction.normalize().multiply(speed);
        this.setVelocity(normalizedDir);
        this.velocityDirty = true;
    }

    public void shootAt(Entity target, float velocity, float divergence) {
        double dx = target.getX() - this.getX();
        double dy = target.getBodyY(0.5D) - this.getY();
        double dz = target.getZ() - this.getZ();

        Vec3d direction = new Vec3d(dx, dy, dz).normalize();
        this.shoot(direction.multiply(velocity));
    }

    // Required overrides from PersistentProjectileEntity
    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemStack.EMPTY;
    }

    // Getters and setters
    public double getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

    public void setSpellLevel(int level) {
        this.spellLevel = Math.max(1, level);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // NBT serialization
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Damage", this.damage);
        nbt.putDouble("Speed", this.speed);
        nbt.putInt("SpellLevel", this.spellLevel);
        nbt.putInt("MaxAge", this.maxAge);
        nbt.putBoolean("HasGravity", this.hasGravity);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.damage = nbt.getFloat("Damage");
        this.speed = nbt.getDouble("Speed");
        this.spellLevel = nbt.getInt("SpellLevel");
        this.maxAge = nbt.getInt("MaxAge");
        this.hasGravity = nbt.getBoolean("HasGravity");
        this.setNoGravity(!hasGravity);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !entity.equals(this.getOwner());
    }
}