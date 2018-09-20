package br.com.rnaufal.jcombiner;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.impl.combiner.CombinationsGenerator;
import br.com.rnaufal.jcombiner.impl.combiner.IterativeCombinationsGenerator;
import br.com.rnaufal.jcombiner.impl.domain.CombinationField;
import br.com.rnaufal.jcombiner.parser.CombinationAnnotationParser;
import br.com.rnaufal.jcombiner.parser.CombinationAnnotationParserImpl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Created by rnaufal
 */
public class JCombinerImpl<T, R> implements JCombiner<T, R> {

    private final CombinationAnnotationParser<R> combinationAnnotationParser;

    public JCombinerImpl() {
        this(new CombinationAnnotationParserImpl<>());
    }

    JCombinerImpl(final CombinationAnnotationParser<R> combinationAnnotationParser) {
        this.combinationAnnotationParser = combinationAnnotationParser;
    }

    @Override
    public R parseCombinations(final T arg, final Class<R> targetClass) {
        Objects.requireNonNull(arg, "Object source should not be null");

        return combinationAnnotationParser
                .parse(arg.getClass(), targetClass)
                .getCombinationFields()
                .stream()
                .map(combinationField -> generateCombinations(combinationField, arg))
                .collect(new TargetInstanceCombinationsCollector<>(targetClass));
    }

    private Entry<CombinationField, Combinations<?>> generateCombinations(final CombinationField combinationField,
                                                                          final T sourceObject) {
        final Collection<?> fieldValue = combinationField
                .getSourceFieldValue(sourceObject);
        final CombinationsGenerator<?> combinationsGenerator = new IterativeCombinationsGenerator<>(fieldValue,
                combinationField.getCombinationSize());
        final Combinations<?> combinations = combinationsGenerator.generate();
        return Map.entry(combinationField, combinations);
    }
}
