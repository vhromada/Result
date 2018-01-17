package cz.vhromada.result;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link Result}.
 *
 * @author Vladimir Hromada
 */
class ResultTest {

    /**
     * Data
     */
    private static final String DATA = "data";

    /**
     * Key
     */
    private static final String KEY = "key";

    /**
     * Message
     */
    private static final String MESSAGE = "message";

    /**
     * Instance of {@link Result}
     */
    private Result<String> result;

    /**
     * Instance of {@link Event} with severity information
     */
    private Event infoEvent;

    /**
     * Instance of {@link Event} with severity warning
     */
    private Event warnEvent;

    /**
     * Instance of {@link Event} with severity error
     */
    private Event errorEvent;

    /**
     * Initializes result and events.
     */
    @BeforeEach
    void setUp() {
        result = new Result<>();
        infoEvent = new Event(Severity.INFO, KEY, MESSAGE);
        warnEvent = new Event(Severity.WARN, KEY, MESSAGE);
        errorEvent = new Event(Severity.ERROR, KEY, MESSAGE);
    }

    /**
     * Test method for {@link Result#addEvent(Event)}.
     */
    @Test
    void addEvent() {
        result.addEvent(infoEvent);

        assertAll(() -> assertEquals(Status.OK, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Collections.singletonList(infoEvent), result.getEvents()));

        result.addEvent(warnEvent);

        assertAll(() -> assertEquals(Status.WARN, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Arrays.asList(infoEvent, warnEvent), result.getEvents()));

        result.addEvent(infoEvent);

        assertAll(() -> assertEquals(Status.WARN, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Arrays.asList(infoEvent, warnEvent, infoEvent), result.getEvents()));

        result.addEvent(errorEvent);

        assertAll(() -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent), result.getEvents()));

        result.addEvent(infoEvent);

        assertAll(() -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent, infoEvent), result.getEvents()));

        result.addEvent(warnEvent);

        assertAll(() -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent, infoEvent, warnEvent), result.getEvents()));
    }

    /**
     * Test method for {@link Result#addEvent(Event)} with null event.
     */
    @Test
    void addEvent_NullEvent() {
        assertThrows(IllegalArgumentException.class, () -> result.addEvent(null));
    }

    /**
     * Test method for {@link Result#addEvents(List)}.
     */
    @Test
    void addEvents() {
        result.addEvents(Arrays.asList(infoEvent, warnEvent, errorEvent));

        assertAll(() -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Arrays.asList(infoEvent, warnEvent, errorEvent), result.getEvents()));
    }

    /**
     * Test method for {@link Result#addEvents(List)} with null events.
     */
    @Test
    void addEvents_NullEvents() {
        assertThrows(IllegalArgumentException.class, () -> result.addEvents(null));
    }

    /**
     * Test method for {@link Result#addEvents(List)} with events with null.
     */
    @Test
    void addEvents_EventsWithNull() {
        assertThrows(IllegalArgumentException.class, () -> result.addEvents(Arrays.asList(infoEvent, null, errorEvent)));
    }

    /**
     * Test method for {@link Result#of(Object)}.
     */
    @Test
    void of() {
        final Result<String> stringResult = Result.of(DATA);

        assertAll(() -> assertEquals(Status.OK, stringResult.getStatus()),
            () -> assertEquals(DATA, stringResult.getData()),
            () -> assertTrue(stringResult.getEvents().isEmpty()));
    }

    /**
     * Test method for {@link Result#info(String, String)}.
     */
    @Test
    void info() {
        final Result<String> infoResult = Result.info(KEY, MESSAGE);

        assertAll(() -> assertEquals(Status.OK, infoResult.getStatus()),
            () -> assertNull(infoResult.getData()),
            () -> assertEquals(Collections.singletonList(infoEvent), infoResult.getEvents()));
    }

    /**
     * Test method for {@link Result#info(String, String)} with null key.
     */
    @Test
    void info_NullKey() {
        assertThrows(IllegalArgumentException.class, () -> Result.info(null, MESSAGE));
    }

    /**
     * Test method for {@link Result#info(String, String)} with null message.
     */
    @Test
    void info_NullMessage() {
        assertThrows(IllegalArgumentException.class, () -> Result.info(KEY, null));
    }

    /**
     * Test method for {@link Result#warn(String, String)}.
     */
    @Test
    void warn() {
        final Result<String> warnResult = Result.warn(KEY, MESSAGE);

        assertAll(() -> assertEquals(Status.WARN, warnResult.getStatus()),
            () -> assertNull(warnResult.getData()),
            () -> assertEquals(Collections.singletonList(warnEvent), warnResult.getEvents()));
    }

    /**
     * Test method for {@link Result#warn(String, String)} with null key.
     */
    @Test
    void warn_NullKey() {
        assertThrows(IllegalArgumentException.class, () -> Result.warn(null, MESSAGE));
    }

    /**
     * Test method for {@link Result#warn(String, String)} with null message.
     */
    @Test
    void warn_NullMessage() {
        assertThrows(IllegalArgumentException.class, () -> Result.warn(KEY, null));
    }

    /**
     * Test method for {@link Result#error(String, String)}.
     */
    @Test
    void error() {
        final Result<String> errorResult = Result.error(KEY, MESSAGE);

        assertAll(() -> assertEquals(Status.ERROR, errorResult.getStatus()),
            () -> assertNull(errorResult.getData()),
            () -> assertEquals(Collections.singletonList(errorEvent), errorResult.getEvents()));
    }

    /**
     * Test method for {@link Result#error(String, String)} with null key.
     */
    @Test
    void error_NullKey() {
        assertThrows(IllegalArgumentException.class, () -> Result.error(null, MESSAGE));
    }

    /**
     * Test method for {@link Result#error(String, String)} with null message.
     */
    @Test
    void error_NullMessage() {
        assertThrows(IllegalArgumentException.class, () -> Result.error(KEY, null));
    }

}
