package br.com.rnaufal.jcombiner.impl.combiner;

import br.com.rnaufal.jcombiner.api.domain.Combinations;

/**
 * Created by rafael.naufal
 */
public interface CombinationsGenerator<T> {

    Combinations<T> generate();
}
