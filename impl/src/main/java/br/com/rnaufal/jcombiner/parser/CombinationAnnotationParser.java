package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.impl.domain.CombinationsDescriptor;

import java.util.Optional;

/**
 * Created by rafael.naufal
 */
public interface CombinationAnnotationParser {

    Optional<CombinationsDescriptor> parse(final Class<?> clazz);
}
