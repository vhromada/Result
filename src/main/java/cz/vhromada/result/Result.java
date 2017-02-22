package cz.vhromada.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * A class represents result.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public class Result<T> {

    /**
     * Status
     */
    private Status status;

    /**
     * Data
     */
    private T data;

    /**
     * Events
     */
    private List<Event> events;

    /**
     * Creates a new instance of Result.
     */
    public Result() {
        this.status = Status.OK;
        this.events = new ArrayList<>();
    }

    /**
     * Returns status.
     *
     * @return status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns data.
     *
     * @return data
     */
    public T getData() {
        return data;
    }

    /**
     * Returns events.
     *
     * @return events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Adds event.
     *
     * @param event event
     * @throws IllegalArgumentException if event is null
     */
    public void addEvent(final Event event) {
        Assert.notNull(event, "Event mustn't be null.");

        this.events.add(event);
        this.status = getNewStatus(this.status, event);
    }

    /**
     * Adds events.
     *
     * @param events events
     * @throws IllegalArgumentException if events are null
     *                                  or events contains null
     */
    public void addEvents(final List<Event> events) {
        Assert.notNull(events, "Events mustn't be null.");

        for (final Event event : events) {
            addEvent(event);
        }
    }

    /**
     * Returns result with specified data.
     *
     * @param data data
     * @param <T>  type of data
     * @return result with specified data
     */
    public static <T> Result<T> of(final T data) {
        final Result<T> result = new Result<>();
        result.data = data;

        return result;
    }

    /**
     * Returns result with specified information message.
     *
     * @param key     key
     * @param message message
     * @param <T>     type of data
     * @return result with specified information message
     * @throws IllegalArgumentException if key is null
     *                                  or value is null
     */
    public static <T> Result<T> info(final String key, final String message) {
        final Result<T> result = new Result<>();
        result.addEvent(new Event(Severity.INFO, key, message));

        return result;
    }

    /**
     * Returns result with specified warning message.
     *
     * @param key     key
     * @param message message
     * @param <T>     type of data
     * @return result with specified warning message
     * @throws IllegalArgumentException if key is null
     *                                  or value is null
     */
    public static <T> Result<T> warn(final String key, final String message) {
        final Result<T> result = new Result<>();
        result.addEvent(new Event(Severity.WARN, key, message));

        return result;
    }

    /**
     * Returns result with specified error message.
     *
     * @param key     key
     * @param message message
     * @param <T>     type of data
     * @return result with specified error message
     * @throws IllegalArgumentException if key is null
     *                                  or value is null
     */
    public static <T> Result<T> error(final String key, final String message) {
        final Result<T> result = new Result<>();
        result.addEvent(new Event(Severity.ERROR, key, message));

        return result;
    }

    /**
     * Returns new status.
     *
     * @param status original status
     * @param event  event
     * @return new status
     */
    private static Status getNewStatus(final Status status, final Event event) {
        final Status newStatus = Status.getStatus(event.getSeverity());

        return status.ordinal() >= newStatus.ordinal() ? status : newStatus;
    }

    @Override
    public String toString() {
        return String.format("Result [status=%s, data=%s, events=%s]", status, data, events);
    }

}
