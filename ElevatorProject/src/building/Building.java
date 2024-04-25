package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.List;
import scanerzus.Request;


/**
 * This class represents a building.
 */
public class Building implements BuildingInterface {

  private final ElevatorInterface[] elevators;
  private ElevatorSystemStatus elevatorsStatus;
  private final int numberOfFloors;
  private final int elevatorCapacity;
  private final int numberOfElevators;
  private final List<Request> upRequests = new ArrayList();
  private final List<Request> downRequests = new ArrayList();

  /**
   * The constructor for the building.
   *
   * @param numberOfFloors the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity the capacity of the elevators in the building.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity)
      throws IllegalArgumentException {
    if (numberOfFloors < 2) {
      throw new IllegalArgumentException("Number of floors must be larger than 2.");
    }
    if (numberOfElevators < 1 || numberOfElevators > 10) {
      throw new IllegalArgumentException(
          "numberOfElevators must be greater than 0 and smaller or equal than 10."
      );
    }
    if (elevatorCapacity < 1) {
      throw new IllegalArgumentException("maxOccupancy must be greater than 1.");
    }

    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevators = new Elevator[numberOfElevators];

    for (int i = 0; i < numberOfElevators; i++) {
      this.elevators[i] = new Elevator(numberOfFloors, this.elevatorCapacity);
    }
    // Initialize the status of the elevator to outOfService
    this.elevatorsStatus = ElevatorSystemStatus.outOfService;
  }

  /**
   * Retrieves the total number of floors in the building.
   *
   * @return The number of floors in the building.
   */
  public int getNumberOfFloors() {
    return this.numberOfFloors;
  }

  /**
   * Retrieves the total number of elevators in the building.
   *
   * @return The number of elevators available in the building.
   */
  public int getNumberOfElevators() {
    return this.numberOfElevators;
  }

  /**
   * Retrieves the maximum capacity of the elevators in the building.
   * Assumes all elevators have the same capacity.
   *
   * @return The maximum number of individuals that can be in each elevator at one time.
   */
  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  /**
   * Adds a request to the elevator system.
   *
   * @param request The request to be added.
   * @return true if the request was successfully added; false if not.
   * @throws IllegalStateException if the system is not currently accepting requests due to
   *         being out of service or stopping.
   * @throws IllegalArgumentException if the request is null, the start and end floors are the
   *         same, or if the floors are outside the valid range of the building.
   */
  @Override
  public boolean addRequest(Request request) {
    if (this.elevatorsStatus == ElevatorSystemStatus.outOfService
        || this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator system not accepting requests.");
    }

    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }

    int startFloor = request.getStartFloor();
    int endFloor = getEndFloor(request, startFloor);

    if (startFloor < endFloor) {
      this.upRequests.add(request);
    } else {
      this.downRequests.add(request);
    }

    // Return true if the request was successfully added.
    return true;
  }

  /**
   * Calculates and validates the end floor for a given request and start floor.
   *
   * This method retrieves the end floor from a request and validates both the start and end floors.
   * The floors must be within the valid range of the building and cannot be the same.
   *
   * @param request The request object containing the desired end floor.
   * @param startFloor The floor from which the request is made.
   * @return The validated end floor number from the request.
   * @throws IllegalArgumentException if the start floor is not within the building's floor range,
   *                                  if the end floor is not within the building's floor range,
   *                                  or if the start floor and end floor are the same.
   */
  private int getEndFloor(Request request, int startFloor) {
    int endFloor = request.getEndFloor();

    if (startFloor < 0 || startFloor >= this.numberOfFloors) {
      throw new IllegalArgumentException(
          "Start floor must be between 0 and " + (this.numberOfFloors - 1)
      );
    }

    if (endFloor < 0 || endFloor >= this.numberOfFloors) {
      throw new IllegalArgumentException(
          "End floor must be between 0 and " + (this.numberOfFloors - 1)
      );
    }

    if (startFloor == endFloor) {
      throw new IllegalArgumentException(
          "Start floor and end floor cannot be the same"
      );
    }
    return endFloor;
  }

  /**
   * Starts all elevators in the system unless the system is already running or is in the process
   * of stopping. If the system is in the process of stopping, an exception is thrown.
   *
   * @return true if the system was successfully started from a non-running state, false if the
   *         system was already running and no action was taken.
   * @throws IllegalStateException if the system is in the process of stopping.
   */
  @Override
  public boolean startElevatorSystem() {
    if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException(
          "The elevator system cannot be started while it is stopping."
      );
    }

    // If the system is already running, we do nothing and return false.
    if (this.elevatorsStatus == ElevatorSystemStatus.running) {
      return false;
    }

    // Start the elevator system.
    for (ElevatorInterface elevator : this.elevators) {
      elevator.start();
    }

    this.elevatorsStatus = ElevatorSystemStatus.running;
    // Indicates success, the system was not running and has now started.
    return true;
  }

  /**
   * Initiates the stop process for all elevators, transitioning the system to OUT_OF_SERVICE.
   *
   * @throws IllegalStateException if the method is called when the system is already stopping
   *         or is out of service, indicating that stopping the system is not allowed in its
   *         current state.
   */
  @Override
  public void stopElevatorSystem() {
    if (this.elevatorsStatus == ElevatorSystemStatus.stopping
        || this.elevatorsStatus == ElevatorSystemStatus.outOfService) {
      throw new IllegalStateException("System is already stopping or out of service.");
    }

    for (ElevatorInterface elevator : this.elevators) {
      elevator.takeOutOfService();
    }

    this.elevatorsStatus = ElevatorSystemStatus.outOfService;
  }

  /**
   * Retrieves the current status of the elevator system.
   *
   * @return A BuildingReport object that contains all the status of the building.
   */
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.elevators.length];

    for (int i = 0; i < this.elevators.length; ++i) {
      elevatorReports[i] = this.elevators[i].getElevatorStatus();
    }

    BuildingReport report = new BuildingReport(
        this.numberOfFloors,
        this.numberOfElevators,
        this.elevatorCapacity,
        elevatorReports,
        this.upRequests,
        this.downRequests,
        this.elevatorsStatus);
    return report;
  }

  /**
   * Steps the elevator system through its operations based on the current status.
   */
  public void stepElevatorSystem() {
    // Only proceed if the elevator is not out of service
    if (this.elevatorsStatus != ElevatorSystemStatus.outOfService) {
      // Pass requests unless the system is stopping
      if (this.elevatorsStatus != ElevatorSystemStatus.stopping) {
        this.distributeRequests();
      }

      // Step each elevator in the system
      for (ElevatorInterface elevator : this.elevators) {
        elevator.step();
      }

      // If the system is stopping, check if all elevators are on the ground floor
      if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
        boolean allElevatorsOnGroundFloor = true;
        for (ElevatorInterface elevator : this.elevators) {
          if (elevator.getCurrentFloor() != 0) {
            allElevatorsOnGroundFloor = false;
            break;
          }
        }

        // Set the system's status to out of service if all elevators are on the ground floor
        if (allElevatorsOnGroundFloor) {
          this.elevatorsStatus = ElevatorSystemStatus.outOfService;
        }
      }
    }
  }

  /**
   * Helper function: Retrieves the requests for an elevator based on its current position.
   */
  private void distributeRequests() {
    // Proceed only if there are requests to distribute
    if (!this.upRequests.isEmpty() || !this.downRequests.isEmpty()) {
      for (ElevatorInterface elevator : this.elevators) {
        // Check if the elevator is accepting requests
        if (elevator.isTakingRequests()) {
          List<Request> requestsForElevator;
          // Assign up requests to the elevator if it's on the ground floor
          if (elevator.getCurrentFloor() == 0) {
            requestsForElevator = this.sendRequests(this.upRequests);
            elevator.processRequests(requestsForElevator);
          } else if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
            requestsForElevator = this.sendRequests(this.downRequests);
            elevator.processRequests(requestsForElevator);
          }
        }
      }
    }
  }

  /**
   * Sends a specific number of requests from the given list, based on the elevator's capacity.
   *
   * @param requests The list of elevator requests.
   * @return A list of requests that have been selected for an elevator to process.
   */
  private List<Request> sendRequests(List<Request> requests) {
    List<Request> selectedRequests = new ArrayList<>();

    // Continue selecting requests until reaching the elevator's capacity or no requests left
    while (!requests.isEmpty() && selectedRequests.size() < this.elevatorCapacity) {
      // Retrieve and remove the first request from the list of requests
      Request requestToProcess = requests.remove(0);
      // Add the removed request to the list of selected requests
      selectedRequests.add(requestToProcess);
    }
    return selectedRequests;
  }
}
