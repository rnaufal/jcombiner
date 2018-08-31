package br.com.rnaufal.jcombiner.impl.domain;

import java.util.Collections;
import java.util.Map;

/**
 * Created by rafael.naufal
 */
public class CombinationsDescriptor {

    private final Class<?> resultClass;

    private final Map<String, CombinationDescriptor> fieldDescriptorsByName;

    public CombinationsDescriptor(final Class<?> resultClass,
                                  final Map<String, CombinationDescriptor> fieldDescriptorsByName) {
        this.resultClass = resultClass;
        this.fieldDescriptorsByName = Collections.unmodifiableMap(fieldDescriptorsByName);
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public Map<String, CombinationDescriptor> getFieldDescriptorsByName() {
        return fieldDescriptorsByName;
    }
}

