package br.com.rnaufal.jcombiner.impl.domain;

import br.com.rnaufal.jcombiner.api.domain.Combination;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rnaufal
 */
public class CombinationsImpl<T> implements Combinations<T> {

    private final List<Combination<T>> values = Lists.newArrayList();

    public void add(final Combination<T> combination) {
        this.values.add(combination);
    }

    public void addAll(final Combinations<T> combinations) {
        this.values.addAll(combinations.stream().collect(Collectors.toUnmodifiableList()));
    }

    @Override
    public Stream<Combination<T>> stream() {
        return values.stream();
    }

    @Override
    public String toString() {
        return Objects.toString(values);
    }
}
