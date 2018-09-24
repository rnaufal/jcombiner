package br.com.rnaufal.jcombiner.client.service;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.client.domain.People;
import br.com.rnaufal.jcombiner.client.domain.PeopleCombinations;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Created by rnaufal
 */
public class PeopleCombinationService {

    private final JCombiner<People, PeopleCombinations> jcombiner;

    @SuppressWarnings("unchecked")
    public PeopleCombinationService(final ServiceLoader<JCombiner> jCombiners) {
        this.jcombiner = Objects.requireNonNull(jCombiners)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No JCombiner service found"));
    }

    public PeopleCombinations from(final People people) {
        return jcombiner.parseCombinations(people, PeopleCombinations.class);
    }
}
