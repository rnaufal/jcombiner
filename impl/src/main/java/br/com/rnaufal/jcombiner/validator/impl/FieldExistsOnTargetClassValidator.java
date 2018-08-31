package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by rafael.naufal
 */
public class FieldExistsOnTargetClassValidator implements FieldValidator {

    @Override
    public FieldValidationResult validate(final Field field,
                                          final Class<?> targetClass) {
        final Class<? extends FieldExistsOnTargetClassValidator> validatorResultClass = getClass();

        if (existsFieldOnTargetClass(targetClass, field)) {
            return FieldValidationResult.ok(field, validatorResultClass);
        } else {
            return FieldValidationResult.error(field, validatorResultClass);
        }
    }

    private boolean existsFieldOnTargetClass(final Class<?> targetClass,
                                             final Field field) {
        return FieldUtils.getDeclaredField(targetClass, getTargetFieldName(field), true) != null;
    }

    private String getTargetFieldName(final Field field) {
        final var combinationAnnotation = field.getAnnotation(CombinationProperty.class);

        return combinationAnnotation.targetField().equals("") ? field.getName() : combinationAnnotation.targetField();
    }
}
