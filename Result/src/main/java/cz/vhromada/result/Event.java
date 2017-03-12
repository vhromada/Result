package cz.vhromada.result;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.util.Assert;

/**
 * A class represents event.
 *
 * @author Vladimir Hromada
 */
public class Event implements Serializable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Severity
     */
    private Severity severity;

    /**
     * Key
     */
    private String key;

    /**
     * Message
     */
    private String message;

    /**
     * Creates a new instance of Event.
     *
     * @param severity severity
     * @param key      key
     * @param message  message
     * @throws IllegalArgumentException if severity is null
     *                                  or key is null
     *                                  or message is null
     */
    public Event(final Severity severity, final String key, final String message) {
        Assert.notNull(severity, "Severity mustn't be null.");
        Assert.notNull(key, "Key mustn't be null.");
        Assert.notNull(message, "Message mustn't be null.");

        this.severity = severity;
        this.key = key;
        this.message = message;
    }

    /**
     * Returns severity.
     *
     * @return severity
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Returns key.
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Event)) {
            return false;
        }

        final Event event = (Event) obj;
        return severity == event.severity && key.equals(event.key) && message.equals(event.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, key, message);
    }

    @Override
    public String toString() {
        return String.format("Event [severity=%s, key=%s, message=%s]", severity, key, message);
    }

}
