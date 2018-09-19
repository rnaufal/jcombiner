package br.com.rnaufal.jcombiner.impl.domain;

import java.util.List;

/**
 * Created by rnaufal
 */
public class CombinationClassDescriptor {

    private final Object sourceObject;

    private final Class<?> resultClass;

    private final List<CombinationField> combinationFields;

    public CombinationClassDescriptor(final Object sourceObject,
                                      final Class<?> resultClass,
                                      final List<CombinationField> combinationFields) {
        this.sourceObject = sourceObject;
        this.resultClass = resultClass;
        this.combinationFields = combinationFields;
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public List<CombinationField> getCombinationFields() {
        return combinationFields;
    }

    public Object getSourceObject() {
        return sourceObject;
    }
}

