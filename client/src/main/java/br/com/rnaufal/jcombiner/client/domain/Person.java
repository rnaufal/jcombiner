package br.com.rnaufal.jcombiner.client.domain;

/**
 * Created by rnaufal
 */
public class Person {

    private final String name;

    public Person(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{name=" + name + "}";
    }
}
