package House.Lift;

import House.Floor;
import House.House;
import Passengers.Passenger;

import java.util.ArrayList;

public class Lift extends Thread {
    private final int secondsPerFloor;
    private int maxWeight;
    private Floor currentFloor;
    private ArrayList<Passenger> passengersInside;
    private LiftController liftController;
    private ArrayList<Floor> floorsToStopForPassengersInside;
    private boolean isWorking = true;

    private boolean isAbleToMove() {
        return getTotalWeight() < maxWeight;
    }

    private int getTotalWeight() {
        int totalWeight = 0;
        synchronized (this) {
            for (Passenger passenger : passengersInside) {
                totalWeight += passenger.getWeight();
            }
        }
        return totalWeight;

    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    private synchronized boolean isEmpty() {
        return getTotalWeight() == 0;
    }

    public Lift(int maxWeight, int secondsPerFloor) {
        this.passengersInside = new ArrayList<>();
        this.maxWeight = maxWeight;
        this.secondsPerFloor = secondsPerFloor;
        this.floorsToStopForPassengersInside = new ArrayList<Floor>();
    }

    private void init() {
        this.currentFloor = House.getInstance().getFloor(0);
        this.liftController = House.getInstance().getLiftController();
    }

    @Override
    public void run() {
        init();
        while (isWorking) {
            try {
                arrive(currentFloor);
                move(chooseNextFloorToStop());
            } catch (NoNeedToMoveException e) {
                try {
                    waitForNewPassengers();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        Thread.currentThread().interrupt();
    }

    public synchronized void addPassenger(Passenger passenger) throws OverweightException {
        passengersInside.add(passenger);
        if (!isAbleToMove()) {
            throw new OverweightException();
        }
        System.out.println("Add " + passenger.getWeight());
    }

    private void waitForNewPassengers() throws InterruptedException {
        while (true) {
            try {
                chooseNextFloorToStop();
                break;
            } catch (NoNeedToMoveException e) {
                arrive(currentFloor);
                Thread.sleep(500);
            }
        }
    }

    private synchronized Floor chooseNextFloorToStop() throws NoNeedToMoveException {
        if (isEmpty()) {
            return liftController.getNextFloorToStop();
        } else {
            return floorsToStopForPassengersInside.get(0);
        }
    }

    private void arrive(Floor floor) {
        removePassengers();
        floor.preparePassengers();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!currentFloor.isEmpty()) {
            currentFloor.setNeedsLift(true);
        }
    }

    private void removePassengers() {
        ArrayList<Passenger> passengersToInterrupt = new ArrayList<>();
        for (Passenger value : passengersInside) {
            if (value.getFinishFloor() == currentFloor) {
                passengersToInterrupt.add(value);
            }
        }

        floorsToStopForPassengersInside.remove(currentFloor);

        for (Passenger passenger : passengersToInterrupt) {
            System.out.println("Remove " + passenger.getWeight());
            passengersInside.remove(passenger);
            passenger.interrupt();
        }
    }


    private void move(Floor nextFloor) throws NoNeedToMoveException {
        if (nextFloor == null) {
            throw new NoNeedToMoveException();
        }
        try {
            if ((nextFloor.getNumber() > currentFloor.getNumber())) {
                moveUp(nextFloor);
            } else {
                moveDown(nextFloor);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveUp(Floor nextFloor) throws InterruptedException {
        int i = currentFloor.getNumber();
        for (; i <= nextFloor.getNumber(); i++) {
            setCurrentFloor(House.getInstance().getFloor(i));
            Thread.sleep(secondsPerFloor * 1000);
        }
    }

    private void moveDown(Floor nextFloor) throws InterruptedException {
        int i = currentFloor.getNumber();
        for (; i >= nextFloor.getNumber(); i--) {
            setCurrentFloor(House.getInstance().getFloor(i));

            if ((floorsToStopForPassengersInside.contains(currentFloor)) || currentFloor.getNeedsLift()) {
                this.arrive(currentFloor);
                if (floorsToStopForPassengersInside.isEmpty()) {
                    currentFloor.setNeedsLift(false);
                }
            } else {
                Thread.sleep(secondsPerFloor * 1000);
            }
        }
        arrive(currentFloor);
    }

    private void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public synchronized void addStopFloor(Floor floor) {
        if (floorsToStopForPassengersInside.contains(floor)) {
            return;
        }

        if (currentFloor.getNumber() == floor.getNumber()) {
            return;
        }

        if (floorsToStopForPassengersInside.isEmpty()) {
            floorsToStopForPassengersInside.add(floor);
        } else {
            boolean isNextFloorLowerThenCurrent = (floorsToStopForPassengersInside.get(0).getNumber() < currentFloor.getNumber());
            boolean isFloorToAddLowerThenCurrent = (floor.getNumber() < currentFloor.getNumber());
            addStopFloorInCorrectPlace(floor, isNextFloorLowerThenCurrent, isFloorToAddLowerThenCurrent);
        }
    }

    private synchronized void addStopFloorInCorrectPlace(Floor floor, boolean isNextFloorLowerThenCurrent,
                                                         boolean isFloorToAddLowerThenCurrent) {
        int beginningSize = floorsToStopForPassengersInside.size();

        if (isNextFloorLowerThenCurrent && isFloorToAddLowerThenCurrent) {
            for (int i = 0; i < beginningSize; i++) {
                if (floorsToStopForPassengersInside.get(i).getNumber() < floor.getNumber() ||
                        floorsToStopForPassengersInside.get(i).getNumber() > currentFloor.getNumber()) {
                    floorsToStopForPassengersInside.add(i, floor);
                    break;
                }
            }
        } else if (isNextFloorLowerThenCurrent) {
            for (int i = 0; i < beginningSize; i++) {
                if (floorsToStopForPassengersInside.get(i).getNumber() > floor.getNumber()) {
                    floorsToStopForPassengersInside.add(i, floor);
                    break;
                }
            }
        } else if (isFloorToAddLowerThenCurrent) {
            for (int i = 0; i < beginningSize; i++) {
                if (floorsToStopForPassengersInside.get(i).getNumber() < floor.getNumber()) {
                    floorsToStopForPassengersInside.add(i, floor);
                    break;
                }
            }
        } else {
            for (int i = 0; i < beginningSize; i++) {
                if (floorsToStopForPassengersInside.get(i).getNumber() > floor.getNumber() ||
                        floorsToStopForPassengersInside.get(i).getNumber() < currentFloor.getNumber()) {
                    floorsToStopForPassengersInside.add(i, floor);
                    break;
                }
            }
        }

        if (floorsToStopForPassengersInside.size() == beginningSize) {
            floorsToStopForPassengersInside.add(beginningSize - 1, floor);
        }

    }

    public void stopLift() {
        isWorking = false;
    }
}