/**
 * Representation of clock time for a 24-hour clock with 1-second resolution.
 * May also be used to specify a duration less than 24 hours.
 * @author Dr. Jody Paul
 * @version 1.4 ($Id: Time.java 344 2016-05-01 18:00:37Z joshua $)
 */
public class Time implements Comparable<Time> {

    /** Hours per day. */
    public static final int HOURS_PER_DAY = 24;

    /** Minutes per hours. */
    public static final int MINUTES_PER_HOUR = 60;

    /** Seconds per minute. */
    public static final int SECONDS_PER_MINUTE = 60;

    /** Hour value. */
    private ModItem hours;

    /** Minute value. */
    private ModItem minutes;

    /** Second value. */
    private ModItem seconds;

    /**
     * Constructs Time object 00:00:00.
     */
    public Time() {
        this(0, 0, 0);
    }

    /**
     * Constructs Time object hour:minute:00.
     * @param hour the hour
     * @param minute the minute
     */
    public Time(final int hour, final int minute) {
        this(hour, minute, 0);
    }

    /**
     * Constructs Time object with specified values.
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     */
    public Time(final int hour, final int minute, final int second) {
        setTime(hour, minute, second);
    }

    /**
     * Copy constructor.
     * @param orig the time to be copied
     */
    public Time(final Time orig) {
        this(orig.hours.intValue(),
             orig.minutes.intValue(),
             orig.seconds.intValue());
    }

    /**
     * Accesses the hour of this time.
     * @return the hour
     */
    public int hour() {
        return this.hours.intValue();
    }

    /**
     * Accesses the minute of this time.
     * @return the minute
     */
    public int minute() {
        return this.minutes.intValue();
    }

    /**
     * Accesses the second of this time.
     * @return the second
     */
    public int second() {
        return this.seconds.intValue();
    }

    /**
     * Returns this time as a number of minutes since 00:00:00,
     * equivalently the number of minutes in this duration.
     * @return this time as minutes
     */
    public int asMinutes() {
        return this.minutes.intValue()
               + (MINUTES_PER_HOUR * this.hours.intValue())
               + (int) (Math.round(this.seconds.intValue()
                                   / (double) SECONDS_PER_MINUTE));
    }

    /**
     * Sets the time to the specified hour and minute,
     * with second set to 0.
     * @param hour the hour
     * @param minute the minute
     */
    public void setTime(final int hour, final int minute) {
        setTime(hour, minute, 0);
    }

    /**
     * Sets the time to the specified hour, minute, and second.
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     */
    public void setTime(final int hour, final int minute, final int second) {
        this.hours = new ModItem(HOURS_PER_DAY, hour);
        this.minutes = new ModItem(MINUTES_PER_HOUR, minute);
        this.seconds = new ModItem(SECONDS_PER_MINUTE, second);
    }

    /**
     * Increment the time by the specified duration.
     * @param duration the incremental amount
     */
    public void increment(final Time duration) {
        incrementHours(duration.hour());
        incrementMinutes(duration.minute());
        incrementSeconds(duration.second());
    }

    /**
     * Increment the time by one second.
     */
    public void incrementOneSecond() {
        incrementSeconds(1);
    }

    /**
     * Increment the time by one minute.
     */
    public void incrementOneMinute() {
        incrementMinutes(1);
    }

    /**
     * Increment the time by one hour.
     */
    public void incrementOneHour() {
        incrementHours(1);
    }

    /**
     * Add a specified number of seconds to this time.
     * @param amount the number of seconds to be added
     */
    public void incrementSeconds(final int amount) {
        this.hours.increment(
                    this.minutes.increment(
                                  this.seconds.increment(amount)));
    }

    /**
     * Add a specified number of minutes to this time.
     * @param amount the number of minutes to be added
     */
    public void incrementMinutes(final int amount) {
        this.hours.increment(this.minutes.increment(amount));
    }

    /**
     * Add a specified number of hours to this time.
     * @param amount the number of hours to be added
     */
    public void incrementHours(final int amount) {
        this.hours.increment(amount);
    }

    /**
     * Determines the duration between two times.
     * End time is at or after the start time.
     * End time is considered to be within a single day's number
     * of hours of the start time.
     * @param startTime the start time
     * @param endTime the end time
     * @return the duration between the start and end times
     */
    public static Time duration(final Time startTime, final Time endTime) {
        int second = 0;
        int minute = 0;
        int hour = 0;
        second = endTime.second() - startTime.second();
        if (second < 0) {
            minute = -1;
            second += SECONDS_PER_MINUTE;
        }
        minute += endTime.minute() - startTime.minute();
        if (minute < 0) {
            hour = -1;
            minute += MINUTES_PER_HOUR;
        }
        hour += endTime.hour() - startTime.hour();
        if (hour < 0) {
            hour += HOURS_PER_DAY;
        }
        return new Time(hour, minute, second);
    }

    /**
     * Default string representation shows hour:minute:second.
     * Use {%link displayHourMinute} for hour:minute.
     * @return "hour:minute:second" rendering
     */
    @Override
    public String toString() {
        String retString;
        retString = this.hours + ":" + this.minutes + ":" + this.seconds;
        return retString;
    }

    /**
     * String rendering of hour:minute:second.
     * @return "hour:minute:second" rendering
     */
    public String displayHoursMinutes() {
        return this.hours + ":" + this.minutes;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || (obj.getClass() != this.getClass())) {
            return false;
        }
        @SuppressWarnings("unchecked") // Have checked explicitly.
        Time tobj = (Time) obj;
        return (tobj.hours.intValue() == this.hours.intValue()
                && tobj.minutes.intValue() == this.minutes.intValue()
                && tobj.seconds.intValue() == this.seconds.intValue());
    }

    @Override
    public int hashCode() {
        return (this.hours.hashCode()
                + this.minutes.hashCode()
                + this.seconds.hashCode());
    }

    @Override
    public int compareTo(final Time tobj) {
        int returnValue = 0;
        if (this.hours.intValue() < tobj.hours.intValue()) {
            returnValue = -1;
        } else if (this.hours.intValue() > tobj.hours.intValue()) {
            returnValue = 1;
        } else if (this.minutes.intValue() < tobj.minutes.intValue()) {
            returnValue = -1;
        } else if (this.minutes.intValue() > tobj.minutes.intValue()) {
            returnValue = 1;
        } else if (this.seconds.intValue() < tobj.seconds.intValue()) {
            returnValue = -1;
        } else if (this.seconds.intValue() > tobj.seconds.intValue()) {
            returnValue = 1;
        }
        return returnValue;
    }
}