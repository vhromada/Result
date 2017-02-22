package cz.vhromada.result;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link Result}.
 *
 * @author Vladimir Hromada
 */
public class ResultTest {

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
    @Before
    public void setUp() {
        result = new Result<>();
        infoEvent = new Event(Severity.INFO, KEY, MESSAGE);
        warnEvent = new Event(Severity.WARN, KEY, MESSAGE);
        errorEvent = new Event(Severity.ERROR, KEY, MESSAGE);
    }

    /**
     * Test method for {@link Result#addEvent(Event)}.
     */
    @Test
    public void addEvent() {
        result.addEvent(infoEvent);

        assertThat(result.getStatus(), is(equalTo(Status.OK)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Collections.singletonList(infoEvent))));

        result.addEvent(warnEvent);

        assertThat(result.getStatus(), is(equalTo(Status.WARN)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Arrays.asList(infoEvent, warnEvent))));

        result.addEvent(infoEvent);

        assertThat(result.getStatus(), is(equalTo(Status.WARN)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Arrays.asList(infoEvent, warnEvent, infoEvent))));

        result.addEvent(errorEvent);

        assertThat(result.getStatus(), is(equalTo(Status.ERROR)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent))));

        result.addEvent(infoEvent);

        assertThat(result.getStatus(), is(equalTo(Status.ERROR)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent, infoEvent))));

        result.addEvent(warnEvent);

        assertThat(result.getStatus(), is(equalTo(Status.ERROR)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent, infoEvent, warnEvent))));
    }

    /**
     * Test method for {@link Result#addEvent(Event)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addEvent_NullArgument() {
        result.addEvent(null);
    }

    /**
     * Test method for {@link Result#addEvents(List)}.
     */
    @Test
    public void addEvents() {
        result.addEvents(Arrays.asList(infoEvent, warnEvent, errorEvent));

        assertThat(result.getStatus(), is(equalTo(Status.ERROR)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Arrays.asList(infoEvent, warnEvent, errorEvent))));
    }

    /**
     * Test method for {@link Result#addEvents(List)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addEvents_NullArgument() {
        result.addEvents(null);
    }

    /**
     * Test method for {@link Result#addEvents(List)} with argument with null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addEvents_ArgumentWithNull() {
        result.addEvents(Arrays.asList(infoEvent, null, errorEvent));
    }

    /**
     * Test method for {@link Result#of(Object)}.
     */
    @Test
    public void of() {
        final Result<String> stringResult = Result.of(DATA);

        assertThat(stringResult.getStatus(), is(equalTo(Status.OK)));
        assertThat(stringResult.getData(), is(equalTo(DATA)));
        assertThat(stringResult.getEvents(), is(equalTo(Collections.emptyList())));
    }

    /**
     * Test method for {@link Result#info(String, String)}.
     */
    @Test
    public void info() {
        final Result<String> infoResult = Result.info(KEY, MESSAGE);

        assertThat(infoResult.getStatus(), is(equalTo(Status.OK)));
        assertThat(infoResult.getData(), nullValue());
        assertThat(infoResult.getEvents(), is(equalTo(Collections.singletonList(infoEvent))));
    }

    /**
     * Test method for {@link Result#info(String, String)} with null key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void info_NullKey() {
        Result.info(null, MESSAGE);
    }

    /**
     * Test method for {@link Result#info(String, String)} with null message.
     */
    @Test(expected = IllegalArgumentException.class)
    public void info_NullMessage() {
        Result.info(KEY, null);
    }

    /**
     * Test method for {@link Result#warn(String, String)}.
     */
    @Test
    public void warn() {
        final Result<String> warnResult = Result.warn(KEY, MESSAGE);

        assertThat(warnResult.getStatus(), is(equalTo(Status.WARN)));
        assertThat(warnResult.getData(), nullValue());
        assertThat(warnResult.getEvents(), is(equalTo(Collections.singletonList(warnEvent))));
    }

    /**
     * Test method for {@link Result#warn(String, String)} with null key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void warn_NullKey() {
        Result.warn(null, MESSAGE);
    }

    /**
     * Test method for {@link Result#warn(String, String)} with null message.
     */
    @Test(expected = IllegalArgumentException.class)
    public void warn_NullMessage() {
        Result.warn(KEY, null);
    }

    /**
     * Test method for {@link Result#error(String, String)}.
     */
    @Test
    public void error() {
        final Result<String> errorResult = Result.error(KEY, MESSAGE);

        assertThat(errorResult.getStatus(), is(equalTo(Status.ERROR)));
        assertThat(errorResult.getData(), nullValue());
        assertThat(errorResult.getEvents(), is(equalTo(Collections.singletonList(errorEvent))));
    }

    /**
     * Test method for {@link Result#error(String, String)} with null key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void error_NullKey() {
        Result.error(null, MESSAGE);
    }

    /**
     * Test method for {@link Result#error(String, String)} with null message.
     */
    @Test(expected = IllegalArgumentException.class)
    public void error_NullMessage() {
        Result.error(KEY, null);
    }

}
