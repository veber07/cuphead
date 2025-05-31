public enum ProjectileType {
    PLAYER_BULLET(10),
    BOSS_BALL(15),
    BOSS_BIG_BALL(150),
    BOSS_SPREAD(15),
    LASER_LOW(5),
    LASER_LONG(5),
    LASER_SHORT(7),
    LASER_HIGH  (10);

    private final int damage;

    ProjectileType(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
