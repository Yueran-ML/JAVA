package sttrswing.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Test;
import sttrswing.model.enums.Faction;
import sttrswing.model.interfaces.Hittable;

public class KlingonTest {

  private int getEnergy(final Klingon klingon) {
    try {
      Field field = Klingon.class.getDeclaredField("energy");
      field.setAccessible(true);
      Stat stat = (Stat) field.get(klingon);
      return stat.get();
    } catch (ReflectiveOperationException e) {
      throw new AssertionError(e);
    }
  }

  @Test
  public void testConstructorInitialization() {
    // Arrange
    int x = 4;
    int y = 6;

    // Act
    Klingon klingon = new Klingon(x, y);

    // Assert
    assertEquals(x, klingon.getX());
    assertEquals(y, klingon.getY());
    assertEquals(300, getEnergy(klingon));
    assertEquals(" ? ", klingon.symbol());
    assertEquals(Faction.NEUTRAL, klingon.faction());
    assertFalse(klingon.isMarkedForRemoval());
  }

  @Test
  public void testGetEnergyInitial() {
    // Arrange
    Klingon klingon = new Klingon(1, 2);

    // Act
    int energy = getEnergy(klingon);

    // Assert
    assertEquals(300, energy);
  }

  @Test
  public void testAttackDealsOneThirdDamage() {
    // Arrange
    Klingon klingon = new Klingon(2, 3);
    RecordingTarget target = new RecordingTarget();

    // Act
    int damage = klingon.attack(target);

    // Assert
    assertEquals(100, damage);
    assertEquals(100, target.lastDamage);
    assertEquals(300, getEnergy(klingon));
  }

  @Test
  public void testHitReducesEnergy() {
    // Arrange
    Klingon klingon = new Klingon(5, 5);

    // Act
    klingon.hit(75);

    // Assert
    assertEquals(225, getEnergy(klingon));
    assertFalse(klingon.isMarkedForRemoval());
  }

  @Test
  public void testHitLethalDamage() {
    // Arrange
    Klingon klingon = new Klingon(5, 5);

    // Act
    klingon.hit(400);

    // Assert
    assertEquals(0, getEnergy(klingon));
    assertTrue(klingon.isMarkedForRemoval());
  }

  @Test
  public void testHitWhenAlreadyDestroyed() {
    // Arrange
    Klingon klingon = new Klingon(5, 5);
    klingon.hit(400);

    // Act
    klingon.hit(50);

    // Assert
    assertEquals(0, getEnergy(klingon));
    assertTrue(klingon.isMarkedForRemoval());
  }

  @Test
  public void testHitWithNegativeDamage() {
    // Arrange
    Klingon klingon = new Klingon(5, 5);

    // Act
    klingon.hit(-25);

    // Assert
    assertEquals(300, getEnergy(klingon));
    assertFalse(klingon.isMarkedForRemoval());
  }

  @Test
  public void testHitWithZeroDamage() {
    // Arrange
    Klingon klingon = new Klingon(4, 4);
    int initialEnergy = getEnergy(klingon);

    // Act
    klingon.hit(0);

    // Assert
    assertEquals(initialEnergy, getEnergy(klingon));
    assertFalse(klingon.isMarkedForRemoval());
  }

  @Test
  public void testIsAlive() {
    // Arrange
    Klingon klingon = new Klingon(0, 0);

    // Act & Assert
    assertFalse(klingon.isMarkedForRemoval());

    klingon.hit(500);

    assertTrue(klingon.isMarkedForRemoval());
  }

  @Test
  public void testFactionUpdatesAfterScan() {
    // Arrange
    Klingon klingon = new Klingon(1, 1);

    // Act
    klingon.scan();

    // Assert
    assertEquals(Faction.KLINGON, klingon.faction());
  }

  @Test
  public void testSymbolStates() {
    // Arrange
    Klingon klingon = new Klingon(2, 2);
    klingon.scan();

    // Act & Assert
    assertEquals("+K+", klingon.symbol());

    klingon.hit(160);

    assertEquals("+k+", klingon.symbol());

    klingon.hit(50);

    assertEquals("-k-", klingon.symbol());
  }

  @Test
  public void testGetFaction() {
    // Arrange
    Klingon klingon = new Klingon(9, 9);

    // Act
    klingon.scan();
    Faction faction = klingon.faction();

    // Assert
    assertEquals(Faction.KLINGON, faction);
  }

  @Test
  public void testSymbolBeforeScan() {
    // Arrange
    Klingon klingon = new Klingon(3, 3);

    // Act
    String symbol = klingon.symbol();

    // Assert
    assertEquals(" ? ", symbol);
  }

  @Test
  public void testCoordinateAccessorsAndMutators() {
    // Arrange
    Klingon klingon = new Klingon(7, 8);

    // Act
    klingon.setX(10);
    klingon.setY(11);
    klingon.adjustPosition(-3, 4);

    // Assert
    assertEquals(7, klingon.getX());
    assertEquals(15, klingon.getY());
  }

  @Test
  public void testAdjustPositionNegative() {
    // Arrange
    Klingon klingon = new Klingon(10, 10);

    // Act
    klingon.adjustPosition(-4, -6);

    // Assert
    assertEquals(6, klingon.getX());
    assertEquals(4, klingon.getY());
  }

  @Test
  public void testMarkForRemoval() {
    // Arrange
    Klingon klingon = new Klingon(0, 0);

    // Act
    klingon.remove();

    // Assert
    assertTrue(klingon.isMarkedForRemoval());
  }

  @Test
  public void testMarkedForRemovalAfterLethalHit() {
    // Arrange
    Klingon klingon = new Klingon(0, 0);

    // Act
    klingon.hit(600);

    // Assert
    assertTrue(klingon.isMarkedForRemoval());
  }

  private static class RecordingTarget implements Hittable {
    private int lastDamage;

    @Override
    public void hit(int damage) {
      this.lastDamage = damage;
    }
  }
}

