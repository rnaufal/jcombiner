package br.com.rnaufal.jcombiner;

import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationClassException;
import br.com.rnaufal.jcombiner.impl.domain.CombinationField;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by rnaufal
 */
public class TargetInstanceCombinationsCollector<R> implements Collector<Map.Entry<CombinationField, Combinations<?>>, R, R> {

    private final Class<?> resultClass;

    TargetInstanceCombinationsCollector(final Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Supplier<R> supplier() {
        return () -> {
            try {
                return (R) resultClass.getConstructor().newInstance();
            } catch (final Exception e) {
                throw new InvalidCombinationClassException("No default construct found for class " +
                        resultClass.getSimpleName(), e);
            }
        };
    }

    @Override
    public BiConsumer<R, Map.Entry<CombinationField, Combinations<?>>> accumulator() {
        return (instance, entry) -> {
            final var combinationField = entry.getKey();
            final Combinations<?> combinations = entry.getValue();
            combinationField.writeTargetFieldValue(instance, combinations);
        };
    }

    @Override
    public BinaryOperator<R> combiner() {
        return (result, partialResult) -> {
            throw new UnsupportedOperationException("nope");
        };
    }

    @Override
    public Function<R, R> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH);
    }
}
