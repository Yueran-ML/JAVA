package sttrswing.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Test;
import sttrswing.model.enums.Faction;

public class StarbaseTest {

  @Test
  public void testConstructorInitialization() {
    // Arrange
    int x = 2;
    int y = 7;

    // Act
    Starbase starbase = new Starbase(x, y);

    // Assert
    assertEquals(x, starbase.getX());
    assertEquals(y, starbase.getY());
    assertFalse(starbase.isMarkedForRemoval());
    assertEquals(" ? ", starbase.symbol());
    assertEquals(Faction.NEUTRAL, starbase.faction());

    starbase.scan();
    assertEquals(Faction.FEDERATION, starbase.faction());
    assertEquals("[S]", starbase.symbol());
  }

  @Test
  public void testHitDoesNothing() {
    // Arrange
    Starbase starbase = new Starbase(3, 3);
    starbase.scan();
    int initialEnergy = getEnergy(starbase);

    // Act
    starbase.hit(75);
    int afterPositiveDamage = getEnergy(starbase);
    starbase.hit(0);
    int afterZeroDamage = getEnergy(starbase);
    starbase.hit(-25);
    int afterNegativeDamage = getEnergy(starbase);

    // Assert
    assertEquals(initialEnergy - 75, afterPositiveDamage);
    assertEquals(afterPositiveDamage, afterZeroDamage);
    assertEquals(afterPositiveDamage + 25, afterNegativeDamage);
    assertFalse(starbase.isMarkedForRemoval());
    assertEquals("[S]", starbase.symbol());
  }

  @Test
  public void testHitLethalDamageMarksForRemoval() {
    // Arrange
    Starbase starbase = new Starbase(1, 1);
    starbase.scan();

    // Act
    starbase.hit(500);

    // Assert
    assertEquals(0, getEnergy(starbase));
    assertTrue(starbase.isMarkedForRemoval());
    assertEquals("|s|", starbase.symbol());
  }

  @Test
  public void testIsAliveAlwaysTrue() {
    // Arrange
    Starbase starbase = new Starbase(4, 4);
    int initialEnergy = getEnergy(starbase);

    // Act
    starbase.hit(100);

    // Assert
    assertEquals(initialEnergy - 100, getEnergy(starbase));
    assertFalse(starbase.isMarkedForRemoval());
  }

  @Test
  public void testHealShieldsRestoresToFull() {
    // Arrange
    Starbase starbase = new Starbase(5, 5);
    starbase.scan();
    Enterprise enterprise = new Enterprise(6, 6, 100, 300, 4);
    int originalEnergy = enterprise.energy();
    int starbaseEnergy = getEnergy(starbase);

    // Act
    starbase.attemptHeal(enterprise);

    // Assert
    assertEquals(originalEnergy + starbaseEnergy, enterprise.energy());
    assertEquals(0, getEnergy(starbase));
    assertFalse(starbase.isMarkedForRemoval());
    assertEquals("|s|", starbase.symbol());
  }

  @Test
  public void testHealShieldsOnFullShields() {
    // Arrange
    Starbase starbase = new Starbase(2, 2);
    Enterprise enterprise = new Enterprise(3, 3);
    int originalEnergy = enterprise.energy();
    int starbaseEnergy = getEnergy(starbase);

    // Act
    starbase.attemptHeal(enterprise);

    // Assert
    assertEquals(originalEnergy + starbaseEnergy, enterprise.energy());
    assertEquals(0, getEnergy(starbase));
  }

  @Test
  public void testAttemptHealRequiresAdjacency() {
    // Arrange
    Starbase starbase = new Starbase(0, 0);
    Enterprise farEnterprise = new Enterprise(5, 5);
    int originalEnergy = farEnterprise.energy();
    int originalStarbaseEnergy = getEnergy(starbase);

    // Act
    starbase.attemptHeal(farEnterprise);

    // Assert
    assertEquals(originalEnergy, farEnterprise.energy());
    assertEquals(originalStarbaseEnergy, getEnergy(starbase));

    // Arrange
    Enterprise overlappingEnterprise = new Enterprise(0, 0);
    int overlappingOriginalEnergy = overlappingEnterprise.energy();

    // Act
    starbase.attemptHeal(overlappingEnterprise);

    // Assert
    assertEquals(overlappingOriginalEnergy, overlappingEnterprise.energy());
    assertEquals(originalStarbaseEnergy, getEnergy(starbase));
  }

  @Test
  public void testGetFaction() {
    // Arrange
    Starbase starbase = new Starbase(1, 2);

    // Act
    starbase.scan();

    // Assert
    assertEquals(Faction.FEDERATION, starbase.faction());
  }

  @Test
  public void testSymbol() {
    // Arrange
    Starbase starbase = new Starbase(9, 9);

    // Act & Assert
    assertEquals(" ? ", starbase.symbol());

    starbase.scan();
    assertEquals("[S]", starbase.symbol());

    starbase.hit(400);
    assertEquals("|s|", starbase.symbol());
  }

  @Test
  public void testCoordinateAccessors() {
    // Arrange
    Starbase starbase = new Starbase(1, 1);

    // Act
    starbase.setX(4);
    starbase.setY(6);
    starbase.adjustPosition(-2, 3);

    // Assert
    assertEquals(2, starbase.getX());
    assertEquals(9, starbase.getY());
  }

  @Test
  public void testMarkForRemovalFlags() {
    // Arrange
    Starbase starbase = new Starbase(7, 7);

    // Act & Assert
    assertFalse(starbase.isMarkedForRemoval());

    starbase.remove();
    assertTrue(starbase.isMarkedForRemoval());

    starbase.remove();
    assertTrue(starbase.isMarkedForRemoval());
  }

  private int getEnergy(Starbase starbase) {
    try {
      Field energyField = Starbase.class.getDeclaredField("energy");
      energyField.setAccessible(true);
      Stat stat = (Stat) energyField.get(starbase);
      return stat.get();
    } catch (ReflectiveOperationException e) {
      throw new AssertionError("Unable to access energy field", e);
    }
  }
}
