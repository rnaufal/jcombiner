package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Created by rnaufal
 */
public class CombinationsTargetFieldTypeValidator implements FieldValidator {

    private static final String COMBINATIONS_TARGET_FIELD_TYPE_ERROR = "Target field [%s] type is not Combinations!";

    private final FieldValidator nextValidator;

    public CombinationsTargetFieldTypeValidator(final FieldValidator nextValidator) {
        this.nextValidator = Objects.requireNonNull(nextValidator);
    }

    @Override
    public <R> FieldValidationResult validate(final Field field,
                                              final Class<R> targetClass) {
        final var targetField = getTargetField(field, targetClass);

        return targetField.getType() == Combinations.class ?
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
        return COMBINATIONS_TARGET_FIELD_TYPE_ERROR;
    }
}
