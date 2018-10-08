package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.impl.domain.MappedField;
import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;

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
    public <R> FieldValidationResult validate(final MappedField mappedField,
                                              final Class<R> targetClass) {
        return mappedField.isCollection() ?
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
        return FIELD_IS_NOT_ASSIGNABLE_FROM_COLLECTION_ERROR;
    }

}
