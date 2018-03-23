package cz.vhromada.validation;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.validation.severity.Warning;

import org.springframework.util.CollectionUtils;

/**
 * A class represents converter from constraint validations to result.
 *
 * @param <T> type of constraint validation
 * @param <U> type of result data
 * @author Vladimir Hromada
 */
public class ResultConverter<T, U> {

    /**
     * Returns converted constraint validations to result.
     *
     * @param constraintViolations constraint validations
     * @return converted constraint validations to result
     */
    public Result<U> convert(final Collection<ConstraintViolation<T>> constraintViolations) {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return new Result<>();
        }

        final Result<U> result = new Result<>();
        result.addEvents(constraintViolations.stream().map(this::convert).collect(Collectors.toList()));

        return result;
    }

    /**
     * Returns converted constraint validation to event.
     *
     * @param constraintViolation constraint validation
     * @return converted constraint validation to event
     */
    public Event convert(final ConstraintViolation<T> constraintViolation) {
        final Severity severity = constraintViolation.getConstraintDescriptor().getPayload().contains(Warning.class) ? Severity.WARN : Severity.ERROR;
        final String property = constraintViolation.getPropertyPath().toString();
        final String validationName = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();

        return new Event(severity, property + validationName, constraintViolation.getMessage());
    }

}
