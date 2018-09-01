package br.com.rnaufal.jcombiner.impl;

import br.com.rnaufal.jcombiner.impl.domain.CombinationImpl;
import br.com.rnaufal.jcombiner.impl.domain.CombinationsImpl;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.util.List;

/**
 * Created by rnaufal
 */
public class CombinationsArgumentConverter<T> extends SimpleArgumentConverter {

    @Override
    protected Object convert(final Object source,
                             final Class<?> targetType) throws ArgumentConversionException {
        @SuppressWarnings("unchecked") final var expectedCombinations = (List<List<T>>) source;

        return expectedCombinations
                .stream()
                .map(combination -> combination.stream()
                        .collect(CombinationImpl<T>::new,
                                CombinationImpl::add,
                                CombinationImpl::addAll))
                .collect(CombinationsImpl<T>::new,
                        CombinationsImpl::add,
                        CombinationsImpl::addAll);
    }
}
