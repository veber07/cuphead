

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void testPlayerTakeDamage() {
        Player player = new Player(800, 600);
        int initialHealth = player.getHealth();
        player.takeDamage(15);
        assertEquals(initialHealth - 15, player.getHealth());
    }

    @Test
    void testPlayerSetX() {
        Player player = new Player(800, 600);
        player.setX(123);
        assertEquals(123, player.getX());
    }

    @Test
    void testBossTakeDamage() {
        Player player = new Player(800, 600);
        Boss boss = new Boss(0, 0, 800, 600, player);
        int initialHealth = boss.getHealth();
        boss.takeDamage(25);
        assertEquals(initialHealth - 25, boss.getHealth());
    }

    @Test
    void testBulletUpdateMoves() {
        Bullet bullet = new Bullet(100, 100, 5, 0, ProjectileType.PLAYER_BULLET, ProjectileTypeMain.PLAYER_BULLET, 10, 10);
        float oldX = bullet.getX();
        bullet.update();
        assertTrue(bullet.getX() > oldX);
    }

    @Test
    void testBulletHit() {
        Bullet bullet = new Bullet(50, 50, 0, 0, ProjectileType.PLAYER_BULLET, ProjectileTypeMain.PLAYER_BULLET, 10, 10);
        assertTrue(bullet.bulletHit(50, 50, 10, 10));
    }
}
