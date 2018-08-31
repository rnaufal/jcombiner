package br.com.rnaufal.jcombiner.impl.combiner;

import br.com.rnaufal.jcombiner.api.domain.Combination;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.impl.domain.CombinationImpl;
import br.com.rnaufal.jcombiner.impl.domain.CombinationsImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Created by rafael.naufal
 */
public class IterativeCombinationsGenerator<T> implements CombinationsGenerator<T> {

    private final int combinationSize;

    private final Deque<Integer> indexes;

    private final List<Map.Entry<Integer, List<Integer>>> successorsByIndex;

    private final List<T> items;

    private int successorIdx;

    private int nextSuccessorIndex;

    public IterativeCombinationsGenerator(final List<T> items,
                                          final int combinationSize) {
        this.items = items;
        this.combinationSize = combinationSize;
        this.indexes = Queues.newArrayDeque();
        this.successorsByIndex = Lists.newArrayList();
        buildSuccessors(items);
    }

    public Combinations<T> generate() {

        if (combinationSize == 0) {
            return new CombinationsImpl<>();
        }

        final var indexCombinations = new CombinationsImpl<Integer>();

        for (var index = 0; index < successorsByIndex.size(); index++) {

            indexes.push(successorsByIndex.get(index).getKey());

            while (!indexes.isEmpty()) {

                if (indexes.size() == combinationSize) {
                    indexCombinations.add(buildIndexCombination());
                    indexes.pop();
                    continue;
                }

                computeSuccessors(indexCombinations, index);
            }
        }

        return indexCombinations
                .stream()
                .map(combination -> combination.stream()
                        .map(items::get)
                        .collect(CombinationImpl<T>::new,
                                CombinationImpl::add,
                                CombinationImpl::addAll))
                .collect(CombinationsImpl<T>::new,
                        CombinationsImpl::add,
                        CombinationsImpl::addAll);
    }

    private void computeSuccessors(final CombinationsImpl<Integer> indexCombinations,
                                   final int index) {
        final var successors = successorsByIndex.get(successorIdx).getValue();

        for (var successorIndex = nextSuccessorIndex; successorIndex < successors.size(); successorIndex++) {
            indexes.push(successors.get(successorIndex));

            if (indexes.size() == combinationSize) {
                successorIdx = indexes.getFirst();
                nextSuccessorIndex = 0;

                indexCombinations.add(buildIndexCombination());
                indexes.pop();
            }
        }

        if (indexes.getFirst() == index) {
            successorIdx = indexes.pop() + 1;
            nextSuccessorIndex = 0;
        } else {
            successorIdx = index;
            nextSuccessorIndex = indexes.pop() - index;
        }
    }

    private Combination<Integer> buildIndexCombination() {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(indexes.descendingIterator(),
                        Spliterator.ORDERED), false)
                .collect(CombinationImpl<Integer>::new,
                        CombinationImpl::add,
                        CombinationImpl::addAll);
    }

    private void buildSuccessors(final List<T> items) {
        for (var i = 0; i < items.size(); i++) {
            final var successors = IntStream
                    .range(i + 1, items.size())
                    .boxed()
                    .collect(Collectors.toUnmodifiableList());
            successorsByIndex.add(Map.entry(i, successors));
        }
    }
}
