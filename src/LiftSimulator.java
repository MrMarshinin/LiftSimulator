import House.House;
import Passengers.Passenger;

public class LiftSimulator {

    public static boolean deliverPassengers(Passenger[] passengers){
        for (int i = 0; i < passengers.length; i++){
            try {
                passengers[i].start();
            } catch (Exception e) {
                return false;
            }
        }

        for (int i = 0; i < passengers.length; i++){
            try {
                passengers[i].join();
            } catch (InterruptedException e) {
                return false;
            }
        }

        House.getInstance().cancelLifts();

        return true;
    }

    public static void main(String[] args) throws InterruptedException {
    }
}
