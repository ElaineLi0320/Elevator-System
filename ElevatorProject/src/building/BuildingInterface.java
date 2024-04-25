package building;

import building.enums.ElevatorSystemStatus;
import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {
  /**
   * Retrieves the total number of floors in the building.
   *
   * @return The number of floors in the building.
   */
  int getNumberOfFloors();

  /**
   * Retrieves the total number of elevators in the building.
   *
   * @return The number of elevators available in the building.
   */
  int getNumberOfElevators();

  /**
   * Retrieves the maximum capacity of the elevators in the building.
   * Assumes all elevators have the same capacity.
   *
   * @return The maximum number of individuals that can be in each elevator at one time.
   */
  int getElevatorCapacity();

  /**
   * Adds a request to the elevator system. The system then decides how to
   * allocate this request to an elevator.
   *
   * @param request The elevator request to add.
   * @return true if the request was successfully added; false otherwise.
   * @throws IllegalStateException if the system is currently unable to accept requests.
   */
  boolean addRequest(Request request);

  /**
   * Starts the elevator system, making it ready to accept and process new requests.
   * If the system is already running, this may have no effect.
   *
   * @return true if the system was successfully started; false otherwise.
   * @throws IllegalStateException if the system is in a state that prevents it from being started.
   */
  boolean startElevatorSystem();

  /**
   * Initiates the shutdown of the elevator system. This involves directing all elevators
   * to return to a default position (e.g., the ground floor) and rejecting new requests.
   */
  void stopElevatorSystem();

  /**
   * Retrieves the current status of the elevator system.
   *
   * @return A BuildingReport object that contains all the status of the building.
   */
  BuildingReport getElevatorSystemStatus();

  /**
   * Steps the elevator system through its operations based on the current status.
   */
  void stepElevatorSystem();
}
