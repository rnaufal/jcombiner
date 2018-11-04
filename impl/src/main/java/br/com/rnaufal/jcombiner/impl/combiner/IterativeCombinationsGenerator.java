package br.com.rnaufal.jcombiner.impl.combiner;

import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.impl.domain.CombinationImpl;
import br.com.rnaufal.jcombiner.impl.domain.CombinationsImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Created by rnaufal
 */
public class IterativeCombinationsGenerator<T> implements CombinationsGenerator<T> {

    private final List<T> items;

    private final int combinationSize;

    public IterativeCombinationsGenerator(final Collection<T> items,
                                          final int combinationSize) {
        this.items = List.copyOf(Objects.requireNonNull(items));
        this.combinationSize = combinationSize;
    }

    public Combinations<T> generate() {
        return buildIndexCombinations()
                .stream()
                .map(combination -> combination
                        .stream()
                        .map(items::get)
                        .collect(CombinationImpl<T>::new,
                                CombinationImpl::add,
                                CombinationImpl::addAll))
                .collect(CombinationsImpl<T>::new,
                        CombinationsImpl::add,
                        CombinationsImpl::addAll);
    }

    private List<List<Integer>> buildIndexCombinations() {
        final Deque<Integer> indexes = Queues.newArrayDeque();
        final List<List<Integer>> indexCombinations = Lists.newArrayList();

        final var size = items.size();
        for (var index = 0; index < size; index++) {
            indexes.push(index);

            var nextIndex = index + 1;
            while (!indexes.isEmpty()) {
                while (nextIndex < size && indexes.size() < combinationSize) {
                    indexes.push(nextIndex++);
                }
                if (indexes.size() == combinationSize) {
                    indexCombinations.add(buildIndexCombination(indexes));
                }
                nextIndex = indexes.pop() + 1;
            }
        }
        return indexCombinations;
    }

    private List<Integer> buildIndexCombination(final Deque<Integer> indexes) {
        final List<Integer> combination = Lists.newArrayList();
        final var iterator = indexes.descendingIterator();
        while (iterator.hasNext()) {
            combination.add(iterator.next());
        }
        return combination;
    }
}
