package br.com.rnaufal.jcombiner.validator;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created by rnaufal
 */
public class FieldValidationResult {

    private final Field field;

    private final Class<? extends FieldValidator> validatorResultClass;

    private final boolean valid;

    private Field targetField;

    private FieldValidationResult(final Field field,
                                  final Class<? extends FieldValidator> validatorResultClass,
                                  final boolean valid) {
        this.field = field;
        this.validatorResultClass = validatorResultClass;
        this.valid = valid;
    }

    public static FieldValidationResult ok(final Field field,
                                           final Field targetField,
                                           final Class<? extends FieldValidator> validatorResultClass) {
        final var fieldValidationResult = new FieldValidationResult(field, validatorResultClass, true);
        fieldValidationResult.setTargetField(targetField);
        return fieldValidationResult;
    }

    public static FieldValidationResult error(final Field field,
                                              final Class<? extends FieldValidator> validatorResultClass) {
        return new FieldValidationResult(field, validatorResultClass, false);
    }

    public Field getField() {
        return field;
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
}
