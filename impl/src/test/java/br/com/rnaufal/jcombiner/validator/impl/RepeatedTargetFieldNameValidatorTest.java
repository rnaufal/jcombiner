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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by rnaufal
 */
@ExtendWith(MockitoExtension.class)
class RepeatedTargetFieldNameValidatorTest {

    @Mock
    private FieldValidator nextFieldValidator;

    private RepeatedTargetFieldNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RepeatedTargetFieldNameValidator(nextFieldValidator);
    }

    @Test
    void shouldDelegateToNextValidatorWhenNoRepeatedTargetFieldNamesAreFound() {
        class ValidCombinationClass {

            @CombinationProperty(size = 2, name = "valuesCombinations")
            private List<Character> values;

            @CombinationProperty(size = 3, name = "namesCombinations")
            private List<String> names;

            @CombinationProperty(size = 4, name = "numbersCombinations")
            private List<Integer> numbers;

            class TargetCombinationsClass {

                private Combinations<Character> valuesCombinations;

                private Combinations<String> namesCombinations;

                private Combinations<Integer> numbersCombinations;
            }
        }

        Arrays.stream(ValidCombinationClass.class.getDeclaredFields())
                .map(MappedField::from)
                .filter(MappedField::matchesCombinationProperty)
                .forEach(mappedField -> {
                    validator.validate(mappedField, ValidCombinationClass.TargetCombinationsClass.class);

                    verify(nextFieldValidator).validate(mappedField, ValidCombinationClass.TargetCombinationsClass.class);
                });
    }

    @Test
    void shouldReturnValidatorErrorWhenRepeatedTargetFieldNamesAreFound() {
        final var validationResult = mock(FieldValidationResult.class);
        when(validationResult.isValid()).thenReturn(true);

        when(nextFieldValidator.validate(any(MappedField.class),
                eq(InvalidCombinationClass.InvalidTargetCombinationClass.class))).thenReturn(validationResult);

        validateFields(List.of("values", "integers", "firstNames"), true);
        validateFields(List.of("strings", "numbers", "lastNames"), false);
    }

    private void validateFields(final List<String> fieldNames,
                                final boolean valid) {
        fieldNames.stream()
                .map(fieldName -> MappedField.from(getDeclaredField(InvalidCombinationClass.class, fieldName, true)))
                .map(invalidField -> validator.validate(invalidField, InvalidCombinationClass.InvalidTargetCombinationClass.class))
                .forEach(actualValidationResult -> assertThat(actualValidationResult.isValid(), is(equalTo(valid))));
    }

    private static class InvalidCombinationClass {

        @CombinationProperty(size = 3)
        private List<String> values;

        @CombinationProperty(size = 2, name = "values")
        private List<String> strings;

        @CombinationProperty(size = 2, name = "numbers")
        private List<Integer> integers;

        @CombinationProperty(size = 3)
        private List<Integer> numbers;

        @FourSizeCombinationProperty
        private List<String> firstNames;

        @FourSizeCombinationProperty
        private List<String> lastNames;

        private static class InvalidTargetCombinationClass {

            private Combinations<String> values;

            private Combinations<Integer> numbers;

            private Combinations<String> names;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @CombinationProperty(size = 4, name = "names")
    private @interface FourSizeCombinationProperty {
    }
}