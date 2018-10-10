package br.com.rnaufal.jcombiner.client.domain;

import br.com.rnaufal.jcombiner.api.domain.Combinations;

import java.util.StringJoiner;

/**
 * Created by rnaufal
 */
public class FruitCombinations {

    private Combinations<Fruit> red;

    private Combinations<Fruit> yellow;

    private Combinations<Fruit> green;

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("red=" + red)
                .add("yellow=" + yellow)
                .add("green=" + green)
                .toString();
    }
}
