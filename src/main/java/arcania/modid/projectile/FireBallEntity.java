package arcania.modid.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireBallEntity extends SpellProjectile {
    private float explosionPower = 4.0F; // Much stronger than ghast (1.5F)
    private boolean destroysBlocks = true;
    private boolean createsFire = true;
    private int burnDuration = 100; // 5 seconds of burning
    
    public FireBallEntity(EntityType<? extends FireBallEntity> entityType, World world) {
        super(entityType, world);
        this.damage = 8.0F; // Higher base damage
        this.speed = 1.5D;
        this.hasGravity = false;
    }
    
    public FireBallEntity(EntityType<? extends FireBallEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
        this.damage = 8.0F;
        this.speed = 1.5D;
        this.hasGravity = false;
    }
    
    public FireBallEntity(EntityType<? extends FireBallEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
        this.damage = 8.0F;
        this.speed = 1.5D;
        this.hasGravity = false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity hitEntity = entityHitResult.getEntity();
        
        if (!this.getWorld().isClient) {
            // Deal direct damage
            this.dealDamage(hitEntity, this.damage);
            
            // Apply burning effect
            if (hitEntity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, burnDuration / 4, 0));
                livingEntity.setOnFireFor(burnDuration / 20); // Convert ticks to seconds
            }
            
            // Create explosion at hit location
            this.createExplosion(entityHitResult.getPos());
        }
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient) {
            Vec3d hitPos = blockHitResult.getPos();
            BlockPos blockPos = blockHitResult.getBlockPos();
            
            // Create explosion
            this.createExplosion(hitPos);
            
            // Set surrounding blocks on fire
            if (createsFire) {
                this.createFireArea(blockPos);
            }
        }
    }
    
    private void createExplosion(Vec3d pos) {
        // Scale explosion power with spell level
        float scaledPower = explosionPower * (1.0F + (spellLevel - 1) * 0.5F);
        
        // Create explosion that can destroy blocks and damage entities
        this.getWorld().createExplosion(
            this, // Entity causing explosion
            pos.x, pos.y, pos.z, // Position
            scaledPower, // Power
            destroysBlocks, // Can destroy blocks
            destroysBlocks ? World.ExplosionSourceType.TNT : World.ExplosionSourceType.NONE // Explosion type
        );
    }
    
    private void createFireArea(BlockPos centerPos) {
        int fireRadius = 2 + spellLevel; // Larger fire area with higher spell level
        
        for (int x = -fireRadius; x <= fireRadius; x++) {
            for (int y = -1; y <= fireRadius; y++) {
                for (int z = -fireRadius; z <= fireRadius; z++) {
                    BlockPos firePos = centerPos.add(x, y, z);
                    
                    // Check if position is within sphere and random chance
                    double distance = Math.sqrt(x*x + y*y + z*z);
                    if (distance <= fireRadius && this.getWorld().getRandom().nextFloat() < 0.7F) {
                        
                        BlockState currentState = this.getWorld().getBlockState(firePos);
                        BlockPos abovePos = firePos.up();
                        BlockState aboveState = this.getWorld().getBlockState(abovePos);
                        
                        // Set fire on top of solid blocks
                        if (!currentState.isAir() && aboveState.isAir()) {
                            this.getWorld().setBlockState(abovePos, Blocks.FIRE.getDefaultState());
                        }
                        
                        // Convert some blocks to lava (very destructive!)
                        if (spellLevel >= 3 && distance <= 1.5 && this.getWorld().getRandom().nextFloat() < 0.3F) {
                            if (currentState.getBlock() != Blocks.BEDROCK && 
                                currentState.getBlock() != Blocks.OBSIDIAN &&
                                !currentState.isAir()) {
                                this.getWorld().setBlockState(firePos, Blocks.LAVA.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void spawnTrailParticles() {
        World world = this.getWorld();
        
        // Main flame trail
        for (int i = 0; i < 3; i++) {
            world.addParticle(ParticleTypes.FLAME,
                this.getX() + (world.getRandom().nextGaussian() * 0.2D),
                this.getY() + (world.getRandom().nextGaussian() * 0.2D),
                this.getZ() + (world.getRandom().nextGaussian() * 0.2D),
                0.0D, 0.0D, 0.0D);
        }
        
        // Smoke trail
        world.addParticle(ParticleTypes.LARGE_SMOKE,
            this.getX(), this.getY(), this.getZ(),
            0.0D, 0.0D, 0.0D);
        
        // Lava particles for higher spell levels
        if (spellLevel >= 2) {
            world.addParticle(ParticleTypes.LAVA,
                this.getX(), this.getY(), this.getZ(),
                0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    protected void spawnImpactParticles(Vec3d pos) {
        World world = this.getWorld();
        
        // Large flame burst
        for (int i = 0; i < 20; i++) {
            world.addParticle(ParticleTypes.FLAME,
                pos.x + (world.getRandom().nextGaussian() * 1.0D),
                pos.y + (world.getRandom().nextGaussian() * 1.0D),
                pos.z + (world.getRandom().nextGaussian() * 1.0D),
                world.getRandom().nextGaussian() * 0.3D,
                world.getRandom().nextGaussian() * 0.3D,
                world.getRandom().nextGaussian() * 0.3D);
        }
        
        // Explosion particles
        for (int i = 0; i < 10; i++) {
            world.addParticle(ParticleTypes.EXPLOSION,
                pos.x, pos.y, pos.z,
                world.getRandom().nextGaussian() * 0.5D,
                world.getRandom().nextGaussian() * 0.5D,
                world.getRandom().nextGaussian() * 0.5D);
        }
    }
    
    @Override
    protected ParticleEffect getTrailParticle() {
        return ParticleTypes.FLAME;
    }
    
    @Override
    protected RegistryEntry.Reference<SoundEvent> getHitSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE;
    }
    
    @Override
    protected void onCollision(net.minecraft.util.hit.HitResult hitResult) {
        if (!this.getWorld().isClient) {
            // Spawn impact particles before discarding
            this.spawnImpactParticles(hitResult.getPos());
        }
        super.onCollision(hitResult);
    }
    
    // Configuration methods
    public void setExplosionPower(float power) {
        this.explosionPower = power;
    }
    
    public void setDestroysBlocks(boolean destroys) {
        this.destroysBlocks = destroys;
    }
    
    public void setCreatesFire(boolean creates) {
        this.createsFire = creates;
    }
    
    public void setBurnDuration(int duration) {
        this.burnDuration = duration;
    }
    
    // Getters
    public float getExplosionPower() {
        return explosionPower * (1.0F + (spellLevel - 1) * 0.5F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }
}