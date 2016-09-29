import java.util.ArrayList;
import java.util.Collection;
/**
 * A light rail train.
 *
 * @author Dr. Jody Paul
 * @version 1.1 ($Id: Train.java 355 2016-05-01 20:23:53Z abdalla $)
 */
public class Train {
    /** The default maximum capacity of a train. */
    public static final int DEFAULT_CAPACITY = 120;

    /** The maximum number of passengers this train can hold. */
    private int maxCapacity;

    /** The passengers currently on board this train. */
    private Collection<Passenger> boardedPassengers;

    /**
     * Constructs a train with a maximum capacity.
     * @param capacity the maximum number of passengers;
     *        if less than zero, uses default capacity
     */
    public Train(final int capacity) {
        if (capacity < 0) {
            this.maxCapacity = DEFAULT_CAPACITY;
        } else {
            this.maxCapacity = capacity;
        }
        boardedPassengers = new ArrayList<>();
    }

    /**
     * A copy constructor.
     * @param capacity the maximum number of passengers
     * @param boardPass the Collection of boarded passengers on train
     */
    public Train(final int capacity, final Collection<Passenger> boardPass) {
        if (capacity < 0) {
           this.maxCapacity = DEFAULT_CAPACITY;
        } else {
        this.maxCapacity = capacity;
        }
        this.boardedPassengers = boardPass;
    }

    /**
     * Boards a passenger onto this train.
     * @param passenger the passenger to board; must not be <tt>null</tt>
     * @return <tt>true</tt> if the passenger was not already on this train
     *         and was permitted to board (within capacity)
     */
    public boolean board(final Passenger passenger) {
        boolean passengerAlreadyBoarded = boardedPassengers.contains(passenger);
        boolean atCapacity = (boardedPassengers.size()) >= this.maxCapacity;

        if (passenger != null && !passengerAlreadyBoarded && !atCapacity) {
            return boardedPassengers.add(passenger);
        }

        return false;
    }

    /**
     * Boards all passengers in the parameter onto this train.
     * If boarding the number of passengers would cause the maximum
     * capacity to be exceeded, none are boarded.
     * @param passengerGroup the passenger to board; must not be <tt>null</tt>
     * @return <tt>true</tt> if any of the passengers boarded the train
     */
    public boolean boardAll(final Collection<Passenger> passengerGroup) {
        ArrayList<Passenger> newPassengers = new ArrayList<>(passengerGroup);
        newPassengers.removeAll(boardedPassengers);

        if ((boardedPassengers.size() + newPassengers.size())
            > this.maxCapacity) {
                return false;
        }

        return boardedPassengers.addAll(newPassengers);
    }

    /**
     * Disembarks a passenger from this train.
     * @param passenger the passenger to disembark
     * @return <tt>true</tt> if the passenger was on this train
     */
    public boolean disembark(final Passenger passenger) {
        return boardedPassengers.remove(passenger);
    }

    /**
     * Disembarks the collection of passengers from this train.
     * @param passengerGroup the passengers to disembark;
     *                       ignored if <tt>null</tt>
     * @return <tt>true</tt> if any of the passengers were on this train
     */
    public boolean disembarkAll(final Collection<Passenger> passengerGroup) {
        if (passengerGroup == null) {
            return false;
        }
        return boardedPassengers.removeAll(passengerGroup);
    }

    /**
     * Retrieves the passengers on this train as a
     * deep copy of the original.
     * @return the passengers
     */
    public Collection<Passenger> passengers() {
        Passenger[] boardedPassengersArray = boardedPassengers.toArray(
                                 new Passenger[boardedPassengers.size()]);

        ArrayList<Passenger> copyBoardedPassengers = new ArrayList<Passenger>();

        for (int i = 0; i < boardedPassengersArray.length; i++) {
            Passenger copyPassenger = new Passenger(boardedPassengersArray[i]);
            copyBoardedPassengers.add(copyPassenger);
        }

        return copyBoardedPassengers;

       // return this.boardedPassengers;
    }

    /**
     * Retrieves the number of passengers on this train.
     * @return the number of passengers
     */
    public int numberOfPassengersOnBoard() {
        return boardedPassengers.size();
    }

    /**
     * Retrieves the maximum capacity of this train.
     * @return the maximum number of passengers allowed
     */
    public int maximumCapacity() {
        return maxCapacity;
    }
}

