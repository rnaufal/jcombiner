package br.com.rnaufal.jcombiner.impl.combiner;

import br.com.rnaufal.jcombiner.api.domain.Combinations;

/**
 * Created by rnaufal
 */
public interface CombinationsGenerator<T> {

    Combinations<T> generate();
}
