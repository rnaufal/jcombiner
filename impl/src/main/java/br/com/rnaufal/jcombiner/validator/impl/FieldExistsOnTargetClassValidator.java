package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.impl.domain.MappedField;
import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;

import java.util.Objects;

/**
 * Created by rnaufal
 */
public class FieldExistsOnTargetClassValidator implements FieldValidator {

    private static final String FIELD_NOT_EXISTS_ON_TARGET_CLASS_ERROR = "Field [%s] not exists on target class!";

    private final FieldValidator nextValidator;

    public FieldExistsOnTargetClassValidator(final FieldValidator nextValidator) {
        this.nextValidator = Objects.requireNonNull(nextValidator);
    }

    @Override
    public <R> FieldValidationResult validate(final MappedField mappedField,
                                              final Class<R> targetClass) {
        return existsFieldOnTargetClass(mappedField, targetClass) ?
                nextValidator.validate(mappedField, targetClass) :
                FieldValidationResult.error(mappedField, getClass());
    }

    @Override
    public void registerTo(final ValidationMessages validationMessages) {
        FieldValidator.super.registerTo(validationMessages);

        nextValidator.registerTo(validationMessages);
    }

    @Override
    public String getErrorMessage() {
        return FIELD_NOT_EXISTS_ON_TARGET_CLASS_ERROR;
    }

    private <R> boolean existsFieldOnTargetClass(final MappedField mappedField,
                                                 final Class<R> targetClass) {
        return getTargetField(mappedField, targetClass) != null;
    }
}
