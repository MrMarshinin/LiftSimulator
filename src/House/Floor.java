package House;

import Passengers.Passenger;

import java.util.HashSet;

public class Floor {
    private final int number;
    private HashSet<Passenger> waitingPassengers;
    private boolean needsLift = false;

    Floor(int number) {
        this.number = number;
        this.waitingPassengers = new HashSet<>();
    }

    public synchronized void preparePassengers() {
        for (Passenger passenger : waitingPassengers) {
            passenger.stopWaiting();
        }
    }

    public int getNumber() {
        return number;
    }

    public synchronized void removeWaitingPassenger(Passenger passenger) {
        waitingPassengers.remove(passenger);
        if (waitingPassengers.isEmpty()){
            needsLift = false;
        }
    }

    public synchronized void addWaitingPassenger(Passenger passenger) {
        if (waitingPassengers.isEmpty()) {
            needsLift = true;
        }
        waitingPassengers.add(passenger);
    }

    public synchronized boolean getNeedsLift() {
        return needsLift;
    }

    public synchronized void setNeedsLift(boolean needsLift) {
        this.needsLift = needsLift;
    }

    public boolean isEmpty(){
        return waitingPassengers.isEmpty();
    }
}
