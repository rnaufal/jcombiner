package br.com.rnaufal.jcombiner;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combination;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationClassException;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import br.com.rnaufal.jcombiner.impl.domain.CombinationClass;
import br.com.rnaufal.jcombiner.impl.domain.CombinationField;
import br.com.rnaufal.jcombiner.parser.CombinationAnnotationParser;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by rnaufal
 */
@ExtendWith(MockitoExtension.class)
class JCombinerImplTest {

    @Mock
    private CombinationAnnotationParser<TargetCombinationsClass> annotationParser;

    @Mock
    private CombinationClass<TargetCombinationsClass> combinationClass;

    @Test
    void shouldGenerateCombinationsForEachFieldSuccessfully() {
        class CombinationsClass {

            @CombinationProperty(size = 2)
            private List<Integer> integers = List.of(0, 1, 2, 3);

            @CombinationProperty(size = 3, name = "stringCombinations")
            private Set<String> strings = Sets.newLinkedHashSet(List.of("a", "b", "c", "d"));
        }

        final var jCombiner = new JCombinerImpl<CombinationsClass, TargetCombinationsClass>();
        final var result = jCombiner.parseCombinations(new CombinationsClass(), TargetCombinationsClass.class);

        final var integerCombinations = combinationsToNestedList(result.getIntegers());
        assertThat(integerCombinations, hasSize(6));
        assertThat(integerCombinations, is(equalTo(List.of(List.of(0, 1), List.of(0, 2), List.of(0, 3), List.of(1, 2),
                List.of(1, 3), List.of(2, 3)))));

        final var stringCombinations = combinationsToNestedList(result.getStringCombinations());
        assertThat(stringCombinations, hasSize(4));
        assertThat(stringCombinations, is(equalTo(List.of(List.of("a", "b", "c"), List.of("a", "b", "d"),
                List.of("a", "c", "d"), List.of("b", "c", "d")))));
    }

    @Test
    void shouldGenerateCombinationsUsingCustomAnnotations() {
        class OtherCombinationClass {

            @TwoSizeCombinationProperty
            private List<Integer> integers = List.of(0, 1, 2, 3, 4);

            @TwoSizeCombinationProperty
            private List<String> names = List.of("Paul", "Mary", "John", "Joe");
        }

        final var jCombiner = new JCombinerImpl<OtherCombinationClass, TargetCombinationsClass>();
        final var result = jCombiner.parseCombinations(new OtherCombinationClass(), TargetCombinationsClass.class);

        final var integerCombinations = combinationsToNestedList(result.getIntegers());
        assertThat(integerCombinations, hasSize(10));
        assertThat(integerCombinations, is(equalTo(List.of(List.of(0, 1), List.of(0, 2), List.of(0, 3), List.of(0, 4),
                List.of(1, 2), List.of(1, 3), List.of(1, 4), List.of(2, 3), List.of(2, 4), List.of(3, 4)))));

        final var stringCombinations = combinationsToNestedList(result.getNames());
        assertThat(stringCombinations, hasSize(6));
        assertThat(stringCombinations, is(equalTo(List.of(List.of("Paul", "Mary"), List.of("Paul", "John"),
                List.of("Paul", "Joe"), List.of("Mary", "John"), List.of("Mary", "Joe"), List.of("John", "Joe")))));
    }

    @Test
    void throwExceptionWhenAnErrorWhileCreatingTargetClassInstance() {
        class StringCombinationClass {

            @CombinationProperty(size = 4)
            private List<String> strings = List.of("aa", "bb", "cc");

            class TargetStringCombinationClass {

                private Combinations<String> strings;
            }
        }

        final var jCombiner = new JCombinerImpl<StringCombinationClass, StringCombinationClass.TargetStringCombinationClass>();
        assertThrows(InvalidCombinationClassException.class, () -> jCombiner.parseCombinations(new StringCombinationClass(),
                StringCombinationClass.TargetStringCombinationClass.class));
    }

    @Test
    void throwExceptionWhileTryingToGetFieldValueFromSourceObject() {
        final var combinationField = new CombinationField(getNumbersSourceField(), getNumbersTargetField(), 5);

        when(combinationClass.getCombinationFields()).thenReturn(List.of(combinationField));
        when(annotationParser.parse(any(), eq(TargetCombinationsClass.class))).thenReturn(combinationClass);

        final JCombiner<Object, TargetCombinationsClass> jCombiner = new JCombinerImpl<>(annotationParser);
        assertThrows(InvalidCombinationFieldException.class, () -> jCombiner.parseCombinations(new Object(), TargetCombinationsClass.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToWriteValueOnTargetField() {
        final var numbersCombination = new NumbersCombination();

        final var combinationField = new CombinationField(getNumbersSourceField(), getNumbersTargetField(), 5);

        when(combinationClass.getCombinationFields()).thenReturn(List.of(combinationField));
        when(annotationParser.parse(eq(NumbersCombination.class), eq(TargetCombinationsClass.class))).thenReturn(combinationClass);

        final JCombiner<NumbersCombination, TargetCombinationsClass> jCombiner = new JCombinerImpl<>(annotationParser);
        assertThrows(InvalidCombinationFieldException.class, () -> jCombiner.parseCombinations(numbersCombination, TargetCombinationsClass.class));
    }

    private Field getNumbersTargetField() {
        return getDeclaredField(TargetCombinationsClass.class, "numbers", true);
    }

    private Field getNumbersSourceField() {
        return getDeclaredField(NumbersCombination.class, "numbers", true);
    }

    private <T> List<List<T>> combinationsToNestedList(final Combinations<T> combinations) {
        return combinations
                .stream()
                .map(Combination::stream)
                .map(combinationStream -> combinationStream.collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private class NumbersCombination {

        @CombinationProperty(size = 2)
        private List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @CombinationProperty(size = 2)
    private @interface TwoSizeCombinationProperty {

    }
}