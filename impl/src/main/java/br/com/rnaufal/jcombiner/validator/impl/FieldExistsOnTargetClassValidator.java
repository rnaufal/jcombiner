package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.message.ValidationMessages;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
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
    public FieldValidationResult validate(final Field field,
                                          final Class<?> targetClass) {
        if (existsFieldOnTargetClass(targetClass, field)) {
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
        return FIELD_NOT_EXISTS_ON_TARGET_CLASS_ERROR;
    }

    private boolean existsFieldOnTargetClass(final Class<?> targetClass,
                                             final Field field) {
        return FieldUtils.getDeclaredField(targetClass, getTargetFieldName(field), true) != null;
    }

    private String getTargetFieldName(final Field field) {
        final var combinationAnnotation = field.getAnnotation(CombinationProperty.class);

        return combinationAnnotation.name().equals("") ? field.getName() : combinationAnnotation.name();
    }
}
