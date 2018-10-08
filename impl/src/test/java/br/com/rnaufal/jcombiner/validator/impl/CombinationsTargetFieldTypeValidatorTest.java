package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.impl.domain.MappedField;
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
        final var mappedField = MappedField.from(integersField);

        when(nextValidator.validate(mappedField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class))
                .thenReturn(FieldValidationResult.ok(mappedField, targetIntegersField, nextValidator.getClass()));

        final var fieldValidationResult = validator.validate(mappedField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(true)));
        assertThat(fieldValidationResult.getMappedField().getField().getName(), is(equalTo("integers")));
    }

    @Test
    void validResultWhenFieldHasCustomNameAndSameTypeOnTargetClass() {
        final var valuesField = getDeclaredField(ValidIntegersCombinationClass.class, "values", true);
        final var integerValuesTargetField = getDeclaredField(ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class, "integerValues", true);
        final var mappedField = MappedField.from(valuesField);

        when(nextValidator.validate(mappedField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class))
                .thenReturn(FieldValidationResult.ok(mappedField, integerValuesTargetField, nextValidator.getClass()));

        final var actualResult = validator.validate(mappedField, ValidIntegersCombinationClass.ValidTargetIntegersCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(true)));
        assertThat(actualResult.getMappedField().getField().getName(), is(equalTo("values")));
    }

    @Test
    void invalidResultWhenTargetFieldHasDifferentType() {
        final var stringsField = getDeclaredField(InvalidStringsCombinationClass.class, "strings", true);
        final var mappedField = MappedField.from(stringsField);

        final var actualResult = validator.validate(mappedField, InvalidStringsCombinationClass.InvalidTargetStringsCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(false)));
        assertThat(actualResult.getMappedField().getField().getName(), is(equalTo("strings")));
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