package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationClass;
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

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by rnaufal
 */
@ExtendWith(MockitoExtension.class)
class FieldExistsOnTargetClassValidatorTest {

    @Mock
    private FieldValidator nextValidator;

    private FieldExistsOnTargetClassValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FieldExistsOnTargetClassValidator(nextValidator);
    }

    @Test
    void validResultWhenFieldExistsOnTargetClass() {
        final var stringsField = getDeclaredField(ValidCombinationClass.class, "strings", true);
        final var targetStringsField = getDeclaredField(ValidCombinationClass.TargetCombinationClass.class, "strings", true);

        when(nextValidator.validate(stringsField, ValidCombinationClass.TargetCombinationClass.class))
                .thenReturn(FieldValidationResult.ok(stringsField, targetStringsField, validator.getClass()));

        final var actualValidationResult = validator.validate(stringsField, ValidCombinationClass.TargetCombinationClass.class);

        assertThat(actualValidationResult.isValid(), is(equalTo(true)));
        assertThat(actualValidationResult.getField().getName(), is(equalTo("strings")));
    }

    @Test
    void invalidResultWhenFieldNotExistsOnTargetClass() {
        final var integersField = getDeclaredField(IntegersCombinationClass.class, "integers", true);

        final var actualValidationResult = validator.validate(integersField, IntegersCombinationClass.InvalidTargetCombinationClass.class);

        assertThat(actualValidationResult.isValid(), is(equalTo(false)));
        assertThat(actualValidationResult.getField().getName(), is(equalTo("integers")));
    }

    @Test
    void invalidResultWhenFieldCustomNameNotExistsOnTargetClass() {
        final var integersField = getDeclaredField(IntegersCombinationClass.class, "otherIntegers", true);

        final var actualValidationResult = validator.validate(integersField, IntegersCombinationClass.InvalidTargetCombinationClass.class);

        assertThat(actualValidationResult.isValid(), is(equalTo(false)));
        assertThat(actualValidationResult.getField().getName(), is(equalTo("otherIntegers")));
    }

    @CombinationClass(ValidCombinationClass.TargetCombinationClass.class)
    private static class ValidCombinationClass {

        @CombinationProperty(size = 2)
        private Collection<String> strings;

        private static class TargetCombinationClass {

            private Combinations<String> strings;
        }
    }

    @CombinationClass(IntegersCombinationClass.InvalidTargetCombinationClass.class)
    private static class IntegersCombinationClass {

        @CombinationProperty(size = 4)
        private Collection<Integer> integers;

        @CombinationProperty(size = 5, name = "combinations")
        private Collection<Integer> otherIntegers;

        private static class InvalidTargetCombinationClass {

            private Combinations field;

            private Combinations stringCombinations;
        }
    }
}