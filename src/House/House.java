package House;

import House.Lift.Lift;
import House.Lift.LiftController;

public class House {
    public static final int NUMBER_OF_FLOORS = 16;

    private int numberOfLifts;
    private static House instance;
    private final Lift[] lifts;
    private final Floor[] floors = new Floor[NUMBER_OF_FLOORS];
    private LiftController liftController = new LiftController();


    public static House getInstance() {
        //Double Checked Locking Singleton
        House localInstance = instance;
        if (localInstance == null) {
            synchronized (Lift.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new House();
                    for (int i = 0; i < instance.numberOfLifts; i++) {
                        instance.lifts[i].start();
                    }
                }
            }
        }
        return localInstance;
    }

    private House() {
        this.numberOfLifts = 2;
        lifts = new Lift[numberOfLifts];

        for (int i = 0; i < NUMBER_OF_FLOORS; i++) {
            floors[i] = new Floor(i);
        }

        for (int i = 0; i < numberOfLifts; i++) {
            lifts[i] = new Lift(450, 1);
        }

    }

    public Lift getLiftOnCurrentFloor(Floor floor) throws NullPointerException {
        for (int i = 0; i < numberOfLifts; i++) {
            if (lifts[i].getCurrentFloor() == floor) {
                return lifts[i];
            }
        }
        return null;
    }

    public Floor getFloor(int numberOfFloor) {
        return floors[numberOfFloor];
    }

    public LiftController getLiftController() {
        return liftController;
    }

    public void stopLifts() {
        for (Lift lift : lifts){
            lift.stopLift();
        }
    }
}
