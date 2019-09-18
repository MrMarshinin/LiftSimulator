package Passengers;

import House.*;
import House.Lift.*;

public class Passenger extends Thread {
    private final int weight;
    private final Floor finishFloor;
    private final Floor startFloor;
    private boolean isWaiting;

    public Passenger(int weight, int finishFloor, int startFloor) throws Exception {
        if (finishFloor < 1 || finishFloor > House.NUMBER_OF_FLOORS) {
            throw new NoSuchFloorException();
        }

        if (weight < 0) {
            throw new NegativeWeightException();
        }

        if (startFloor < 1 || startFloor > House.NUMBER_OF_FLOORS) {
            throw new NoSuchFloorException();
        }

        if (startFloor == finishFloor) {
            throw new LiftIsNotNeededException();
        }

        House house = House.getInstance();

        this.weight = weight;
        this.finishFloor = house.getFloor(finishFloor - 1);
        this.startFloor = house.getFloor(startFloor - 1);
        this.isWaiting = true;
    }

    private void callLift() {
        startFloor.addWaitingPassenger(this);
        try {
            waitLift();
        } catch (InterruptedException e) {
        }
    }

    private void waitLift() throws InterruptedException {
        while (isWaiting) {
            Thread.sleep(50);
        }
        isWaiting = true;
    }

    private synchronized void enterLift() {
        Lift lift = null;
        try {
            lift = House.getInstance().getLiftOnCurrentFloor(startFloor);
        } catch (NoLiftOnCurrentFloorException e) {
            e.printStackTrace();
        }

        try {
            lift.addPassenger(this);
        } catch (Exception e) {
            callLift();
            enterLift();
        }
        lift.addStopFloor(finishFloor);
        startFloor.removeWaitingPassenger(this);
    }

    @Override
    public void run() {
        callLift();
        enterLift();
        while (!isInterrupted()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public int getWeight() {
        return weight;
    }

    public void stopWaiting() {
        isWaiting = false;
    }

    public Floor getFinishFloor() {
        return finishFloor;
    }
}
