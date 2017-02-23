package cz.vhromada.result;

/**
 * An enum represents status.
 *
 * @author Vladimir Hromada
 */
public enum Status {

    /**
     * OK
     */
    OK,

    /**
     * Warning
     */
    WARN,

    /**
     * Error
     */
    ERROR;

    /**
     * Returns status for severity.
     *
     * @param severity severity
     * @return status for severity
     * @throws IllegalArgumentException if status can't be found
     */
    public static Status getStatus(final Severity severity) {
        if (severity == null) {
            return null;
        }

        for (final Status status : values()) {
            if (status.ordinal() == severity.ordinal()) {
                return status;
            }
        }

        throw new IllegalArgumentException("No Status found for Severity " + severity);
    }

}
