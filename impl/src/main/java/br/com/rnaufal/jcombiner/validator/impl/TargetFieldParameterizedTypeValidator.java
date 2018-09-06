package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * Created by rnaufal
 */
public class TargetFieldParameterizedTypeValidator implements FieldValidator {

    private static final String FIELD_PARAMETERIZED_TYPE_ON_TARGET_CLASS_ERROR = "Field [%s] has different parameterized type on target class!";

    @Override
    public FieldValidationResult validate(final Field field,
                                          final Class<?> targetClass) {
        final var targetField = getTargetField(field, targetClass);

        return getActualTypeArgument(field)
                .map(sourceFieldTypeArgument -> compareTypeArguments(field, targetField, sourceFieldTypeArgument))
                .orElseGet(() -> FieldValidationResult.error(field, getClass()));
    }

    private FieldValidationResult compareTypeArguments(final Field field,
                                                       final Field targetField,
                                                       final String sourceFieldTypeArgument) {
        final var maybeActualTypeArgument = getActualTypeArgument(targetField);

        return maybeActualTypeArgument
                .filter(targetFieldTypeArgument -> StringUtils.equals(targetFieldTypeArgument, sourceFieldTypeArgument))
                .map(targetFieldTypeArgument -> FieldValidationResult.ok(field, getClass()))
                .orElseGet(() -> FieldValidationResult.error(field, getClass()));
    }

    private Optional<String> getActualTypeArgument(final Field field) {
        final var genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            final var parameterizedType = (ParameterizedType) genericType;
            return Optional.of(parameterizedType.getActualTypeArguments()[0].getTypeName());
        }
        return Optional.empty();
    }

    @Override
    public String getErrorMessage() {
        return FIELD_PARAMETERIZED_TYPE_ON_TARGET_CLASS_ERROR;
    }
}
