package br.com.rnaufal.jcombiner.impl.domain;

import java.util.List;

/**
 * Created by rnaufal
 */
public class CombinationClass<R> {

    private final Class<R> resultClass;

    private final List<CombinationField> combinationFields;

    public CombinationClass(final Class<R> resultClass,
                            final List<CombinationField> combinationFields) {
        this.resultClass = resultClass;
        this.combinationFields = combinationFields;
    }

    public Class<R> getResultClass() {
        return resultClass;
    }

    public List<CombinationField> getCombinationFields() {
        return combinationFields;
    }

}

