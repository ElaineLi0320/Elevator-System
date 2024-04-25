package building;

import scanerzus.Request;

/**
 * Simulates the operation of an elevator system within a building.
 */
public class Driver {

  /**
   * Executes a simple simulation of an elevator system.
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      // Start the system
      Building myBuilding = new Building(10, 2, 5);
      System.out.println("Starting the elevator system...");
      myBuilding.startElevatorSystem();
      System.out.println("Started the elevator system.");

      // Add requests
      System.out.println("Adding requests...");
      myBuilding.addRequest(new Request(1, 4));
      myBuilding.addRequest(new Request(6, 2));
      System.out.println("Added requests.");

      // Step through the system
      System.out.println("Stepping through the system.");
      for (int i = 0; i < 5; i++) {
        myBuilding.stepElevatorSystem();
      }

      // Stop the system
      System.out.println("Stopping the elevator system...");
      myBuilding.stopElevatorSystem();
      System.out.println("Stopped the elevator system.");

    } catch (IllegalArgumentException | IllegalStateException e) {
      System.err.println("Error occurred: " + e.getMessage());
    }
  }
}
