package sttrswing.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityTest {

  private static class TestEntity extends Entity {

    TestEntity(int x, int y) {
      super(x, y);
    }
  }

  @Test
  public void testConstructorInitialization() {
    TestEntity entity = new TestEntity(3, 5);

    assertEquals(3, entity.getX());
    assertEquals(5, entity.getY());
  }

  @Test
  public void testGetX() {
    TestEntity entity = new TestEntity(10, 7);

    assertEquals(10, entity.getX());
  }

  @Test
  public void testGetY() {
    TestEntity entity = new TestEntity(10, 7);

    assertEquals(7, entity.getY());
  }

  @Test
  public void testSetX() {
    TestEntity entity = new TestEntity(0, 0);

    entity.setX(12);

    assertEquals(12, entity.getX());
  }

  @Test
  public void testSetXAtBoundaries() {
    TestEntity entity = new TestEntity(3, 3);

    entity.setX(0);
    assertEquals(0, entity.getX());

    entity.setX(7);
    assertEquals(7, entity.getX());
  }

  @Test
  public void testSetXAllowsOutOfBounds() {
    TestEntity entity = new TestEntity(3, 3);

    entity.setX(-1);
    assertEquals(-1, entity.getX());

    entity.setX(8);
    assertEquals(8, entity.getX());
  }

  @Test
  public void testSetY() {
    TestEntity entity = new TestEntity(0, 0);

    entity.setY(-4);

    assertEquals(-4, entity.getY());
  }

  @Test
  public void testSetYAtBoundaries() {
    TestEntity entity = new TestEntity(3, 3);

    entity.setY(0);
    assertEquals(0, entity.getY());

    entity.setY(7);
    assertEquals(7, entity.getY());
  }

  @Test
  public void testSetYAllowsOutOfBounds() {
    TestEntity entity = new TestEntity(3, 3);

    entity.setY(-1);
    assertEquals(-1, entity.getY());

    entity.setY(8);
    assertEquals(8, entity.getY());
  }

  @Test
  public void testAdjustPositionPositiveDelta() {
    TestEntity entity = new TestEntity(2, 2);

    entity.adjustPosition(3, 4);

    assertEquals(5, entity.getX());
    assertEquals(6, entity.getY());
  }

  @Test
  public void testAdjustPositionNegativeDelta() {
    TestEntity entity = new TestEntity(5, 5);

    entity.adjustPosition(-2, -3);

    assertEquals(3, entity.getX());
    assertEquals(2, entity.getY());
  }

  @Test
  public void testAdjustPositionAcrossBoundaries() {
    TestEntity entity = new TestEntity(0, 0);

    entity.adjustPosition(-1, -1);
    assertEquals(-1, entity.getX());
    assertEquals(-1, entity.getY());

    entity.adjustPosition(9, 10);
    assertEquals(8, entity.getX());
    assertEquals(9, entity.getY());
  }

  @Test
  public void testAdjustPositionZeroDelta() {
    TestEntity entity = new TestEntity(8, -1);

    entity.adjustPosition(0, 0);

    assertEquals(8, entity.getX());
    assertEquals(-1, entity.getY());
  }

  @Test
  public void testIsScannedInitially() {
    TestEntity entity = new TestEntity(0, 0);

    assertFalse(entity.isScanned());
  }

  @Test
  public void testScanMarksEntity() {
    TestEntity entity = new TestEntity(0, 0);

    entity.scan();

    assertTrue(entity.isScanned());
  }

  @Test
  public void testSymbolBeforeAndAfterScan() {
    TestEntity entity = new TestEntity(0, 0);
    entity.setSymbol("ABC");

    String symbolBeforeScan = entity.symbol();
    entity.scan();
    String symbolAfterScan = entity.symbol();

    assertEquals(" ? ", symbolBeforeScan);
    assertEquals("ABC", symbolAfterScan);
  }

  @Test
  public void testIsMarkedForRemovalInitially() {
    TestEntity entity = new TestEntity(0, 0);

    assertFalse(entity.isMarkedForRemoval());
  }

  @Test
  public void testRemoveMarksEntityForRemoval() {
    TestEntity entity = new TestEntity(0, 0);

    entity.remove();

    assertTrue(entity.isMarkedForRemoval());
  }

  @Test
  public void testRemoveIsIdempotent() {
    TestEntity entity = new TestEntity(0, 0);

    entity.remove();
    entity.remove();

    assertTrue(entity.isMarkedForRemoval());
  }

  @Test
  public void testToStringReflectsState() {
    TestEntity entity = new TestEntity(1, 2);

    String initialString = entity.toString();
    entity.scan();
    entity.remove();
    String updatedString = entity.toString();

    assertTrue(initialString.contains("x:1"));
    assertTrue(initialString.contains("y:2"));
    assertTrue(initialString.contains("scanned:false"));
    assertTrue(initialString.contains("markedForRemovalfalse"));

    assertTrue(updatedString.contains("scanned:true"));
    assertTrue(updatedString.contains("markedForRemovaltrue"));
  }
}
