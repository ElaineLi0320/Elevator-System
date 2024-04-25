package controller;

import building.Building;
import scanerzus.Request;
import view.BuildingView;

/**
 * Controller for the Building model and BuildingView view.
 */
public class BuildingController {
  private Building model;
  private BuildingView view;

  /**
   * Constructor for the BuildingController.
   * @param model the Building model
   * @param view the BuildingView view
   */
  public BuildingController(Building model, BuildingView view) {
    this.model = model;
    this.view = view;
    this.view.setController(this);
    this.view.createView();
  }

  /**
   * This method is used to process the step action.
   */
  public void processStepAction() {
    model.stepElevatorSystem();
    view.updateView(model.getElevatorSystemStatus());
  }

  /**
    * This method is used to process the start action.
   */
  public void startSimulation() {
    model.startElevatorSystem();
    view.updateView(model.getElevatorSystemStatus());
  }

  /**
   * This method is used to process the stop action.
   */
  public void stopSimulation() {
    model.stopElevatorSystem();
    view.updateView(model.getElevatorSystemStatus());
  }

  /**
   * This method is used to get the model.
   * @return the model.
   */
  public Building getModel() {
    return model;
  }

  /**
   * This method is used to process the request.
   * @param startFloor the start floor
   * @param destinationFloor the destination floor
   */
  public void processRequest(int startFloor, int destinationFloor) {
    Request request = new Request(startFloor, destinationFloor);
    model.addRequest(request);
    view.updateView(model.getElevatorSystemStatus());
  }
}
