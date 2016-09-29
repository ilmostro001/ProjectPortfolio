import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.Map;
/**
 * Light-rail model initialization and simulation.
 *
 * @author Dr. Jody Paul
 * @version 1.1 ($Id: Simulation.java 377 2016-05-02 03:25:12Z joshua $)
 */
public class Simulation {

    /** The model route. */
    private Route modelRoute;

    /** The model departure schedule. */
    private Schedule modelSchedule;

    /** The departure times in this schedule. */
    private Iterator<Time> departureTimesIterator;

    /** The model minimum wait time at a station. */
    private Time modelMinimumWaitTime;

    /** The model maximum number of passengers per train. */
    private Integer modelPassengersPerTrain;

    /** The model passenger boarding function. */
    private BoardingFunction modelBoardingFn;

    /** The model function for passenger arrivals at a station. */
    private PassengerArrivalFunction modelArrivalFn;

    /** Trains known to this simulation. */
    private List<Train> knownTrains;

    /** Stations known to this simulation. */
    private List<Station> knownStations;

    /** Current time of this simulation. */
    private Time currentTime;

    /** Default Simulation start time. */
    private Time defaultStartTime = new Time(0, 0, 0);

    /** List of the time to the next station for each train. */
    private List<Time> timesFromTrainsToStations;

    /** Map of trains to their travel times and next stations. */
    private Map<Train, NextStationAndTravelTime> trainToStationAndTime;

    /** Private class for storing time and station data. */
    class NextStationAndTravelTime {

        /** Time to the next station. */
        private Time timeToNextStation;

        /** The next station that will be reached. */
        private Station nextStation;
        /**
         * Retrieve the next station and the time to the next station.
         * @param nextStation the next station that will be reached
         * @param timeToNextStation the time to reach next station
         */
        NextStationAndTravelTime(final Station nextStation,
                                        final Time timeToNextStation) {
            this.timeToNextStation = timeToNextStation;
            this.nextStation = nextStation;
        }
    }

    /**
     * Model initialization with fixed sized trains
     * and the same passenger arrival function for
     * every station on the route.
     * Sets up the following simulation parameters:
     * route,
     * departure schedule,
     * minimum wait time,
     * maximum number of passengers per train,
     * boarding function,
     * passenger arrival function.
     * @param route the light-rail route
     * @param schedule the daily schedule
     * @param minWaitTime the minimum wait time at a station
     * @param maxPassengers the maximum number of passengers per train
     * @param boardingFunc the boarding function
     * @param arrivalFunc the passenger arrival function
     */
    public void initializeModel(final Route route,
                                final Schedule schedule,
                                final Time minWaitTime,
                                final Integer maxPassengers,
                                final BoardingFunction boardingFunc,
                                final PassengerArrivalFunction arrivalFunc) {
        this.modelRoute = route;
        this.modelSchedule = schedule;
        this.modelMinimumWaitTime = minWaitTime;
        this.modelPassengersPerTrain = maxPassengers;
        this.modelBoardingFn = boardingFunc;
        this.modelArrivalFn = arrivalFunc;
        this.currentTime = defaultStartTime;
        this.departureTimesIterator = modelSchedule.departureTimes().iterator();
        this.knownStations = modelRoute.stations();
        this.knownTrains = new ArrayList<>();
    }

    /**
     * Advances simulation by specified amount of time.
     * @param duration the amount of time to advice simulation
     */
    public void advanceSimulation(final Time duration) {
        Time subTimer = new Time(0, 0, 0);
        while (subTimer.compareTo(duration) < 0) {
            // Depart a train if needed.
            if (modelSchedule.departureTimes().contains(currentTime)) {
                knownTrains.add(0, new Train(modelPassengersPerTrain));
            }

            subTimer.incrementOneSecond();
            currentTime.incrementOneSecond();
        }
    }

    /**
     * Determines current number of clumps of trains.
     * Only trains at a station or blocked by a train
     * at a station participate in a clump.
     * A clump consists of one or more trains.
     * @return the number of clumps
     */
    public Integer numberOfClumps() {
        return null;
    }

    /**
     * Retrieves the trains currently in the simulation.
     * @return the trains in this simulation
     */
    public Collection<Train> trains() {
        ArrayList<Train> copyKnownTrains = new ArrayList<Train>();
        int copyMaxCapacity;
        Collection<Passenger> copyBoardedPassengers;


        for (int i = 0; i < knownTrains.size(); i++) {
            copyMaxCapacity = knownTrains.get(i).maximumCapacity();
            copyBoardedPassengers = knownTrains.get(i).passengers();
            Train copyTrain = new Train(copyMaxCapacity, copyBoardedPassengers);
            copyKnownTrains.add(copyTrain);
        }

        return copyKnownTrains;
    }

    /**
     * Retrieves the stations currently in the simulation.
     * @return the stations in this simulation
     */
    public Collection<Station> stations() {
        return this.knownStations;
    }

    /**
     * Renders the current state of the model as a displayable string.
     * ToDo: Render actual content
     * @return textual rendering of model state
     */
    @Override
    public String toString() {
        return "Model State:\n"
                + "Current time: " + currentTime.toString() + "\n"
                + modelSchedule.departureTimes().stream()
                                                .map(time -> time.toString())
                                                .collect(Collectors.joining(
                                                ", ")) + "\n";
    }
}