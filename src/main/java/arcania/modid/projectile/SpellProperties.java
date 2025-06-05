package arcania.modid.projectile;

public class SpellProperties {
    private boolean hasGravity;
    private float damage;
    private double speed;
    private int maxAge;
    private int spellLevel;

    public SpellProperties() {
        this.hasGravity = false;
        this.damage = 6.0F;
        this.speed = 1.5D;
        this.maxAge = 200;
        this.spellLevel = 1;
    }

    public SpellProperties(boolean hasGravity, float damage, double speed, int maxAge, int spellLevel) {
        this.hasGravity = hasGravity;
        this.damage = damage;
        this.speed = speed;
        this.maxAge = maxAge;
        this.spellLevel = spellLevel;
    }

    // Getters
    public boolean hasGravity() {
        return hasGravity;
    }

    public float getDamage() {
        return damage;
    }

    public double getSpeed() {
        return speed;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

    // Setters
    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setSpellLevel(int spellLevel) {
        this.spellLevel = spellLevel;
    }
}