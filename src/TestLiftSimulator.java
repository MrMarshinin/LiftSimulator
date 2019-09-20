import Passengers.Passenger;
import org.junit.Assert;
import org.junit.Test;

public class TestLiftSimulator {
    private static Passenger[] passengers1 = new Passenger[10];
    private static Passenger[] passengers2 = new Passenger[10];
    private static Passenger[] passengers3 = new Passenger[20];
    private static Passenger[] passengers4 = new Passenger[20];
    private static Passenger[] passengers5 = new Passenger[30];
    private static Passenger[] passengers6 = new Passenger[100];

    private static void init() throws Exception {
        for (int i = 0; i < passengers1.length; i++) {
            passengers1[i] = new Passenger(70, i + 3, 1);
        }

        for (int i = 0; i < passengers2.length; i++) {
            passengers2[i] = new Passenger(i * 2 + 70, 15, i + 1);
        }

        for (int i = 0; i < passengers3.length; i++) {
            passengers3[i] = new Passenger(i + 70, i / 4 + 1, i / 3 + 3);
        }

        for (int i = 0; i < passengers4.length; i++) {
            passengers4[i] = new Passenger(i * 5 + 50, 16, 15 - i / 2);
        }

        for (int i = 0; i < passengers5.length / 2; i++) {
            passengers5[i] = new Passenger(i * 5 + 20, 1, 16 - i / 4);
        }

        for (int i = passengers5.length / 2; i < passengers5.length; i++) {
            passengers5[i] = new Passenger(i * 5 + 20, 16, 15 - i / 4);
        }

        for (int i = 0; i < passengers6.length / 2; i++) {
            passengers6[i] = new Passenger(i * 2 + 70, 1, 16 - i / 4);
        }

        for (int i = passengers6.length / 2; i < passengers6.length; i++) {
            passengers6[i] = new Passenger(i * 2 + 60, 16 - (i - passengers6.length / 2) / 4, 1);
        }
    }

    @Test
    public void testDeliverPassengers() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(LiftSimulator.willPassengersBeDelivered(passengers1));
        Assert.assertTrue(LiftSimulator.willPassengersBeDelivered(passengers2));
        Assert.assertTrue(LiftSimulator.willPassengersBeDelivered(passengers3));
        Assert.assertTrue(LiftSimulator.willPassengersBeDelivered(passengers4));
        Assert.assertTrue(LiftSimulator.willPassengersBeDelivered(passengers5));
        Assert.assertTrue(LiftSimulator.willPassengersBeDelivered(passengers6));
    }
}
