# Building Elevator System

## About/Overview
This project implements a simulated elevator control system for 
a building. The software manages multiple elevators, processing 
requests from users to travel between floors.

## List of Features

- **Multiple Elevators**: Support controlling multiple elevators.
- **Floor Request Handling**: Process floor requests and direct elevators to serve these requests.
- **Elevator Status Updates**: Real-time status reporting of each elevator in terms of its current floor, direction, and operational status.
- **Start/Stop Simulation**: Start and stop the elevator simulation.
- **Interactive UI**: A user interface that allows interaction with the elevator system through buttons and displays the current status of all elevators.

## How To Run
### Prerequisites
- Java Runtime Environment (JRE) installed on your machine.
- Java Development Kit (JDK) for compiling the source code.

### Running the JAR File
1. Open a terminal or command prompt.
2. Navigate to the directory containing the `ElevatorSystem.jar` file.
3. Run the following command:

   ```bash
   java -jar ElevatorSystem.jar

## How to Use the Program
- Start Simulation: Click the 'Start' button to initiate the elevator system.
- Send Request: Select the start and destination floors and click 
'Send Request' to simulate a user requesting an elevator.
- Step Simulation: Click 'Step' to move the simulation forward by one step in time.
- Stop Simulation: Click the 'Stop' button to halt the elevator system.

## Design/Model Changes
Version changes:
- **Version 1.0**:Initial design with basic elevator movement and request handling.
- **Version 2.0**:Added support for multiple elevators and real-time status updates.
- **Version 3.0**:Improved UI and added simulation controls.

## Assumptions
- The building has a finite number of floors.
- The elevators can only move vertically.
- All elevators have the same capacity and speed.
- Elevators respond to requests based on their current direction and nearest request priorities.

## Limitations
- The system does not optimize the path in real-time based on dynamically changing requests.
- The UI is a simple representation and does not reflect a real-world elevator interface.

## Citations
No external resources were directly used to aid in the solution of this project.


