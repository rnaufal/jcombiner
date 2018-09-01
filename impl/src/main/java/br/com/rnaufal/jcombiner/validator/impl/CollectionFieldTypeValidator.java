package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.message.ValidationMessages;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by rnaufal
 */
public class CollectionFieldTypeValidator implements FieldValidator {

    private static final String FIELD_IS_NOT_ASSIGNABLE_FROM_COLLECTION_ERROR = "Field [%s] is not assignable from Collection!";

    private final FieldValidator nextValidator;

    public CollectionFieldTypeValidator(final FieldValidator nextValidator) {
        this.nextValidator = Objects.requireNonNull(nextValidator);
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

    @Override
    public void registerTo(final ValidationMessages validationMessages) {
        FieldValidator.super.registerTo(validationMessages);

        nextValidator.registerTo(validationMessages);
    }

    @Override
    public String getErrorMessage() {
        return FIELD_IS_NOT_ASSIGNABLE_FROM_COLLECTION_ERROR;
    }
}
