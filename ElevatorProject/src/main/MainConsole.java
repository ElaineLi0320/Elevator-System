package main;

import building.Building;
import controller.BuildingController;
import view.BuildingView;

/**
 * The driver for the elevator system.
 * This class will create the elevator system and run it.
 * this is for testing the elevator system.
 * <p>
 * It provides a user interface to the elevator system.
 */
public class MainConsole {

  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Building building = new Building(11, 8, 3);
    BuildingView view = new BuildingView();
    BuildingController controller = new BuildingController(building, view);
    view.setController(controller);
    view.setVisible(true);
  }
}