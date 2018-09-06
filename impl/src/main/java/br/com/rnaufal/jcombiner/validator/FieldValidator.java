package br.com.rnaufal.jcombiner.validator;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by rnaufal
 */
public interface FieldValidator {

    FieldValidationResult validate(Field field, Class<?> targetClass);

    String getErrorMessage();

    default void registerTo(final ValidationMessages validationMessages) {
        validationMessages.add(this);
    }

    default Field getTargetField(final Field field, final Class<?> targetClass) {
        final var combinationAnnotation = field.getAnnotation(CombinationProperty.class);

        final var targetFieldName = combinationAnnotation.name().equals("") ?
                field.getName() : combinationAnnotation.name();

        return FieldUtils.getDeclaredField(targetClass, targetFieldName, true);
    }
}
