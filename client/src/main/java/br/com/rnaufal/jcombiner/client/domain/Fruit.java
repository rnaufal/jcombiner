package br.com.rnaufal.jcombiner.client.domain;

/**
 * Created by rnaufal
 */
public class Fruit {

    private final String name;

    public Fruit(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fruit {name=" + name + "}";
    }
}
