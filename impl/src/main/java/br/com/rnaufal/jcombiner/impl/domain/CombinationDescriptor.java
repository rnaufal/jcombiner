package br.com.rnaufal.jcombiner.impl.domain;

import java.lang.reflect.Field;

/**
 * Created by rnaufal
 */
public class CombinationDescriptor {

    private final Field sourceField;

    private final Field targetField;

    private final int combinationSize;

    public CombinationDescriptor(final Field sourceField,
                                 final Field targetField,
                                 final int combinationSize) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.combinationSize = combinationSize;
    }

    public Field getSourceField() {
        return sourceField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public int getCombinationSize() {
        return combinationSize;
    }
}
