package br.com.rnaufal.jcombiner.domain;

import br.com.rnaufal.jcombiner.impl.domain.CombinationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by rnaufal
 */
class CombinationTest {

    @ParameterizedTest(name = "expectedSize: {0}, values: {1}")
    @MethodSource("integerArgumentsProvider")
    void testIntegerCombinationWithDifferentSizes(final long expectedSize,
                                                  final List<Integer> values) {
        final var combination = buildCombinationFrom(values);

        assertThat(combination.stream().count(), is(equalTo(expectedSize)));
    }

    @Test
    void testCombineTwoIntegerCombinations() {
        final var combination = buildCombinationFrom(List.of(1, 2, 3));
        final var otherCombination = buildCombinationFrom(List.of(4, 5, 6));

        combination.addAll(otherCombination);

        assertThat(combination.stream().count(), is(equalTo(6L)));

        final var expected = List.of(1, 2, 3, 4, 5, 6);
        assertThat(combination.stream().collect(Collectors.toList()),is(equalTo(expected)));
        assertThat(combination.toString(), is(equalTo(Objects.toString(expected))));
    }

    private CombinationImpl<Integer> buildCombinationFrom(final List<Integer> values) {
        return values
                .stream()
                .collect(CombinationImpl<Integer>::new,
                        CombinationImpl::add,
                        CombinationImpl::addAll);
    }

    private static Stream<Arguments> integerArgumentsProvider() {
        return Stream.of(Arguments.of(0L, Collections.emptyList()),
                Arguments.of(1L, List.of(1)),
                Arguments.of(2L, List.of(1, 2)));
    }
}