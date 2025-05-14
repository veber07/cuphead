public enum ProjectileType {
    PLAYER_BULLET(10),
    BOSS_BALL(15);

    private final int damage;

    ProjectileType(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
