package br.com.rnaufal.jcombiner.impl;

import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.impl.combiner.CombinationsGenerator;
import br.com.rnaufal.jcombiner.impl.combiner.IterativeCombinationsGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by rnaufal
 */
class IterativeCombinationsGeneratorTest {

    @ParameterizedTest(name = "combinationSize: {0}, inputValues: {1}")
    @MethodSource("combinationArguments")
    @DisplayName("Should generate string list combinations")
    void testStringListCombinations(final int inputSize,
                                    final List<String> inputValues,
                                    @ConvertWith(CombinationsArgumentConverter.class) final Combinations<String> expectedCombinations) {
        final CombinationsGenerator<String> combinationsGenerator = new IterativeCombinationsGenerator<>(inputValues, inputSize);
        final var actualCombinations = combinationsGenerator.generate();
        assertNotNull(actualCombinations);
        assertCombinationsAreEquals(actualCombinations, expectedCombinations);
    }

    private void assertCombinationsAreEquals(final Combinations<String> actualCombinations,
                                             final Combinations<String> expectedCombinations) {
        final var actualCombinationsIt = actualCombinations.stream().collect(Collectors.toList());
        final var expectedCombinationsIt = expectedCombinations.stream().collect(Collectors.toList());
        assertThat(actualCombinationsIt.size(), is(equalTo(expectedCombinationsIt.size())));

        for (var i = 0; i < actualCombinationsIt.size(); i++) {
            final var actualCombination = actualCombinationsIt.get(i).stream().collect(Collectors.toList());
            final var expectedCombination = expectedCombinationsIt.get(i).stream().collect(Collectors.toList());

            assertThat(actualCombination.size(), is(equalTo(expectedCombination.size())));

            for (var index = 0; index < actualCombination.size(); index++) {
                assertThat(actualCombination.get(index), is(equalTo(expectedCombination.get(index))));
            }
        }
    }

    private static Stream<Arguments> combinationArguments() {
        return Stream.of(zeroSizeCombinations(), oneSizeCombinations(), twoSizeCombinations(), threeSizeCombinations());
    }

    private static Arguments twoSizeCombinations() {
        return Arguments.of(2, of("a", "b", "c", "d", "e", "f"), of(of("a", "b"), of("a", "c"), of("a", "d"),
                of("a", "e"), of("a", "f"), of("b", "c"), of("b", "d"), of("b", "e"), of("b", "f"), of("c", "d"),
                of("c", "e"), of("c", "f"), of("d", "e"), of("d", "f"), of("e", "f")));
    }

    private static Arguments threeSizeCombinations() {
        return Arguments.of(3, of("a", "b", "c", "d", "e", "f"), of(of("a", "b", "c"), of("a", "b", "d"), of("a", "b", "e"),
                of("a", "b", "f"), of("a", "c", "d"), of("a", "c", "e"), of("a", "c", "f"), of("a", "d", "e"), of("a", "d", "f"), of("a", "e", "f"),
                of("b", "c", "d"), of("b", "c", "e"), of("b", "c", "f"), of("b", "d", "e"), of("b", "d", "f"), of("b", "e", "f"),
                of("c", "d", "e"), of("c", "d", "f"), of("c", "e", "f"), of("d", "e", "f")));
    }

    private static Arguments oneSizeCombinations() {
        return Arguments.of(1, of("a", "b", "c", "d", "e", "f"),
                of(of("a"), of("b"), of("c"), of("d"), of("e"), of("f")));
    }

    private static Arguments zeroSizeCombinations() {
        return Arguments.of(0, of("a", "b", "c", "d", "e", "f"), Collections.emptyList());
    }

}