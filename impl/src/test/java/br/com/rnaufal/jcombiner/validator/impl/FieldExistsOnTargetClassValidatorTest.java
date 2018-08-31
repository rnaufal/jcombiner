package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.annotation.CombinationsTarget;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by rafael.naufal
 */
class FieldExistsOnTargetClassValidatorTest {

    private FieldExistsOnTargetClassValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FieldExistsOnTargetClassValidator();
    }

    @Test
    void validResultWhenFieldExistsOnTargetClass() throws NoSuchFieldException {
        final var stringsField = ValidCombinationClass.class.getField("strings");

        final var actualValidationResult = validator.validate(stringsField, ValidCombinationClass.TargetCombinationClass.class);

        assertThat(actualValidationResult.isValid(), is(equalTo(true)));
        assertThat(actualValidationResult.getField().getName(), is(equalTo("strings")));
    }

    @Test
    void invalidResultWhenFieldNotExistsOnTargetClass() throws NoSuchFieldException {
        final var integersField = IntegersCombinationClass.class.getField("integers");

        final var actualValidationResult = validator.validate(integersField, IntegersCombinationClass.InvalidTargetCombinationClass.class);

        assertThat(actualValidationResult.isValid(), is(equalTo(false)));
        assertThat(actualValidationResult.getField().getName(), is(equalTo("integers")));
    }

    @Test
    void invalidResultWhenFieldCustomNameNotExistsOnTargetClass() throws NoSuchFieldException {
        final var integersField = IntegersCombinationClass.class.getField("otherIntegers");

        final var actualValidationResult = validator.validate(integersField, IntegersCombinationClass.InvalidTargetCombinationClass.class);

        assertThat(actualValidationResult.isValid(), is(equalTo(false)));
        assertThat(actualValidationResult.getField().getName(), is(equalTo("otherIntegers")));
    }

    @CombinationsTarget(ValidCombinationClass.TargetCombinationClass.class)
    private static class ValidCombinationClass {

        @CombinationProperty(size = 2)
        public Collection<String> strings;

        private static class TargetCombinationClass {

            private Combinations<String> strings;
        }
    }

    @CombinationsTarget(IntegersCombinationClass.InvalidTargetCombinationClass.class)
    private static class IntegersCombinationClass {

        @CombinationProperty(size = 4)
        public Collection<Integer> integers;

        @CombinationProperty(size = 5, targetField = "combinations")
        public Collection<Integer> otherIntegers;

        private static class InvalidTargetCombinationClass {

            private Combinations<Integer> field;

            private Combinations<Integer> stringCombinations;
        }
    }
}