package House.Lift;

import House.Floor;
import House.House;

public class LiftController {

    synchronized Floor getNextFloorToStop() throws NoNeedToMoveException {
        House house = House.getInstance();
        if (!house.getFloor(0).getNeedsLift()) {
            for (int i = House.NUMBER_OF_FLOORS - 1; i > 0; i--) {
                if (house.getFloor(i).getNeedsLift()) {
                    house.getFloor(i).setNeedsLift(false);
                    return house.getFloor(i);
                }
            }
            throw new NoNeedToMoveException();
        } else {
            house.getFloor(0).setNeedsLift(false);
            return house.getFloor(0);
        }
    }
}
