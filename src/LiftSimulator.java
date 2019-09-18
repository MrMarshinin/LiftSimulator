import House.House;
import Passengers.Passenger;

public class LiftSimulator {

    public static void main(String[] args) throws InterruptedException {
        House house = House.getInstance();
        Passenger[] passengers = new Passenger[25];
        for (int i = 0; i < 25; i++){
            try {
                passengers[i] = new Passenger(i + 70, i / 5 + 2, i/5 + 1);
                passengers[i].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 25; i++){
            System.out.println(i + " joined");
            passengers[i].join();
        }

        System.out.println("Успех!");

        house.stopLifts();
    }
}
