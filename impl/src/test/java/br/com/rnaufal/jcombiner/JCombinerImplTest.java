package br.com.rnaufal.jcombiner;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.api.annotation.CombinationClass;
import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combination;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationClassException;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import br.com.rnaufal.jcombiner.impl.domain.CombinationClassDescriptor;
import br.com.rnaufal.jcombiner.impl.domain.CombinationField;
import br.com.rnaufal.jcombiner.parser.CombinationAnnotationParser;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by rnaufal
 */
class JCombinerImplTest {

    @Test
    void shouldGenerateCombinationsForEachFieldSuccessfully() {
        @CombinationClass(TargetCombinationsClass.class)
        class CombinationsClass {

            @CombinationProperty(size = 2)
            private List<Integer> integers = List.of(0, 1, 2, 3);

            @CombinationProperty(size = 3, name = "stringCombinations")
            private Set<String> strings = Sets.newLinkedHashSet(List.of("a", "b", "c", "d"));
        }

        final JCombiner jCombiner = new JCombinerImpl();
        final var result = (TargetCombinationsClass) jCombiner.parseCombinations(new CombinationsClass());

        final var integerCombinations = combinationsToNestedList(result.getIntegers());
        assertThat(integerCombinations, hasSize(6));
        assertThat(integerCombinations, is(equalTo(List.of(List.of(0, 1), List.of(0, 2), List.of(0, 3),
                List.of(1, 2), List.of(1, 3), List.of(2, 3)))));

        final var stringCombinations = combinationsToNestedList(result.getStringCombinations());
        assertThat(stringCombinations, hasSize(4));
        assertThat(stringCombinations, is(equalTo(List.of(List.of("a", "b", "c"), List.of("a", "b", "d"),
                List.of("a", "c", "d"), List.of("b", "c", "d")))));
    }

    @Test
    void throwExceptionWhenAnErrorWhileCreatingTargetClassInstance() {
        @CombinationClass(StringCombinationClass.TargetStringCombinationClass.class)
        class StringCombinationClass {

            @CombinationProperty(size = 4)
            private List<String> strings = List.of("aa", "bb", "cc");

            class TargetStringCombinationClass {

                private Combinations<String> strings;
            }
        }

        final JCombiner jCombiner = new JCombinerImpl();
        assertThrows(InvalidCombinationClassException.class, () -> jCombiner.parseCombinations(new StringCombinationClass()));
    }

    @Test
    void throwExceptionWhenNoCombinationClassDescriptorIsGenerated() {
        final var combinationAnnotationParser = mock(CombinationAnnotationParser.class);
        when(combinationAnnotationParser.parse(Object.class)).thenReturn(Optional.empty());

        final JCombiner jCombiner = new JCombinerImpl(combinationAnnotationParser);

        assertThrows(InvalidCombinationClassException.class, () -> jCombiner.parseCombinations(Object.class));
    }

    @Test
    void throwExceptionWhileTryingToGetFieldValueFromSourceObject() {
        final var descriptor = new CombinationField(getNumbersSourceField(), getNumbersTargetField(), 5);

        final var classDescriptor = mock(CombinationClassDescriptor.class);
        when(classDescriptor.getCombinationFields()).thenReturn(List.of(descriptor));
        when(classDescriptor.getSourceObject()).thenReturn(new Object());
        Mockito.<Class<?>>when(classDescriptor.getResultClass()).thenReturn(TargetCombinationsClass.class);


        final var combinationAnnotationParser = mock(CombinationAnnotationParser.class);
        final var numbersCombination = new NumbersCombination();
        when(combinationAnnotationParser.parse(eq(numbersCombination))).thenReturn(Optional.of(classDescriptor));

        final JCombiner jCombiner = new JCombinerImpl(combinationAnnotationParser);
        assertThrows(InvalidCombinationFieldException.class, () -> jCombiner.parseCombinations(numbersCombination));
    }

    @Test
    void shouldThrowExceptionWhenTryingToWriteValueOnTargetField() {
        final var numbersCombination = new NumbersCombination();

        final var descriptor = new CombinationField(getNumbersSourceField(), getNumbersTargetField(), 5);

        final var classDescriptor = mock(CombinationClassDescriptor.class);
        when(classDescriptor.getCombinationFields()).thenReturn(List.of(descriptor));
        when(classDescriptor.getSourceObject()).thenReturn(numbersCombination);
        Mockito.<Class<?>>when(classDescriptor.getResultClass()).thenReturn(TargetCombinationsClass.class);

        final var combinationAnnotationParser = mock(CombinationAnnotationParser.class);
        when(combinationAnnotationParser.parse(eq(numbersCombination))).thenReturn(Optional.of(classDescriptor));

        final JCombiner jCombiner = new JCombinerImpl(combinationAnnotationParser);
        assertThrows(InvalidCombinationFieldException.class, () -> jCombiner.parseCombinations(numbersCombination));
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

    @CombinationClass(TargetCombinationsClass.class)
    private class NumbersCombination {

        @CombinationProperty(size = 2)
        private List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    }
}