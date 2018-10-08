package br.com.rnaufal.jcombiner;

import br.com.rnaufal.jcombiner.api.domain.Combinations;

import java.util.List;

/**
 * Created by rnaufal
 */
public class TargetCombinationsClass {

    private Combinations<Integer> integers;

    private Combinations<String> stringCombinations;

    private Combinations<String> names;

    private List<Integer> numbers;

    Combinations<Integer> getIntegers() {
        return integers;
    }

    Combinations<String> getStringCombinations() {
        return stringCombinations;
    }

    Combinations<String> getNames() {
        return names;
    }
}
