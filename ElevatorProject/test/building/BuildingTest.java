package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import building.enums.ElevatorSystemStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;

/**
 * A JUnit test class for the Building class.
 */
public class BuildingTest {
  private Building building;
  private List<Request> requests;

  /**
   * Set up a building with 10 floors and 2 elevators, each with a capacity of 5.
   */
  @Before
  public void setUp() {
    // Create a building with 10 floors, 2 elevators, each with a capacity of 5.
    building = new Building(10, 2, 5);
    // Initialize a sample list of requests
    requests = new ArrayList<>();
    requests.add(new Request(0, 5));
    requests.add(new Request(5, 0));
    requests.add(new Request(3, 8));
  }

  /**
   * Tests the Building constructor.
   */
  @Test
  public void testBuildingConstructor() {
    assertEquals(10, building.getNumberOfFloors());
    assertEquals(2, building.getNumberOfElevators());
    assertEquals(5, building.getElevatorCapacity());
  }

  /**
   * Tests the Building constructor with invalid arguments.
   *
   * @throws IllegalArgumentException if the constructor arguments are invalid
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorWithInvalidArgs() {
    new Building(-1, 20, -1);
  }

  /**
   * Verifies that the total number of floors in the building is correctly reported.
   */
  @Test
  public void testGetNumberOfFloors() {
    assertEquals(
        "The number of floors reported does not match the expected value.",
        10,
        building.getNumberOfFloors()
    );
  }

  /**
   * Tests the accurate retrieval of the total number of elevators in the building.
   */
  @Test
  public void testGetNumberOfElevators() {
    assertEquals(
        "The number of elevators reported does not match the expected value.",
        2,
        building.getNumberOfElevators()
    );
  }

  /**
   * Confirms that the maximum capacity of the elevators in the building is reported correctly.
   */
  @Test
  public void testGetElevatorCapacity() {
    assertEquals(
        "The elevator capacity reported does not match the expected value.",
        5,
        building.getElevatorCapacity()
    );
  }

  /**
   * Tests that an {@link IllegalStateException} is thrown when trying to add a request
   * while the elevator system is out of service.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestWhenSystemOutOfService() {
    building.stopElevatorSystem();
    Request request = new Request(1, 2);
    building.addRequest(request);
  }

  /**
   * Tests adding a request with invalid floor numbers,
   * expecting an {@link IllegalArgumentException}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestWithInvalidFloors() {
    building.startElevatorSystem();
    // Floors outside the valid range
    Request request = new Request(-1, 11);
    building.addRequest(request);
  }

  /**
   * Tests that an {@link IllegalArgumentException} is thrown when adding a null request.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullRequest() {
    building.startElevatorSystem();
    building.addRequest(null);
  }

  /**
   * Tests that adding a request where the start and end floors are the same
   * throws an {@link IllegalArgumentException}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestWithSameStartAndEndFloor() {
    building.startElevatorSystem();
    // Same start and end floor
    Request request = new Request(3, 3);
    building.addRequest(request);
  }

  /**
   * Verifies that a valid request of going up is successfully added to the system.
   */
  @Test
  public void testAddValidRequestGoingUp() {
    building.startElevatorSystem();
    Request request = new Request(1, 5);
    assertTrue(
        "Expected the request to be successfully added",
        building.addRequest(request)
    );
  }

  /**
   * Verifies that a valid request of going down is correctly added to the system.
   */
  @Test
  public void testAddValidRequestGoingDown() {
    building.startElevatorSystem();
    Request request = new Request(5, 2);
    assertTrue(
        "Expected the request to be successfully added",
        building.addRequest(request)
    );
  }

  /**
   * Tests the behavior of adding multiple requests to the system.
   */
  @Test
  public void testUpAndDownRequestOrdering() {
    building.startElevatorSystem();

    Request upRequest1 = new Request(1, 3);
    Request downRequest1 = new Request(5, 2);
    Request upRequest2 = new Request(2, 4);
    Request downRequest2 = new Request(6, 1);

    building.addRequest(upRequest1);
    building.addRequest(downRequest1);
    building.addRequest(upRequest2);
    building.addRequest(downRequest2);

    List<Request> upRequests = building.getElevatorSystemStatus().getUpRequests();
    List<Request> downRequests = building.getElevatorSystemStatus().getDownRequests();

    // Check if the up requests are stored in the order they were received.
    assertEquals(upRequests.get(0), upRequest1);
    assertEquals(upRequests.get(1), upRequest2);

    // Check if the down requests are stored in the order they were received.
    assertEquals(downRequests.get(0), downRequest1);
    assertEquals(downRequests.get(1), downRequest2);
  }

  /**
   * Tests the behavior of starting the elevator system when it is already running.
   * The method should return false, indicating no change was made to the system state.
   */
  @Test
  public void testStartSystemWhenAlreadyRunning() {
    assertTrue(
        "First start should be successful",
        building.startElevatorSystem()
    );
    assertFalse(
        "Starting an already running system should return false",
        building.startElevatorSystem()
    );
  }

  /**
   * Tests starting the elevator system from a non-running state.
   * Expects the method to return true, indicating the system was successfully started.
   */
  @Test
  public void testStartSystemFromNonRunningState() {
    assertTrue(
        "The system should successfully start from a non-running state.",
        building.startElevatorSystem()
    );
  }

  /**
   * Tests stopping the elevator system when it is already stopped.
   */
  @Test(expected = IllegalStateException.class)
  public void testStopSystemWhenAlreadyStoppingOrOutOfService() {
    building.stopElevatorSystem();
    building.stopElevatorSystem();
  }

  /**
   * Tests stopping the elevator system from a running state.
   */
  @Test
  public void testStopSystemFromRunningState() {
    building.startElevatorSystem();
    try {
      building.stopElevatorSystem();
      assertTrue(
          "The system should transition to OUT_OF_SERVICE without throwing exceptions.",
          true);
    } catch (IllegalStateException e) {
      fail("Stopping a running system should not throw IllegalStateException.");
    }
  }

  /**
   * Tests the stepping of the elevator system with active requests.
   */
  @Test
  public void testStepElevatorSystemWithRequests() {
    building.startElevatorSystem();
    assertTrue(building.addRequest(requests.get(0)));
    building.stepElevatorSystem();
  }

  /**
   * Validates that the initial status of the building's elevator system is outOfService.
   */
  @Test
  public void testInitialBuildingStatus() {
    BuildingReport report = building.getElevatorSystemStatus();
    assertEquals("Initial status should be outOfService",
        ElevatorSystemStatus.outOfService, report.getSystemStatus());
  }

  /**
   * Tests the status of the elevator system after starting it.
   */
  @Test
  public void testBuildingStatusAfterStarting() {
    building.startElevatorSystem();
    BuildingReport report = building.getElevatorSystemStatus();
    assertEquals("Status after starting should be running",
        ElevatorSystemStatus.running, report.getSystemStatus());
  }

  /**
   * Tests the status of the elevator system after stopping it.
   */
  @Test
  public void testBuildingStatusAfterStopping() {
    building.startElevatorSystem();
    building.stopElevatorSystem();
    BuildingReport report = building.getElevatorSystemStatus();
    assertEquals("Status after stopping should be outOfService",
        ElevatorSystemStatus.outOfService, report.getSystemStatus());
  }

  /**
   * Verifies that the building's elevator system can be taken out of service,
   * following a stop command, regardless of the door state.
   */
  @Test
  public void testTakeOutOfServiceWithOpenDoor() {
    building.startElevatorSystem();
    building.stopElevatorSystem();

    for (int i = 0; i < 10; i++) {
      building.stepElevatorSystem();
    }

    // Verify that the building is taken out of service despite an open door.
    BuildingReport report = building.getElevatorSystemStatus();
    assertEquals("Building should be out of service",
        ElevatorSystemStatus.outOfService, report.getSystemStatus());
  }
}
