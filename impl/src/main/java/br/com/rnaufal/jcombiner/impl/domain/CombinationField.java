package br.com.rnaufal.jcombiner.impl.domain;

import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;

/**
 * Created by rnaufal
 */
public class CombinationField {

    private final Field sourceField;

    private final Field targetField;

    private final int combinationSize;

    public CombinationField(final Field sourceField,
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

    public Collection<?> getSourceFieldValue(final Object sourceObject) {
        try {
            return (Collection<?>) readField(sourceField, sourceObject, true);
        } catch (Exception e) {
            throw new InvalidCombinationFieldException("Error while generating combinations for field " +
                    sourceField.getName(), e);
        }
    }

    public <R> void writeTargetFieldValue(final R instance,
                                          final Combinations<?> combinations) {
        try {
            writeField(targetField, instance, combinations, true);
        } catch (Exception e) {
            throw new InvalidCombinationFieldException("Error while generating combinations for field " +
                    sourceField.getName(), e);
        }
    }
}