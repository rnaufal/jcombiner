package br.com.rnaufal.jcombiner.impl.domain;

import br.com.rnaufal.jcombiner.api.domain.Combination;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rnaufal
 */
public class CombinationImpl<T> implements Combination<T> {

    private final List<T> values = Lists.newArrayList();

    public void add(final T value) {
        this.values.add(value);
    }

    public void addAll(final Combination<T> combination) {
        this.values.addAll(combination.stream().collect(Collectors.toUnmodifiableList()));
    }

    public Stream<T> stream() {
        return values.stream();
    }
}