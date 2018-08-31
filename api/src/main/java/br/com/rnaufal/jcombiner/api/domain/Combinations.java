package br.com.rnaufal.jcombiner.api.domain;

import java.util.stream.Stream;

/**
 * Created by rafael.naufal
 */
public interface Combinations<T> {

    Stream<Combination<T>> stream();

}
