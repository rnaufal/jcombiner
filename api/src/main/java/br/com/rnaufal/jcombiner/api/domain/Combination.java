package br.com.rnaufal.jcombiner.api.domain;

import java.util.stream.Stream;

/**
 * Created by rafael.naufal
 */
public interface Combination<T> {

    Stream<T> stream();
}