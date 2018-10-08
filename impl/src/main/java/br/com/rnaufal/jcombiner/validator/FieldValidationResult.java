package br.com.rnaufal.jcombiner.validator;

import br.com.rnaufal.jcombiner.impl.domain.MappedField;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created by rnaufal
 */
public class FieldValidationResult {

    private final MappedField mappedField;

    private final Class<? extends FieldValidator> validatorResultClass;

    private final boolean valid;

    private Field targetField;

    private FieldValidationResult(final MappedField mappedField,
                                  final Class<? extends FieldValidator> validatorResultClass,
                                  final boolean valid) {
        this.mappedField = mappedField;
        this.validatorResultClass = validatorResultClass;
        this.valid = valid;
    }

    public static FieldValidationResult ok(final MappedField mappedField,
                                           final Field targetField,
                                           final Class<? extends FieldValidator> validatorResultClass) {
        final var fieldValidationResult = new FieldValidationResult(mappedField, validatorResultClass, true);
        fieldValidationResult.setTargetField(targetField);
        return fieldValidationResult;
    }

    public static FieldValidationResult error(final MappedField field,
                                              final Class<? extends FieldValidator> validatorResultClass) {
        return new FieldValidationResult(field, validatorResultClass, false);
    }

    public MappedField getMappedField() {
        return mappedField;
    }

    public boolean isValid() {
        return valid;
    }

    public void setTargetField(final Field targetField) {
        this.targetField = targetField;
    }

    public Optional<Field> getTargetField() {
        return Optional.ofNullable(targetField);
    }

    public Class<? extends FieldValidator> getValidatorResultClass() {
        return validatorResultClass;
    }

    public String formatErrorMessage(final String errorMessage) {
        return String.format(errorMessage, mappedField.getField().getName());
    }
}
