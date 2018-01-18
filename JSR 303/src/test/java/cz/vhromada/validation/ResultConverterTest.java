package cz.vhromada.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collection;
import java.util.Collections;
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

        assertThat(result).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEmpty();
        });
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with incorrect text.
     */
    @Test
    void convert_IncorrectText() {
        bean.setText(null);
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertThat(result).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.WARN);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.WARN, "textNotNull", "Value mustn't be null.")));
        });
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with incorrect number.
     */
    @Test
    void convert_IncorrectNumber() {
        bean.setNumber(1);
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertThat(result).isNotNull();
        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getData()).isNull();
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "numberMin", "Value must be greater than 5.")));
        });
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
