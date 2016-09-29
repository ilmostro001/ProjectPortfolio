import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
/**
 * Tests for class Train.
 *
 * @author  Dr. Jody Paul
 * @author  Stephen Bapple
 * @version 2.0 ($Id: TrainTest.java 344 2016-05-01 18:00:37Z joshua $)
 */
public class TrainTest {

    /** Test capacity. */
    private static final int CAP = 100;

    /** Test train. */
    private Train trainH;

    /** Sample start station. */
    private Station start;

    /** Sample destination station. */
    private Station destination;

    /** Alternate sample start station. */
    private Station altStart;

    /** Alternate sample destination station. */
    private Station altDestination;

    /** A list of passengers. */
    private List<Passenger> plistA;

    /** An alternate list of passengers. */
    private List<Passenger> plistB;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        this.start = new Station("The Start");
        this.destination = new Station("The Destination");
        this.altStart = new Station("Alt Start");
        this.altDestination = new Station("Alt Destination");
        this.trainH = new Train(CAP);

        this.plistA = new ArrayList<Passenger>();
        this.plistB = new ArrayList<Passenger>();
        for (int i = 0; i < 10; i++) {
            plistA.add(new Passenger(this.altStart, this.altDestination));
            plistB.add(new Passenger(this.start, this.destination));
        }

    }

    /**
     * Fill the given train to its maximum capacity.
     * @param t the train
     */
    private void fillTrain(final Train t) {
        for (int i = t.numberOfPassengersOnBoard(); i < t.maximumCapacity(); i++) {
            t.board(new Passenger(start, destination));
        }
    }

    /**
     * Board passengers up to a given limit.
     * Does nothing if limit is higher than capacity.
     * @param t the train
     * @param limit the number of passengers to board up to
     */
    private void boardTrainToLimit(final Train t, final int limit) {
        for (int i = t.numberOfPassengersOnBoard();
                i < limit && i < t.maximumCapacity(); i++) {
            t.board(new Passenger(start, destination));
        }
    }

    /**
     * Test that a train contains the proper capacity.
     */
    @Test
    public void capacityTest() {
        Train train0 = new Train(0);
        assertEquals(0, train0.maximumCapacity());
        assertEquals(CAP, this.trainH.maximumCapacity());
    }

    /** Station "A" */
    private static final Station SA = new Station("A");
    /** Station "B" */
    private static final Station SB = new Station("B");

    /**
     * Test empty train has zero passengers on board.
     */
    @Test
    public void testEmptyTrain() {
        Train train4 = new Train(4);
        assertEquals(0, train4.numberOfPassengersOnBoard());
        assertEquals(0, train4.passengers().size());
    }

    /**
     * Test adding a single passenger to a train.
     */
    @Test
    public void testAddingOnePassenger() {
        Train train = new Train(10);
        assertTrue(train.board(new Passenger(SA, SB)));
        assertEquals(1, train.numberOfPassengersOnBoard());
    }

    /**
     * Test filling a train to its maximum capacity.
     */
    @Test
    public void testFillingTrainToCapacity() {
        Train train20 = new Train(20);
        for (int i = train20.numberOfPassengersOnBoard();
             i < train20.maximumCapacity();
             i++) {
               assertTrue(train20.board(new Passenger(SA, SB)));
               assertEquals(i + 1, train20.numberOfPassengersOnBoard());
        }
        assertEquals(train20.numberOfPassengersOnBoard(),
                     train20.maximumCapacity());
    }

    /**
     * Test boarding a train multiple times, but not fully.
     */
    @Test
    public void multipleBoardTest() {
        for (int i = 0; i < this.trainH.maximumCapacity() - 1; i++) {
            assertTrue(this.trainH.board(new Passenger(SA, SB)));
        }

        assertEquals(this.trainH.maximumCapacity() - 1,
                     this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test that attempting to board a duplicate passenger will not board them.
     * Note: start and destination station do not matter for equality,
     * only primitive "objA == objB" equality counts here.
     */
    @Test
    public void repeatBoardTest() {
        boardTrainToLimit(this.trainH, 3);
        Passenger repeater = new Passenger(SA, SB);

        assertTrue(this.trainH.board(repeater));  // Can board.
        assertFalse(this.trainH.board(repeater)); // Can't board again.
        assertEquals(4, this.trainH.numberOfPassengersOnBoard()); // +1 only.
    }

    /**
     * Test that a train cannot be over boarded.
     */
    @Test
    public void overBoardTest() {
        // Use a train with a capacity of 5.
        Train train5 = new Train(5);
        fillTrain(train5);
        assertEquals(train5.maximumCapacity(), train5.numberOfPassengersOnBoard());
        assertFalse(train5.board(new Passenger(SA, SB)));
        assertEquals(train5.maximumCapacity(), train5.numberOfPassengersOnBoard());

        // Duplicate logic for this.trainH.
        fillTrain(this.trainH);
        assertEquals(CAP, this.trainH.numberOfPassengersOnBoard());
        assertFalse(this.trainH.board(new Passenger(SA, SB)));
        assertEquals(CAP, this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test that for the collection of passengers returned
     * the size of the collection is equal to the number of passengers.
     * This test is simplistic, but it simplifies the logic of many other tests.
     */
    @Test
    public void collectionSizeTest() {
        boardTrainToLimit(this.trainH, 50);
        assertEquals(this.trainH.passengers().size(),
                     this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test boarding an entire collection of passengers.
     */
    @Test
    public void boardAllTest() {
        List<Passenger> plist = new ArrayList<Passenger>();
        for (int i = 0; i < 10; i++) {
            plist.add(new Passenger(this.start, this.destination));
            plist.add(new Passenger(this.altStart, this.altDestination));
        }

        assertTrue(this.trainH.boardAll(plist));
        assertEquals(20, this.trainH.numberOfPassengersOnBoard());
        assertTrue(this.trainH.passengers().containsAll(plist));
    }

    /**
     * Test that a boarding a collection of passengers
     * that would overboard a train boards none of them.
     */
    @Test
    public void boardAllOverboardTest() {
        List<Passenger> plist = new ArrayList<Passenger>();
        for (int i = 0; i < 10; i++) {
            plist.add(new Passenger(SA, SB));
        }

        Train train15 = new Train(15);
        boardTrainToLimit(train15, 10);

        assertFalse(train15.boardAll(plist));
        assertEquals(10, train15.numberOfPassengersOnBoard());
    }

    /**
     * Test that a train returns the same collection of passengers that was
     * boarded.
     */
    @Test
    public void passengersTest() {
        Passenger pass1 = new Passenger(start, destination);
        this.trainH.board(pass1);
        assertTrue(this.trainH.passengers().contains(pass1));

        // start and destination are the default stations given for passengers
        // in this utility method.
        boardTrainToLimit(this.trainH, 4);
        for (Passenger p : this.trainH.passengers()) {
            assertEquals(start, p.start());
            assertEquals(destination, p.destination());
        }
    }

    /**
     * Test disembarking a <tt>null</tt> passenger.
     */
    @Test
    public void disembarkNullTest() {
        this.trainH.board(new Passenger(start, destination));
        assertFalse(this.trainH.disembark(null));
        assertEquals(1, this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test disembarking a <tt>null</tt> collection of passengers.
     */
    @Test
    public void disembarkAllNullTest() {
        this.trainH.board(new Passenger(start, destination));
        assertFalse(this.trainH.disembarkAll(null));
        assertEquals(1, this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test disembarking a passenger not on the train.
     */
    @Test
    public void disembarkNonBoardedPassengerTest() {
        Passenger boarded = new Passenger(start, destination);
        Passenger notBoarded = new Passenger(altStart, destination);

        this.trainH.board(boarded);
        assertFalse(this.trainH.disembark(notBoarded));
        assertEquals(1, this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test disembarking a list from an empty train.
     */
    @Test
    public void disembarkAllEmptyTrainTest() {
        assertFalse(this.trainH.disembarkAll(plistA));
        assertEquals(0, this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test disembarking a collection of passengers not on the train.
     * Note that this differs from disembarking a list from an empty train,
     * there are passengers boarded here, just not the ones being disembarked.
     */
    @Test
    public void disembarkNonBoardedCollectionTest() {
        this.trainH.boardAll(plistA);
        assertFalse(this.trainH.disembarkAll(plistB));
        assertEquals(plistA.size(), this.trainH.numberOfPassengersOnBoard());
    }

    /**
     * Test disembarking a single passenger.
     */
    @Test
    public void disembarkTest() {
        Passenger pass = new Passenger(this.start, this.destination);
        this.trainH.board(pass);
        assertTrue(this.trainH.disembark(pass));
        assertEquals(0, this.trainH.numberOfPassengersOnBoard());
        assertFalse(this.trainH.disembark(pass));
    }

    /**
     * Test disembarking all passengers.
     */
    @Test
    public void disembarkAllTest() {
        this.trainH.boardAll(plistA);
        this.trainH.boardAll(plistB);

        // Disembark 1 collection of passengers.
        assertTrue(this.trainH.disembarkAll(plistA));
        assertFalse(this.trainH.passengers().containsAll(plistA));
        assertEquals(plistB.size(), this.trainH.numberOfPassengersOnBoard());

        // Disembark the second collection of passengers.
        assertTrue(this.trainH.disembarkAll(plistB));
        assertFalse(this.trainH.passengers().containsAll(plistB));
        assertEquals(0, this.trainH.numberOfPassengersOnBoard());
    }

    @Test
    public void ensureDeepCopyOfBoardedPassengersIsSameAsOriginal() {
        Station station0 = new Station("Station 1");
        Station station1 = new Station("Station 2");

        Passenger pass0 = new Passenger(station0, station1);
        Passenger pass1 = new Passenger(station0, station1);
        Passenger pass2 = new Passenger(station0, station1);

        Train train0 = new Train(5);

        train0.board(pass0);
        train0.board(pass1);
        train0.board(pass2);

        ArrayList<Passenger> origBoardedPassengers = new ArrayList<Passenger>();
        origBoardedPassengers.add(pass0);
        origBoardedPassengers.add(pass1);
        origBoardedPassengers.add(pass2);

        Collection<Passenger> copyBoardedPassengers = train0.passengers();
        Passenger[] copyBoardedPassengersArray = copyBoardedPassengers.toArray(
                                                 new Passenger[
                                                 copyBoardedPassengers.size()]);

        assertEquals(origBoardedPassengers.size(), copyBoardedPassengers.size());

        assertEquals(origBoardedPassengers.get(0).start().getName(), copyBoardedPassengersArray[0].start().getName());
        assertEquals(origBoardedPassengers.get(0).destination().getName(), copyBoardedPassengersArray[0].destination().getName());

        assertEquals(origBoardedPassengers.get(1).start().getName(), copyBoardedPassengersArray[1].start().getName());
        assertEquals(origBoardedPassengers.get(1).destination().getName(), copyBoardedPassengersArray[1].destination().getName());

        assertEquals(origBoardedPassengers.get(2).start().getName(), copyBoardedPassengersArray[2].start().getName());
        assertEquals(origBoardedPassengers.get(2).destination().getName(), copyBoardedPassengersArray[2].destination().getName());
    }
}