package br.com.rnaufal.jcombiner;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationClassException;
import br.com.rnaufal.jcombiner.impl.combiner.CombinationsGenerator;
import br.com.rnaufal.jcombiner.impl.combiner.IterativeCombinationsGenerator;
import br.com.rnaufal.jcombiner.impl.domain.CombinationClassDescriptor;
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
public class JCombinerImpl implements JCombiner {

    private final CombinationAnnotationParser combinationAnnotationParser;

    public JCombinerImpl() {
        this(new CombinationAnnotationParserImpl());
    }

    JCombinerImpl(final CombinationAnnotationParser combinationAnnotationParser) {
        this.combinationAnnotationParser = combinationAnnotationParser;
    }

    @Override
    public <T, R> R parseCombinations(final T arg) {
        Objects.requireNonNull(arg, "Object source should not be null");

        return combinationAnnotationParser
                .parse(arg)
                .map(this::<R>generateCombinations)
                .orElseThrow(() -> new InvalidCombinationClassException("[" + arg.getClass().getSimpleName() + "] " +
                        "is not a valid Combination class"));
    }

    private <R> R generateCombinations(final CombinationClassDescriptor combinationClassDescriptor) {
        return combinationClassDescriptor.getCombinationFields()
                .stream()
                .map(combinationFieldDescriptor -> generateCombinations(combinationFieldDescriptor, combinationClassDescriptor.getSourceObject()))
                .collect(new TargetInstanceCombinationsCollector<>(combinationClassDescriptor.getResultClass()));
    }

    private Entry<CombinationField, Combinations<?>> generateCombinations(final CombinationField combinationField,
                                                                          final Object sourceObject) {
        final Collection<?> fieldValue = combinationField
                .getSourceFieldValue(sourceObject);
        final CombinationsGenerator<?> combinationsGenerator = new IterativeCombinationsGenerator<>(fieldValue,
                combinationField.getCombinationSize());
        final Combinations<?> combinations = combinationsGenerator.generate();
        return Map.entry(combinationField, combinations);
    }

}
