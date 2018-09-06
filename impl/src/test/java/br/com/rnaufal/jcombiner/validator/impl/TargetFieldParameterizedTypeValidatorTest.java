package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationClass;
import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by rafael.naufal
 */
class TargetFieldParameterizedTypeValidatorTest {

    private TargetFieldParameterizedTypeValidator validator;

    @BeforeEach
    void setUp() {
        this.validator = new TargetFieldParameterizedTypeValidator();
    }

    @Test
    void validResultWhenFieldHasSameTypeOnTargetClass() throws NoSuchFieldException {
        final var integersField = ValidCombinationClass.class.getField("integers");

        final var actualResult = validator.validate(integersField, ValidCombinationClass.TargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(true)));
        assertThat(actualResult.getField().getName(), is(equalTo("integers")));
    }

    @Test
    void validResultWhenFieldHasCustomNameAndSameTypeOnTargetClass() throws NoSuchFieldException {
        final var valuesField = ValidCombinationClass.class.getField("values");

        final var actualResult = validator.validate(valuesField, ValidCombinationClass.TargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(true)));
        assertThat(actualResult.getField().getName(), is(equalTo("values")));
    }

    @Test
    void invalidResultWhenSourceCombinationFieldHasNoTypeArgument() throws NoSuchFieldException {
        final var stringsField = InvalidSourceStringsCombinationClass.class.getField("strings");

        final var actualResult = validator.validate(stringsField, InvalidSourceStringsCombinationClass.TargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(false)));
        assertThat(actualResult.getField().getName(), is(equalTo("strings")));
    }

    @Test
    void invalidWhenSourceAndTargetActualTypeArgumentsAreDifferent() throws NoSuchFieldException {
        final var numbersField = InvalidFloatCombinationClass.class.getField("numbers");

        final var actualResult = validator.validate(numbersField, InvalidFloatCombinationClass.InvalidTypeArgumentTargetCombinationClass.class);

        assertThat(actualResult.isValid(), is(equalTo(false)));
        assertThat(actualResult.getField().getName(), is(equalTo("numbers")));
    }

    @CombinationClass(ValidCombinationClass.TargetCombinationClass.class)
    private static class ValidCombinationClass {

        @CombinationProperty(size = 3)
        public Collection<Integer> integers;

        @CombinationProperty(size = 2, name = "inputValues")
        public List<Double> values;

        private static class TargetCombinationClass {

            private Combinations<Integer> integers;

            private Combinations<Double> inputValues;
        }
    }

    @CombinationClass(InvalidSourceStringsCombinationClass.TargetCombinationClass.class)
    private static class InvalidSourceStringsCombinationClass {

        @CombinationProperty(size = 4)
        public List strings;

        private static class TargetCombinationClass {

            private Combinations<String> strings;
        }
    }

    @CombinationClass(InvalidFloatCombinationClass.InvalidTypeArgumentTargetCombinationClass.class)
    private static class InvalidFloatCombinationClass {

        @CombinationProperty(size = 5)
        public List<Float> numbers;

        private static class InvalidTypeArgumentTargetCombinationClass {

            private Combinations<Integer> numbers;
        }
    }
}