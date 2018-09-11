package br.com.rnaufal.jcombiner.domain;

import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.impl.CombinationsArgumentConverter;
import br.com.rnaufal.jcombiner.impl.domain.CombinationImpl;
import br.com.rnaufal.jcombiner.impl.domain.CombinationsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by rnaufal
 */
class CombinationsTest {

    @ParameterizedTest(name = "expectedSize: {0}, values: {1}")
    @MethodSource("stringArgumentsProvider")
    void testStringCombinationsWithDifferentSizes(final long expectedSize,
                                                  @ConvertWith(CombinationsArgumentConverter.class) final Combinations<String> values) {
        assertThat(values.stream().count(), is(equalTo(expectedSize)));
    }

    @Test
    void testCombineTwoStringCombinations() {
        final var combinations = buildCombinationsFrom(of(of("1", "2"), of("2", "3")));
        final var otherCombinations = buildCombinationsFrom(of(of("3", "4"), of("4", "5"), of("5", "6")));

        combinations.addAll(otherCombinations);

        assertThat(combinations.stream().count(), is(equalTo(5L)));
    }

    private CombinationsImpl<String> buildCombinationsFrom(final List<List<String>> values) {
        return values
                .stream()
                .map(combination -> combination.stream()
                        .collect(CombinationImpl<String>::new,
                                CombinationImpl::add,
                                CombinationImpl::addAll))
                .collect(CombinationsImpl<String>::new,
                        CombinationsImpl::add,
                        CombinationsImpl::addAll);
    }

    private static Stream<Arguments> stringArgumentsProvider() {
        return Stream.of(Arguments.of(0L, of()),
                Arguments.of(1L, of(of("1"))),
                Arguments.of(2L, of(of("1", "2"), of("2", "3"))));
    }

}