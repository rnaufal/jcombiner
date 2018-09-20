package br.com.rnaufal.jcombiner.api;

/**
 * Created by rnaufal
 */
public interface JCombiner<R> {

    <T> R parseCombinations(T arg, Class<R> targetClass);
}
