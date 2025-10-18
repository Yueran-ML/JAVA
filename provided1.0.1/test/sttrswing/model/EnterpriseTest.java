package sttrswing.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;
import sttrswing.model.enums.Faction;

public class EnterpriseTest {

  @Test
  public void testConstructorInitialization() {
    // Arrange
    int x = 7;
    int y = 9;

    // Act
    Enterprise enterprise = new Enterprise(x, y);

    // Assert
    assertEquals(x, enterprise.getX());
    assertEquals(y, enterprise.getY());
    assertEquals(2500, enterprise.energy());
    assertEquals(500, enterprise.shields());
    assertEquals(10, enterprise.torpedoAmmo());
    assertTrue(enterprise.isAlive());
    assertEquals(Faction.FEDERATION, enterprise.faction());
    assertEquals("{Ë}", enterprise.symbol());
  }

  @Test
  public void testFullConstructorInitialization() {
    // Arrange
    int x = 3;
    int y = 5;
    int energy = 1400;
    int shields = 750;
    int torpedoes = 4;

    // Act
    Enterprise enterprise = new Enterprise(x, y, energy, shields, torpedoes);

    // Assert
    assertEquals(x, enterprise.getX());
    assertEquals(y, enterprise.getY());
    assertEquals(energy, enterprise.energy());
    assertEquals(shields, enterprise.shields());
    assertEquals(torpedoes, enterprise.torpedoAmmo());
    assertTrue(enterprise.isAlive());
    assertEquals("{Ë}", enterprise.symbol());
  }

  @Test
  public void testFireTorpedoReducesAmmo() {
    // Arrange
    Enterprise enterprise = new Enterprise(4, 4);
    int startingAmmo = enterprise.torpedoAmmo();

    // Act
    Entity torpedo = enterprise.fireTorpedo();

    // Assert
    assertNotNull(torpedo);
    assertEquals(startingAmmo - 1, enterprise.torpedoAmmo());
    assertEquals(enterprise.getX(), torpedo.getX());
    assertEquals(enterprise.getY(), torpedo.getY());
  }

  @Test
  public void testFireTorpedoWhenOutOfAmmo() {
    // Arrange
    Enterprise enterprise = new Enterprise(1, 1, 2000, 400, 0);

    // Act
    Entity torpedo = enterprise.fireTorpedo();

    // Assert
    assertNull(torpedo);
    assertEquals(0, enterprise.torpedoAmmo());
  }

  @Test
  public void testHasTorpedoAmmo() {
    // Arrange
    Enterprise enterprise = new Enterprise(0, 0);

    // Act & Assert
    assertTrue(enterprise.hasTorpedoAmmo());

    enterprise = new Enterprise(2, 2, 1000, 300, 0);
    assertFalse(enterprise.hasTorpedoAmmo());
  }

  @Test
  public void testTransferEnergyToShieldsNormal() {
    // Arrange
    Enterprise enterprise = new Enterprise(2, 2, 1000, 200, 5);
    int transferAmount = 250;

    // Act
    int transferred = enterprise.transferEnergyToShields(transferAmount);

    // Assert
    assertEquals(transferAmount, transferred);
    assertEquals(750, enterprise.energy());
    assertEquals(450, enterprise.shields());
  }

  @Test
  public void testTransferEnergyToShieldsMoreThanAvailable() {
    // Arrange
    Enterprise enterprise = new Enterprise(6, 6, 120, 50, 3);

    // Act
    int transferred = enterprise.transferEnergyToShields(500);

    // Assert
    assertEquals(120, transferred);
    assertEquals(0, enterprise.energy());
    assertEquals(170, enterprise.shields());
  }

  @Test
  public void testTransferEnergyToShieldsWhenEnergyLow() {
    // Arrange
    Enterprise enterprise = new Enterprise(8, 8, 1, 90, 2);

    // Act
    int transferred = enterprise.transferEnergyToShields(25);

    // Assert
    assertEquals(1, transferred);
    assertEquals(0, enterprise.energy());
    assertEquals(91, enterprise.shields());
  }

  @Test
  public void testDrainEnergyNormal() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5, 900, 300, 1);

    // Act
    int drained = enterprise.drainEnergy(400);

    // Assert
    assertEquals(400, drained);
    assertEquals(500, enterprise.energy());
  }

  @Test
  public void testDrainEnergyMoreThanAvailable() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5, 90, 300, 1);

    // Act
    int drained = enterprise.drainEnergy(400);

    // Assert
    assertEquals(90, drained);
    assertEquals(0, enterprise.energy());
  }

  @Test
  public void testDrainEnergyWhenEnergyLow() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5, 1, 300, 1);

    // Act
    int drained = enterprise.drainEnergy(10);

    // Assert
    assertEquals(1, drained);
    assertEquals(0, enterprise.energy());
  }

  @Test
  public void testDrainEnergyWhenDepleted() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5, 0, 300, 1);

    // Act
    int drained = enterprise.drainEnergy(50);

    // Assert
    assertEquals(0, drained);
    assertEquals(0, enterprise.energy());
  }

  @Test
  public void testHitReducesShieldsFirst() {
    // Arrange
    Enterprise enterprise = new Enterprise(3, 3, 500, 200, 2);

    // Act
    enterprise.hit(100);

    // Assert
    assertEquals(100, enterprise.shields());
    assertEquals(500, enterprise.energy());
    assertTrue(enterprise.isAlive());
  }

  @Test
  public void testHitOverflowsShieldsReducesEnergy() {
    // Arrange
    Enterprise enterprise = new Enterprise(3, 3, 600, 150, 2);

    // Act
    enterprise.hit(200);

    // Assert
    assertEquals(0, enterprise.shields());
    assertEquals(600, enterprise.energy());
    assertFalse(enterprise.isAlive());
  }

  @Test
  public void testHitWhenShieldsZero() {
    // Arrange
    Enterprise enterprise = new Enterprise(3, 3, 500, 0, 2);

    // Act
    enterprise.hit(200);

    // Assert
    assertEquals(0, enterprise.shields());
    assertEquals(500, enterprise.energy());
    assertFalse(enterprise.isAlive());
  }

  @Test
  public void testHitLethalDamage() {
    // Arrange
    Enterprise enterprise = new Enterprise(3, 3, 400, 50, 2);

    // Act
    enterprise.hit(500);

    // Assert
    assertFalse(enterprise.isAlive());
    assertEquals(400, enterprise.energy());
    assertEquals(0, enterprise.shields());
  }

  @Test
  public void testHitWithZeroDamage() {
    // Arrange
    Enterprise enterprise = new Enterprise(2, 2, 600, 200, 3);
    int initialEnergy = enterprise.energy();
    int initialShields = enterprise.shields();

    // Act
    enterprise.hit(0);

    // Assert
    assertEquals(initialShields, enterprise.shields());
    assertEquals(initialEnergy, enterprise.energy());
    assertTrue(enterprise.isAlive());
  }

  @Test
  public void testHitWithNegativeDamage() {
    // Arrange
    Enterprise enterprise = new Enterprise(3, 3, 400, 150, 2);
    int initialEnergy = enterprise.energy();
    int initialShields = enterprise.shields();

    // Act
    enterprise.hit(-100);

    // Assert
    assertTrue(enterprise.shields() >= initialShields);
    assertEquals(initialEnergy, enterprise.energy());
    assertTrue(enterprise.isAlive());
  }

  @Test
  public void testIsAlive() {
    // Arrange
    Enterprise enterprise = new Enterprise(4, 4, 300, 200, 2);

    // Act & Assert
    assertTrue(enterprise.isAlive());

    enterprise.hit(600);
    assertFalse(enterprise.isAlive());
  }

  @Test
  public void testExportFormatWithNonDefaultValues() {
    // Arrange
    Enterprise enterprise = new Enterprise(1, 2, 321, 654, 7);

    // Act
    String export = enterprise.export();

    // Assert
    assertEquals("[e] x:1 y:2 e:321 s:654 t:7 |\n", export);
  }

  @Test
  public void testGetX() {
    // Arrange
    Enterprise enterprise = new Enterprise(10, 11);

    // Act
    int x = enterprise.getX();

    // Assert
    assertEquals(10, x);
  }

  @Test
  public void testGetY() {
    // Arrange
    Enterprise enterprise = new Enterprise(10, 11);

    // Act
    int y = enterprise.getY();

    // Assert
    assertEquals(11, y);
  }

  @Test
  public void testSetX() {
    // Arrange
    Enterprise enterprise = new Enterprise(10, 11);

    // Act
    enterprise.setX(15);

    // Assert
    assertEquals(15, enterprise.getX());
  }

  @Test
  public void testSetY() {
    // Arrange
    Enterprise enterprise = new Enterprise(10, 11);

    // Act
    enterprise.setY(19);

    // Assert
    assertEquals(19, enterprise.getY());
  }

  @Test
  public void testGetFaction() {
    // Arrange
    Enterprise enterprise = new Enterprise(0, 0);

    // Act
    Faction faction = enterprise.faction();

    // Assert
    assertEquals(Faction.FEDERATION, faction);
  }

  @Test
  public void testSymbol() {
    // Arrange
    Enterprise enterprise = new Enterprise(0, 0);

    // Act
    String symbol = enterprise.symbol();

    // Assert
    assertEquals("{Ë}", symbol);
  }

  @Test
  public void testDockedWhenAdjacent() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5);
    Starbase adjacent = new Starbase(6, 5);
    ArrayList<Starbase> starbases = new ArrayList<>();
    starbases.add(adjacent);

    // Act
    boolean docked = enterprise.docked(starbases);

    // Assert
    assertTrue(docked);
  }

  @Test
  public void testDockedWhenNotAdjacent() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5);
    Starbase far = new Starbase(10, 10);
    ArrayList<Starbase> starbases = new ArrayList<>();
    starbases.add(far);

    // Act
    boolean docked = enterprise.docked(starbases);

    // Assert
    assertFalse(docked);
  }

  @Test
  public void testDockedWithEmptyList() {
    // Arrange
    Enterprise enterprise = new Enterprise(5, 5);
    ArrayList<Starbase> starbases = new ArrayList<>();

    // Act
    boolean docked = enterprise.docked(starbases);

    // Assert
    assertFalse(docked);
  }

  @Test
  public void testGainEnergyRespectsMax() {
    // Arrange
    Enterprise enterprise = new Enterprise(2, 2, 2950, 200, 2);

    // Act
    enterprise.gainEnergy(200);

    // Assert
    assertEquals(3000, enterprise.energy());
  }

  @Test
  public void testHealIncreasesEnergy() {
    // Arrange
    Enterprise enterprise = new Enterprise(2, 2, 1000, 200, 2);

    // Act
    enterprise.heal(500);

    // Assert
    assertEquals(1500, enterprise.energy());
  }
}
