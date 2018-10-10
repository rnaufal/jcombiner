package br.com.rnaufal.jcombiner.client.service;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.client.domain.FruitCombinations;
import br.com.rnaufal.jcombiner.client.domain.Fruits;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Created by rnaufal
 */
public class FruitCombinationService {

    private final JCombiner<Fruits, FruitCombinations> jcombiner;

    @SuppressWarnings("unchecked")
    public FruitCombinationService(final ServiceLoader<JCombiner> jCombiners) {
        this.jcombiner = Objects.requireNonNull(jCombiners)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No JCombiner service found"));
    }

    public FruitCombinations from(final Fruits fruits) {
        return jcombiner.parseCombinations(fruits, FruitCombinations.class);
    }
}
