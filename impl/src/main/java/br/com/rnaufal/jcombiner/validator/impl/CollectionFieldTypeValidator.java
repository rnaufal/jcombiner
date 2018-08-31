package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.validator.FieldValidator;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by rafael.naufal
 */
public class CollectionFieldTypeValidator implements FieldValidator {

    private final FieldValidator nextValidator;

    public CollectionFieldTypeValidator(final FieldValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    @Override
    public FieldValidationResult validate(final Field field,
                                    final Class<?> targetClass) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            return nextValidator.validate(field, targetClass);
        } else {
            return FieldValidationResult.error(field, getClass());
        }
    }
}
