package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.validator.FieldValidator;

import java.lang.reflect.Field;

/**
 * Created by rafael.naufal
 */
public class FieldValidationResult {

    private final Field field;

    private final Class<? extends FieldValidator> validatorResultClass;

    private final boolean valid;

    private FieldValidationResult(final Field field,
                                  final Class<? extends FieldValidator> validatorResultClass,
                                  final boolean valid) {
        this.field = field;
        this.validatorResultClass = validatorResultClass;
        this.valid = valid;
    }

    public static FieldValidationResult ok(final Field field,
                                           final Class<? extends FieldValidator> validatorResultClass) {
        return new FieldValidationResult(field, validatorResultClass,true);
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

    public Class<? extends FieldValidator> getValidatorResultClass() {
        return validatorResultClass;
    }
}
