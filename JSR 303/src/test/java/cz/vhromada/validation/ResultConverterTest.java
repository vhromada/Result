package cz.vhromada.validation;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;
import cz.vhromada.validation.severity.Warning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * A class represents test for class {@link ResultConverter}.
 *
 * @author Vladimir Hromada
 */
class ResultConverterTest {

    /**
     * Instance of {@link ResultConverter}
     */
    private ResultConverter<Bean, Void> resultConverter;

    /**
     * Instance of {@link Validator}
     */
    private Validator validator;

    /**
     * Instance of {@link Bean}
     */
    private Bean bean;

    /**
     * Initializes converter, validator and bean.
     */
    @BeforeEach
    void setUp() {
        final ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("messages");
        final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(resourceBundleMessageSource);
        localValidatorFactoryBean.afterPropertiesSet();

        resultConverter = new ResultConverter<>();
        validator = localValidatorFactoryBean;
        bean = new Bean();
        bean.setText("Value");
        bean.setNumber(10);
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with correct data.
     */
    @Test
    void convert_CorrectData() {
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertNotNull(result);
        assertAll(() -> assertEquals(Status.OK, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertTrue(result.getEvents().isEmpty()));
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with incorrect text.
     */
    @Test
    void convert_IncorrectText() {
        bean.setText(null);
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertNotNull(result);
        assertAll(() -> assertEquals(Status.WARN, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(1, result.getEvents().size()),
            () -> assertEquals(new Event(Severity.WARN, "textNotNull", "Value mustn't be null."), result.getEvents().get(0)));
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with incorrect number.
     */
    @Test
    void convert_IncorrectNumber() {
        bean.setNumber(1);
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertNotNull(result);
        assertAll(() -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(1, result.getEvents().size()),
            () -> assertEquals(new Event(Severity.ERROR, "numberMin", "Value must be greater than 5."), result.getEvents().get(0)));
    }

    /**
     * A class represents bean for validation.
     */
    private static final class Bean {

        /**
         * Text
         */
        @SuppressWarnings("unused")
        private @NotNull(payload = Warning.class) String text;

        /**
         * Number
         */
        @SuppressWarnings("unused")
        private @Min(5) int number;

        /**
         * Sets a new value to text.
         *
         * @param text new value
         */
        void setText(final String text) {
            this.text = text;
        }

        /**
         * Sets a new value to number.
         *
         * @param number new value
         */
        void setNumber(final int number) {
            this.number = number;
        }

    }

}
