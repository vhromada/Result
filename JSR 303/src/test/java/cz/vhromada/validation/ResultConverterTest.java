package cz.vhromada.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * A class represents test for class {@link ResultConverter}.
 *
 * @author Vladimir Hromada
 */
public class ResultConverterTest {

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
    @Before
    public void setUp() {
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
    public void convert_CorrectData() {
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertThat(result.getStatus(), is(equalTo(Status.OK)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), is(equalTo(Collections.emptyList())));
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with incorrect text.
     */
    @Test
    public void convert_IncorrectString() {
        bean.setText(null);
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertThat(result.getStatus(), is(equalTo(Status.WARN)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), notNullValue());
        assertThat(result.getEvents().size(), is(equalTo(1)));
        assertThat(result.getEvents().get(0), is(equalTo(new Event(Severity.WARN, "textNotNull", "Value mustn't be null."))));
    }

    /**
     * Test method for {@link ResultConverter#convert(Collection)} with incorrect number.
     */
    @Test
    public void convert_IncorrectNumber() {
        bean.setNumber(1);
        final Set<ConstraintViolation<Bean>> constraintViolations = validator.validate(bean);

        final Result<Void> result = resultConverter.convert(constraintViolations);

        assertThat(result.getStatus(), is(equalTo(Status.ERROR)));
        assertThat(result.getData(), nullValue());
        assertThat(result.getEvents(), notNullValue());
        assertThat(result.getEvents().size(), is(equalTo(1)));
        assertThat(result.getEvents().get(0), is(equalTo(new Event(Severity.ERROR, "numberMin", "Value must be greater than 5."))));
    }

    /**
     * A class represents bean for validation.
     */
    private static final class Bean {

        /**
         * Text
         */
        @NotNull(payload = Warning.class)
        @SuppressWarnings("unused")
        private String text;

        /**
         * Number
         */
        @Min(5)
        @SuppressWarnings("unused")
        private int number;

        /**
         * Sets a new value to text.
         *
         * @param text new value
         */
        @SuppressFBWarnings("URF_UNREAD_FIELD")
        void setText(final String text) {
            this.text = text;
        }

        /**
         * Sets a new value to number.
         *
         * @param number new value
         */
        @SuppressFBWarnings("URF_UNREAD_FIELD")
        void setNumber(final int number) {
            this.number = number;
        }

    }

}
