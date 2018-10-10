package br.com.rnaufal.jcombiner.client.domain;

import br.com.rnaufal.jcombiner.client.annotation.TwoSizeCombination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by rnaufal
 */
public class Fruits {

    @TwoSizeCombination
    private Collection<Fruit> red;

    @TwoSizeCombination
    private Collection<Fruit> yellow;

    @TwoSizeCombination
    private Collection<Fruit> green;

    public Fruits() {
        this.red = new ArrayList<>();
        this.yellow = new ArrayList<>();
        this.green = new ArrayList<>();
    }

    public void addRed(final Fruit fruit) {
        this.red.add(Objects.requireNonNull(fruit));
    }

    public void addYellow(final Fruit fruit) {
        this.yellow.add(Objects.requireNonNull(fruit));
    }

    public void addGreen(final Fruit fruit) {
        this.green.add(Objects.requireNonNull(fruit));
    }
}
