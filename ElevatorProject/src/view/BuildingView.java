package view;

import building.BuildingReport;
import controller.BuildingController;
import elevator.ElevatorReport;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * This class is the view for the building.
 */
public class BuildingView extends JFrame {
  private BuildingController controller;
  private JPanel panel;
  private JButton startButton;
  private JButton stopButton;
  private JButton stepButton;
  private JButton sendRequestButton;
  private JTextArea statusArea;


  /**
   * This constructor is used to create a new BuildingView object.
   */
  public BuildingView() {
    super("Building Elevator System");
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /**
   * This method is used to initialize the UI for the building.
   */
  private void initUi() {
    panel = new JPanel(new FlowLayout());

    startButton = new JButton("Start");
    startButton.addActionListener(e -> controller.startSimulation());

    stepButton = new JButton("Step");
    stepButton.addActionListener(e -> controller.processStepAction());

    stopButton = new JButton("Stop");
    stopButton.addActionListener(e -> controller.stopSimulation());

    // Request components
    JComboBox<Integer> startFloorBox = new JComboBox<>();
    JComboBox<Integer> destinationFloorBox = new JComboBox<>();
    int numFloors = controller.getModel().getNumberOfFloors();
    for (int i = 0; i < numFloors; i++) {
      startFloorBox.addItem(i);
    }
    for (int i = 0; i < numFloors; i++) {
      destinationFloorBox.addItem(i);
    }

    sendRequestButton = new JButton("Send Request");
    sendRequestButton.addActionListener(e -> {
      int startFloor = (int) Objects.requireNonNull(startFloorBox.getSelectedItem());
      int destinationFloor = (int) Objects.requireNonNull(destinationFloorBox.getSelectedItem());

      System.out.println("Selected Start floor: " + startFloor);
      System.out.println("Selected Destination floor: " + destinationFloor);

      controller.processRequest(startFloor, destinationFloor);
    });

    statusArea = new JTextArea(20, 50);
    statusArea.setEditable(false);

    panel.add(startButton);
    panel.add(stepButton);
    panel.add(stopButton);
    panel.add(new JLabel("Start Floor:"));
    panel.add(startFloorBox);
    panel.add(new JLabel("End Floor:"));
    panel.add(destinationFloorBox);
    panel.add(sendRequestButton);
    panel.add(new JScrollPane(statusArea));

    add(panel);
  }

  /**
   * This constructor is used to create a new BuildingView object.
   * @param controller The controller for the building.
   */
  public void setController(BuildingController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.controller = controller;
    initUi();
  }

  /**
   * This method is used to create the view for the building.
   */
  public void createView() {
    SwingUtilities.invokeLater(() -> {
      setVisible(true);
    });
  }

  /**
   * This method is used to update the view for the building.
   * @param report The building report to update the view with.
   */
  public void updateView(BuildingReport report) {
    if (report == null) {
      statusArea.setText("No current data available.");
      return;
    }

    StringBuilder status = new StringBuilder("Building Status:\n");
    for (ElevatorReport elevatorReport : report.getElevatorReports()) {
      status.append(elevatorReport.toString()).append("\n");
    }
    statusArea.setText(status.toString());
  }

  /**
   * This method is used to display an error message.
   *
   * @param message The error message to display.
   */
  public void displayError(String message) {
    JOptionPane.showMessageDialog(
        this,
        message,
        "Error",
        JOptionPane.ERROR_MESSAGE);
  }
}

