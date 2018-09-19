package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.impl.domain.CombinationClassDescriptor;

import java.util.Optional;

/**
 * Created by rnaufal
 */
public interface CombinationAnnotationParser {

    <T> Optional<CombinationClassDescriptor> parse(final T object);
}
