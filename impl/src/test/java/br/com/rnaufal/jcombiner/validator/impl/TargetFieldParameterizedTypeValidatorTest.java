package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by rnaufal
 */
class TargetFieldParameterizedTypeValidatorTest {

    private TargetFieldParameterizedTypeValidator validator;

    @BeforeEach
    void setUp() {
        this.validator = new TargetFieldParameterizedTypeValidator();
    }

    @Test
    void validResultWhenFieldHasSameTypeOnTargetClass() {
        final var integersField = getDeclaredField(ValidCombinationClass.class, "integers", true);

        final var actualResult = validator.validate(integersField, ValidCombinationClass.TargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(true)));
        assertThat(actualResult.getField().getName(), is(equalTo("integers")));
    }

    @Test
    void validResultWhenFieldHasCustomNameAndSameTypeOnTargetClass() {
        final var valuesField = getDeclaredField(ValidCombinationClass.class, "values", true);

        final var actualResult = validator.validate(valuesField, ValidCombinationClass.TargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(true)));
        assertThat(actualResult.getField().getName(), is(equalTo("values")));
    }

    @Test
    void invalidResultWhenSourceCombinationFieldHasNoTypeArgument() {
        final var stringsField = getDeclaredField(InvalidSourceStringsCombinationClass.class, "strings", true);

        final var actualResult = validator.validate(stringsField, InvalidSourceStringsCombinationClass.TargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(false)));
        assertThat(actualResult.getField().getName(), is(equalTo("strings")));
    }

    @Test
    void invalidWhenSourceAndTargetActualTypeArgumentsAreDifferent() {
        final var numbersField = getDeclaredField(InvalidFloatCombinationClass.class, "numbers", true);

        final var actualResult = validator.validate(numbersField, InvalidFloatCombinationClass.InvalidTypeArgumentTargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(false)));
        assertThat(actualResult.getField().getName(), is(equalTo("numbers")));
    }

    private static class ValidCombinationClass {

        @CombinationProperty(size = 3)
        private Collection<Integer> integers;

        @CombinationProperty(size = 2, name = "inputValues")
        private List<Double> values;

        private static class TargetCombinationClass {

            private Combinations<Integer> integers;

            private Combinations<Double> inputValues;
        }
    }

    private static class InvalidSourceStringsCombinationClass {

        @CombinationProperty(size = 4)
        private List strings;

        private static class TargetCombinationClass {

            private Combinations<String> strings;
        }
    }

    private static class InvalidFloatCombinationClass {

        @CombinationProperty(size = 5)
        private List<Float> numbers;

        private static class InvalidTypeArgumentTargetCombinationClass {

            private Combinations<Integer> numbers;
        }
    }
}