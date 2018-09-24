package br.com.rnaufal.jcombiner.client.domain;

import br.com.rnaufal.jcombiner.api.domain.Combinations;

import java.util.StringJoiner;

/**
 * Created by rnaufal
 */
public class PeopleCombinations {

    private Combinations<Person> men;

    private Combinations<Person> someWomen;

    @Override
    public String toString() {
        return new StringJoiner(",")
                .add("men=" + men)
                .add("someWomen=" + someWomen)
                .toString();
    }
}
