package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.impl.domain.MappedField;
import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;
import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

/**
 * Created by rnaufal
 */
public class RepeatedTargetFieldNameValidator implements FieldValidator {

    private static final String REPEATED_TARGET_FIELD_NAME_ERROR = "Field [%s] has target field name already mapped by other field!";

    private FieldValidator nextValidator;

    private Set<String> targetFieldNames;

    public RepeatedTargetFieldNameValidator(final FieldValidator nextValidator) {
        this.nextValidator = Objects.requireNonNull(nextValidator);
        this.targetFieldNames = Sets.newHashSet();
    }

    @Override
    public <R> FieldValidationResult validate(final MappedField mappedField,
                                              final Class<R> targetClass) {
        if (targetFieldNames.add(mappedField.getTargetFieldName())) {
            return nextValidator.validate(mappedField, targetClass);
        } else {
            return FieldValidationResult.error(mappedField, getClass());
        }
    }

    @Override
    public void registerTo(final ValidationMessages validationMessages) {
        FieldValidator.super.registerTo(validationMessages);

        nextValidator.registerTo(validationMessages);
    }

    @Override
    public String getErrorMessage() {
        return REPEATED_TARGET_FIELD_NAME_ERROR;
    }
}
