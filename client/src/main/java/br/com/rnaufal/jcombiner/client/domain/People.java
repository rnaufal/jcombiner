package br.com.rnaufal.jcombiner.client.domain;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by rnaufal
 */
public class People {

    @CombinationProperty(size = 3)
    private List<Person> men;

    @CombinationProperty(size = 2, name = "someWomen")
    private List<Person> women;

    public People() {
        this.men = new ArrayList<>();
        this.women = new ArrayList<>();
    }

    public void addMen(final Person person) {
        this.men.add(Objects.requireNonNull(person));
    }

    public void addWoman(final Person person) {
        this.women.add(Objects.requireNonNull(person));
    }
}
