
/**
 * Defines various types of projectiles in the game.
 */
public enum ProjectileType {
    PLAYER_BULLET(10),
    BOSS_BALL(1),
    BOSS_BIG_BALL(3),
    BOSS_SPREAD(1),
    LASER_LONG(1),
    LASER_SHORT(1);


    private final int damage;

    ProjectileType(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
