package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by rnaufal
 */
@ExtendWith(MockitoExtension.class)
class CombinationsTargetFieldTypeValidatorTest {

    @Mock
    private FieldValidator nextValidator;

    private CombinationsTargetFieldTypeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CombinationsTargetFieldTypeValidator(nextValidator);
    }

    @Test
    void shouldDelegateToNextValidatorWhenTargetFieldTypeIsCombinations() {
        final var integersField = getDeclaredField(ValidIntegersCombinationClass.class, "integers", true);
        final var targetIntegersField = getDeclaredField(ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class, "integers", true);

        when(nextValidator.validate(integersField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class))
                .thenReturn(FieldValidationResult.ok(integersField, targetIntegersField, nextValidator.getClass()));

        final var fieldValidationResult = validator.validate(integersField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(true)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("integers")));
    }

    @Test
    void validResultWhenFieldHasCustomNameAndSameTypeOnTargetClass() {
        final var valuesField = getDeclaredField(ValidIntegersCombinationClass.class, "values", true);
        final var integerValuesTargetField = getDeclaredField(ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class, "integerValues", true);

        when(nextValidator.validate(valuesField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class))
                .thenReturn(FieldValidationResult.ok(valuesField, integerValuesTargetField, nextValidator.getClass()));

        final var actualResult = validator.validate(valuesField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(true)));
        assertThat(actualResult.getField().getName(), is(equalTo("values")));
    }

    @Test
    void invalidResultWhenTargetFieldHasDifferentType() {
        final var stringsField = getDeclaredField(InvalidStringsCombinationClass.class, "strings", true);

        final var actualResult = validator.validate(stringsField, InvalidStringsCombinationClass.InvalidTargetStringsCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(false)));
        assertThat(actualResult.getField().getName(), is(equalTo("strings")));
    }

    private static class ValidIntegersCombinationClass {

        @CombinationProperty(size = 2)
        private Collection<Integer> integers;

        @CombinationProperty(size = 2, name = "integerValues")
        private List<Integer> values;

        private static class ValidTargetIntegersCombinationClass {

            private Combinations<Integer> integers;

            private Combinations<Integer> integerValues;
        }
    }

    private static class InvalidStringsCombinationClass {

        @CombinationProperty(size = 2)
        private Set<String> strings;

        private static class InvalidTargetStringsCombinationClass {

            private Set<String> strings;
        }
    }
}