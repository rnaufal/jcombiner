package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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
    public <R> FieldValidationResult validate(final Field field,
                                              final Class<R> targetClass) {
        return isValid(field) ?
                nextValidator.validate(field, targetClass) :
                FieldValidationResult.error(field, getClass());
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

    private boolean isValid(final Field field) {
        return Collection.class.isAssignableFrom(field.getType()) && hasParameterizedType(field);
    }

    private boolean hasParameterizedType(final Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }
}
