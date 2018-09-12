package br.com.rnaufal.jcombiner.impl.domain;

import java.util.List;

/**
 * Created by rnaufal
 */
public class CombinationsDescriptor {

    private final Class<?> resultClass;

    private final List<CombinationDescriptor> fieldDescriptorsByName;

    public CombinationsDescriptor(final Class<?> resultClass,
                                  final List<CombinationDescriptor> fieldDescriptors) {
        this.resultClass = resultClass;
        this.fieldDescriptorsByName = fieldDescriptors;
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public List<CombinationDescriptor> getFieldDescriptors() {
        return fieldDescriptorsByName;
    }
}

