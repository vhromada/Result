package cz.vhromada.result;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(infoEvent));
        });

        result.addEvent(warnEvent);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.WARN);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(infoEvent, warnEvent));
        });

        result.addEvent(infoEvent);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.WARN);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(infoEvent, warnEvent, infoEvent));
        });

        result.addEvent(errorEvent);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent));
        });

        result.addEvent(infoEvent);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent, infoEvent));
        });

        result.addEvent(warnEvent);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(infoEvent, warnEvent, infoEvent, errorEvent, infoEvent, warnEvent));
        });
    }

    /**
     * Test method for {@link Result#addEvent(Event)} with null event.
     */
    @Test
    void addEvent_NullEvent() {
        assertThatThrownBy(() -> result.addEvent(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#addEvents(List)}.
     */
    @Test
    void addEvents() {
        result.addEvents(Arrays.asList(infoEvent, warnEvent, errorEvent));

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Arrays.asList(infoEvent, warnEvent, errorEvent));
        });
    }

    /**
     * Test method for {@link Result#addEvents(List)} with null events.
     */
    @Test
    void addEvents_NullEvents() {
        assertThatThrownBy(() -> result.addEvents(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#addEvents(List)} with events with null.
     */
    @Test
    void addEvents_EventsWithNull() {
        assertThatThrownBy(() -> result.addEvents(Arrays.asList(infoEvent, null, errorEvent))).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#of(Object)}.
     */
    @Test
    void of() {
        final Result<String> stringResult = Result.of(DATA);

        assertSoftly(softly -> {
            softly.assertThat(stringResult.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(stringResult.getData()).isEqualTo(DATA);
            softly.assertThat(stringResult.getEvents()).isEmpty();
        });
    }

    /**
     * Test method for {@link Result#info(String, String)}.
     */
    @Test
    void info() {
        final Result<String> infoResult = Result.info(KEY, MESSAGE);

        assertSoftly(softly -> {
            softly.assertThat(infoResult.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(infoResult.getData()).isNull();
            softly.assertThat(infoResult.getEvents()).isEqualTo(Collections.singletonList(infoEvent));
        });
    }

    /**
     * Test method for {@link Result#info(String, String)} with null key.
     */
    @Test
    void info_NullKey() {
        assertThatThrownBy(() -> Result.info(null, MESSAGE)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#info(String, String)} with null message.
     */
    @Test
    void info_NullMessage() {
        assertThatThrownBy(() -> Result.info(KEY, null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#warn(String, String)}.
     */
    @Test
    void warn() {
        final Result<String> warnResult = Result.warn(KEY, MESSAGE);

        assertSoftly(softly -> {
            softly.assertThat(warnResult.getStatus()).isEqualTo(Status.WARN);
            softly.assertThat(warnResult.getData()).isNull();
            softly.assertThat(warnResult.getEvents()).isEqualTo(Collections.singletonList(warnEvent));
        });
    }

    /**
     * Test method for {@link Result#warn(String, String)} with null key.
     */
    @Test
    void warn_NullKey() {
        assertThatThrownBy(() -> Result.warn(null, MESSAGE)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#warn(String, String)} with null message.
     */
    @Test
    void warn_NullMessage() {
        assertThatThrownBy(() -> Result.warn(KEY, null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#error(String, String)}.
     */
    @Test
    void error() {
        final Result<String> errorResult = Result.error(KEY, MESSAGE);

        assertSoftly(softly -> {
            softly.assertThat(errorResult.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(errorResult.getData()).isNull();
            softly.assertThat(errorResult.getEvents()).isEqualTo(Collections.singletonList(errorEvent));
        });
    }

    /**
     * Test method for {@link Result#error(String, String)} with null key.
     */
    @Test
    void error_NullKey() {
        assertThatThrownBy(() -> Result.error(null, MESSAGE)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link Result#error(String, String)} with null message.
     */
    @Test
    void error_NullMessage() {
        assertThatThrownBy(() -> Result.error(KEY, null)).isInstanceOf(IllegalArgumentException.class);
    }

}
