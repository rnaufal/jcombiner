package br.com.rnaufal.jcombiner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by rnaufal
 */
class TargetInstanceCombinationsCollectorTest {

    private TargetInstanceCombinationsCollector<Object> targetInstanceCombinationsCollector;

    @BeforeEach
    void setUp() {
        targetInstanceCombinationsCollector = new TargetInstanceCombinationsCollector<>(Object.class);
    }

    @Test
    void throwExceptionWhenCombinerIsInvoked() {
        final var combiner = targetInstanceCombinationsCollector.combiner();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> combiner.apply(new Object() , new Object()));
    }

    @Test
    void shouldReturnIdentityFunctionWhenFinishedFunctionIsInvoked() {
        final var finisher = targetInstanceCombinationsCollector.finisher();

        final var expected = new Object();
        final var actual = finisher.apply(expected);

        assertThat(actual, is(equalTo(expected)));
    }
}