
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for class Passenger.
 *
 * @author Dr. Jody Paul
 * @version 1.0 ($Id: PassengerTest.java 348 2016-05-01 18:40:48Z joshua $)
 */
public class PassengerTest {

    /**
     * Verifies start and destination stations.
     */
    @Test
    public void stationTest() {
        Station station1 = new Station("Pass 1 Start");
        Station station2 = new Station("Pass 1 Destination");

        Passenger pass1 = new Passenger(station1, station2);

        assertEquals("Can't access the start station", station1, pass1.start());
        assertEquals("Can't access the destination", station2, pass1.destination());
    }

    /**
     * Verifies exception for null station passed to first parameter of
     * constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void firstNullStationExceptionTest() {
        new Passenger(null, new Station("S1"));
    }

    /**
     * Verifies exception for null station passed to second parameter of
     * constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void secondNullStationExceptionTest() {
        new Passenger(new Station("S1"), null);
    }
}