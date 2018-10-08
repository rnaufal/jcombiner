package br.com.rnaufal.jcombiner.validator;

import br.com.rnaufal.jcombiner.impl.domain.MappedField;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by rnaufal
 */
public interface FieldValidator {

    <R> FieldValidationResult validate(MappedField mappedField, Class<R> targetClass);

    String getErrorMessage();

    default void registerTo(final ValidationMessages validationMessages) {
        validationMessages.add(this);
    }

    default <R> Field getTargetField(final MappedField mappedField,
                                     final Class<R> targetClass) {
        return FieldUtils.getDeclaredField(targetClass, mappedField.getTargetFieldName(), true);
    }
}
