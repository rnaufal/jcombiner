package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.impl.domain.CombinationClass;

/**
 * Created by rnaufal
 */
public interface CombinationAnnotationParser<R> {

    <T> CombinationClass<R> parse(final Class<T> sourceClass,
                                  final Class<R> targetClass);
}
