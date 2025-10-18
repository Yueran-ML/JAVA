package sttrswing.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class QuadrantTest {

  @Test
  public void testConstructorInitialization() {
    // Arrange
    int x = 2;
    int y = 5;

    // Act
    Quadrant quadrant = new Quadrant(x, y, 1, 2, 3);

    // Assert
    assertEquals(x, quadrant.getX());
    assertEquals(y, quadrant.getY());
    assertEquals(3, quadrant.starCount());
    assertEquals(1, quadrant.starbaseCount());
    assertEquals(2, quadrant.klingonCount());
  }

  @Test
  public void testRandomConstructorProducesValidQuadrant() {
    // Arrange & Act
    Quadrant quadrant = new Quadrant(4, 6);

    // Assert
    assertEquals(4, quadrant.getX());
    assertEquals(6, quadrant.getY());
    int totalEntities = quadrant.starCount() + quadrant.starbaseCount() + quadrant.klingonCount();
    assertTrue(totalEntities >= 0 && totalEntities <= 64);

    Set<String> occupied = new HashSet<>();
    for (Starbase starbase : quadrant.starbases()) {
      occupied.add(starbase.getX() + "," + starbase.getY());
    }
    for (Klingon klingon : quadrant.klingons()) {
      occupied.add(klingon.getX() + "," + klingon.getY());
    }
    for (Star star : quadrant.stars()) {
      occupied.add(star.getX() + "," + star.getY());
    }

    assertEquals(totalEntities, occupied.size());
    assertEquals(
        "" + quadrant.stars().size() + quadrant.starbases().size() + quadrant.klingons().size(),
        quadrant.symbol());
  }

  @Test
  public void testConstructorRandomness() {
    // Arrange
    Set<String> signatures = new HashSet<>();

    // Act
    for (int i = 0; i < 20; i++) {
      Quadrant quadrant = new Quadrant(5, 5);
      signatures.add(layoutSignature(quadrant));
    }

    // Assert
    assertTrue("Expected at least two different quadrant layouts", signatures.size() > 1);
  }

  @Test
  public void testConstructorEntityPlacement() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 2, 3, 4);

    // Act
    List<Starbase> starbases = quadrant.starbases();
    List<Klingon> klingons = quadrant.klingons();
    List<Star> stars = quadrant.stars();

    // Assert
    assertEquals(2, starbases.size());
    assertEquals(3, klingons.size());
    assertEquals(4, stars.size());
    assertEquals("423", quadrant.symbol());

    Set<String> positions = new HashSet<>();
    for (Starbase starbase : starbases) {
      positions.add(starbase.getX() + "," + starbase.getY());
      assertTrue(starbase.getX() >= 0 && starbase.getX() < 8);
      assertTrue(starbase.getY() >= 0 && starbase.getY() < 8);
    }
    for (Klingon klingon : klingons) {
      positions.add(klingon.getX() + "," + klingon.getY());
      assertTrue(klingon.getX() >= 0 && klingon.getX() < 8);
      assertTrue(klingon.getY() >= 0 && klingon.getY() < 8);
    }
    for (Star star : stars) {
      positions.add(star.getX() + "," + star.getY());
      assertTrue(star.getX() >= 0 && star.getX() < 8);
      assertTrue(star.getY() >= 0 && star.getY() < 8);
    }
    assertEquals(2 + 3 + 4, positions.size());
  }

  @Test
  public void testGetEntitiesSize() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 3, 2, 4);

    // Act
    int fromLists = quadrant.starbaseCount() + quadrant.klingonCount() + quadrant.starCount();
    int counted = 0;
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        if (quadrant.getEntityAt(x, y) != null) {
          counted += 1;
        }
      }
    }

    // Assert
    assertEquals(fromLists, counted);
  }

  @Test
  public void testConstructorWithZeroEntities() {
    // Arrange
    Quadrant quadrant = new Quadrant(3, 4, 0, 0, 0);

    // Act & Assert
    assertEquals(0, quadrant.starbases().size());
    assertEquals(0, quadrant.klingons().size());
    assertEquals(0, quadrant.stars().size());
    assertEquals("000", quadrant.symbol());
  }

  @Test
  public void testConstructorPlacementAvoidsCollisions() {
    // Arrange
    Quadrant quadrant = new Quadrant(1, 1, 5, 6, 7);

    // Act
    Set<String> positions = new HashSet<>();
    for (Starbase starbase : quadrant.starbases()) {
      positions.add(starbase.getX() + "," + starbase.getY());
    }
    for (Klingon klingon : quadrant.klingons()) {
      positions.add(klingon.getX() + "," + klingon.getY());
    }
    for (Star star : quadrant.stars()) {
      positions.add(star.getX() + "," + star.getY());
    }

    // Assert
    assertEquals(5 + 6 + 7, positions.size());
  }

  @Test
  public void testGetX() {
    // Arrange
    Quadrant quadrant = new Quadrant(6, 7, 1, 1, 1);

    // Act
    int x = quadrant.getX();

    // Assert
    assertEquals(6, x);
  }

  @Test
  public void testGetY() {
    // Arrange
    Quadrant quadrant = new Quadrant(6, 7, 1, 1, 1);

    // Act
    int y = quadrant.getY();

    // Assert
    assertEquals(7, y);
  }

  @Test
  public void testGetSymbolAt() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);
    Starbase starbase = quadrant.starbases().get(0);
    Klingon klingon = quadrant.klingons().get(0);
    Star star = quadrant.stars().get(0);

    // Act & Assert
    assertEquals(" ? ", quadrant.getSymbolAt(starbase.getX(), starbase.getY()));
    assertEquals(" ? ", quadrant.getSymbolAt(klingon.getX(), klingon.getY()));
    assertEquals(" * ", quadrant.getSymbolAt(star.getX(), star.getY()));
    assertEquals("   ", quadrant.getSymbolAt(7, 7));
  }

  @Test
  public void testGetEntityAtOccupied() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);
    Starbase starbase = quadrant.starbases().get(0);
    Klingon klingon = quadrant.klingons().get(0);
    Star star = quadrant.stars().get(0);

    // Act & Assert
    assertSame(starbase, quadrant.getEntityAt(starbase.getX(), starbase.getY()));
    assertSame(klingon, quadrant.getEntityAt(klingon.getX(), klingon.getY()));
    assertSame(star, quadrant.getEntityAt(star.getX(), star.getY()));
  }

  @Test
  public void testGetEntityAtEmpty() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);

    // Act
    Entity entity = quadrant.getEntityAt(7, 7);

    // Assert
    assertNull(entity);
  }

  @Test
  public void testGetEntityAtOutOfBounds() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);

    // Act & Assert
    assertNull(quadrant.getEntityAt(-1, 0));
    assertNull(quadrant.getEntityAt(0, -1));
    assertNull(quadrant.getEntityAt(8, 8));
  }

  @Test
  public void testKlingonsListContainsOnlyKlingons() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 2, 4, 1);

    // Act
    List<Klingon> klingons = quadrant.klingons();

    // Assert
    assertEquals(4, klingons.size());
    for (Klingon klingon : klingons) {
      assertNotNull(klingon);
      assertTrue(klingon instanceof Klingon);
      assertSame(klingon, quadrant.getEntityAt(klingon.getX(), klingon.getY()));
    }
  }

  @Test
  public void testStarbasesListContainsOnlyStarbases() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 3, 1, 2);

    // Act
    List<Starbase> starbases = quadrant.starbases();

    // Assert
    assertEquals(3, starbases.size());
    for (Starbase starbase : starbases) {
      assertNotNull(starbase);
      assertTrue(starbase instanceof Starbase);
      assertSame(starbase, quadrant.getEntityAt(starbase.getX(), starbase.getY()));
    }
  }

  @Test
  public void testStarsListContainsOnlyStars() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 2, 5);

    // Act
    List<Star> stars = quadrant.stars();

    // Assert
    assertEquals(5, stars.size());
    for (Star star : stars) {
      assertNotNull(star);
      assertTrue(star instanceof Star);
      assertSame(star, quadrant.getEntityAt(star.getX(), star.getY()));
    }
  }

  @Test
  public void testKlingonCountAfterCleanup() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 0, 2, 0);
    Klingon klingon = quadrant.klingons().get(0);

    // Act
    klingon.hit(400);
    assertEquals(1, quadrant.klingonsMarkedForRemovalCount());
    quadrant.cleanup();

    // Assert
    assertEquals(1, quadrant.klingonCount());
    assertEquals(0, quadrant.klingonsMarkedForRemovalCount());
  }

  @Test
  public void testKlingonsMarkedForRemovalCountInitial() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 2, 3);

    // Act
    int marked = quadrant.klingonsMarkedForRemovalCount();

    // Assert
    assertEquals(0, marked);
  }

  @Test
  public void testCleanupRemovesStarbases() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 2, 0, 0);
    Starbase starbase = quadrant.starbases().get(0);

    // Act
    starbase.remove();
    quadrant.cleanup();

    // Assert
    assertEquals(1, quadrant.starbaseCount());
    assertFalse(quadrant.starbases().contains(starbase));
  }

  @Test
  public void testRemoveMarkedEntitiesUpdatesGetEntityAt() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 0);
    Starbase starbase = quadrant.starbases().get(0);
    int x = starbase.getX();
    int y = starbase.getY();

    // Act
    starbase.remove();
    quadrant.cleanup();

    // Assert
    assertNull(quadrant.getEntityAt(x, y));
  }

  @Test
  public void testCleanupWhenNoneMarkedDoesNothing() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);
    int klingonsBefore = quadrant.klingonCount();
    int starbasesBefore = quadrant.starbaseCount();

    // Act
    quadrant.cleanup();

    // Assert
    assertEquals(klingonsBefore, quadrant.klingonCount());
    assertEquals(starbasesBefore, quadrant.starbaseCount());
  }

  @Test
  public void testSymbolUpdatesAfterRemoval() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 2, 1);
    String initialSymbol = quadrant.symbol();

    // Act
    quadrant.klingons().get(0).hit(400);
    quadrant.cleanup();
    String updatedSymbol = quadrant.symbol();

    // Assert
    assertEquals("111", updatedSymbol);
    assertNotEquals(initialSymbol, updatedSymbol);
  }

  @Test
  public void testToStringUpdatesAfterRemoval() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 2, 3);
    String initial = quadrant.toString();

    // Act
    quadrant.klingons().get(0).hit(400);
    quadrant.cleanup();
    String updated = quadrant.toString();

    // Assert
    assertTrue(updated.contains("Enemies: 1"));
    assertNotEquals(initial, updated);
  }

  @Test
  public void testHitDamagesAllKlingons() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 0, 2, 0);
    Klingon first = quadrant.klingons().get(0);
    Klingon second = quadrant.klingons().get(1);
    int firstEnergy = getEnergy(first);
    int secondEnergy = getEnergy(second);

    // Act
    quadrant.hit(50);

    // Assert
    assertEquals(firstEnergy - 50, getEnergy(first));
    assertEquals(secondEnergy - 50, getEnergy(second));
  }

  @Test
  public void testHitDamagesAllKlingonsEqually() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 0, 3, 0);
    List<Integer> before = new ArrayList<>();
    for (Klingon klingon : quadrant.klingons()) {
      before.add(getEnergy(klingon));
    }

    // Act
    quadrant.hit(60);

    // Assert
    int expectedDrop = before.get(0) - getEnergy(quadrant.klingons().get(0));
    for (int i = 0; i < quadrant.klingons().size(); i++) {
      int delta = before.get(i) - getEnergy(quadrant.klingons().get(i));
      assertEquals(expectedDrop, delta);
    }
  }

  @Test
  public void testHitDoesNotDamageOtherEntities() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 2, 1);
    Starbase starbase = quadrant.starbases().get(0);
    int starbaseEnergy = getEnergy(starbase);

    // Act
    quadrant.hit(120);

    // Assert
    assertEquals(starbaseEnergy, getEnergy(starbase));
    assertEquals(1, quadrant.starbases().size());
  }

  @Test
  public void testHitWithZeroDamage() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 0, 2, 0);
    List<Integer> energies = new ArrayList<>();
    for (Klingon klingon : quadrant.klingons()) {
      energies.add(getEnergy(klingon));
    }

    // Act
    quadrant.hit(0);

    // Assert
    for (int i = 0; i < quadrant.klingons().size(); i++) {
      assertEquals(energies.get(i).intValue(), getEnergy(quadrant.klingons().get(i)));
    }
    assertEquals(0, quadrant.klingonsMarkedForRemovalCount());
  }

  @Test
  public void testKlingonsMarkedForRemovalCountAfterHit() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 0, 3, 0);

    // Act
    quadrant.klingons().get(0).hit(350);
    quadrant.klingons().get(1).hit(350);

    // Assert
    assertEquals(2, quadrant.klingonsMarkedForRemovalCount());
  }

  @Test
  public void testScanMarksEntities() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);
    Starbase starbase = quadrant.starbases().get(0);
    Klingon klingon = quadrant.klingons().get(0);
    Star star = quadrant.stars().get(0);
    assertFalse(starbase.isScanned());
    assertFalse(klingon.isScanned());
    assertTrue(star.isScanned());

    // Act
    quadrant.scan();

    // Assert
    assertTrue(starbase.isScanned());
    assertTrue(klingon.isScanned());
    assertTrue(star.isScanned());
  }

  @Test
  public void testRandomEmptySectorMultipleCalls() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 1, 1, 1);

    // Act & Assert
    for (int i = 0; i < 20; i++) {
      XyPair empty = quadrant.getRandomEmptySector();
      assertNotNull(empty);
      assertTrue(empty.getX() >= 0 && empty.getX() < 8);
      assertTrue(empty.getY() >= 0 && empty.getY() < 8);
      assertNull(quadrant.getEntityAt(empty.getX(), empty.getY()));
    }
  }

  @Test
  public void testGetRandomEmptySectorOnFullQuadrant() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 20, 20, 24);

    // Act
    XyPair empty = quadrant.getRandomEmptySector();

    // Assert
    assertNull(empty);
  }

  @Test
  public void testSymbolFormat() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 3, 2, 1);

    // Act
    String symbol = quadrant.symbol();

    // Assert
    assertEquals("132", symbol);
  }

  @Test
  public void testToStringContainsCounts() {
    // Arrange
    Quadrant quadrant = new Quadrant(0, 0, 2, 1, 3);

    // Act
    String description = quadrant.toString();

    // Assert
    assertTrue(description.contains("Stars: 3"));
    assertTrue(description.contains("Enemies: 1"));
    assertTrue(description.contains("Starbases: 2"));
  }

  private String layoutSignature(Quadrant quadrant) {
    List<String> entries = new ArrayList<>();
    for (Starbase starbase : quadrant.starbases()) {
      entries.add("B" + starbase.getX() + "," + starbase.getY());
    }
    for (Klingon klingon : quadrant.klingons()) {
      entries.add("K" + klingon.getX() + "," + klingon.getY());
    }
    for (Star star : quadrant.stars()) {
      entries.add("S" + star.getX() + "," + star.getY());
    }
    Collections.sort(entries);
    return entries.toString();
  }

  private int getEnergy(Klingon klingon) {
    try {
      Field energyField = Klingon.class.getDeclaredField("energy");
      energyField.setAccessible(true);
      Stat stat = (Stat) energyField.get(klingon);
      return stat.get();
    } catch (ReflectiveOperationException e) {
      throw new AssertionError("Unable to access Klingon energy", e);
    }
  }

  private int getEnergy(Starbase starbase) {
    try {
      Field energyField = Starbase.class.getDeclaredField("energy");
      energyField.setAccessible(true);
      Stat stat = (Stat) energyField.get(starbase);
      return stat.get();
    } catch (ReflectiveOperationException e) {
      throw new AssertionError("Unable to access Starbase energy", e);
    }
  }
}
