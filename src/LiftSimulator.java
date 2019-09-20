import House.House;
import Passengers.Passenger;

public class LiftSimulator {

    static boolean willPassengersBeDelivered(Passenger[] passengers){
        for (Passenger passenger : passengers) {
            try {
                passenger.start();
            } catch (Exception e) {
                return false;
            }
        }

        for (Passenger passenger : passengers) {
            try {
                passenger.join();
            } catch (InterruptedException e) {
                return false;
            }
        }

        House.getInstance().cancelLifts();

        return true;
    }

    public static void main(String[] args) {
    }
}
