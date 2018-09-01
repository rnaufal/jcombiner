package br.com.rnaufal.jcombiner.validator;

import br.com.rnaufal.jcombiner.validator.impl.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.message.ValidationMessages;

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
}
