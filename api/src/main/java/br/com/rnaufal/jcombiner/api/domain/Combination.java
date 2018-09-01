package br.com.rnaufal.jcombiner.api.domain;

import java.util.stream.Stream;

/**
 * Created by rnaufal
 */
public interface Combination<T> {

    Stream<T> stream();
}