package br.com.rnaufal.jcombiner.api.domain;

import java.util.stream.Stream;

/**
 * Created by rnaufal
 */
public interface Combinations<T> {

    Stream<Combination<T>> stream();

}
