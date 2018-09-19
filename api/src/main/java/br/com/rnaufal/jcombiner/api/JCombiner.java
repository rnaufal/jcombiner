package br.com.rnaufal.jcombiner.api;

/**
 * Created by rnaufal
 */
public interface JCombiner {

    <T, R> R parseCombinations(T arg);
}
