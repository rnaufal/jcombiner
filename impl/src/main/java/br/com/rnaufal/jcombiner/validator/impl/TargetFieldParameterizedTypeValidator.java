package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.impl.domain.MappedField;
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
    public <R> FieldValidationResult validate(final MappedField mappedField,
                                              final Class<R> targetClass) {
        final var targetField = getTargetField(mappedField, targetClass);

        return getActualTypeArgument(mappedField.getField())
                .map(sourceFieldTypeArgument -> compareTypeArguments(mappedField, targetField, sourceFieldTypeArgument))
                .orElseGet(() -> FieldValidationResult.error(mappedField, getClass()));
    }

    private FieldValidationResult compareTypeArguments(final MappedField mappedField,
                                                       final Field targetField,
                                                       final String sourceFieldTypeArgument) {
        final var maybeActualTypeArgument = getActualTypeArgument(targetField);

        return maybeActualTypeArgument
                .filter(targetFieldTypeArgument -> StringUtils.equals(targetFieldTypeArgument, sourceFieldTypeArgument))
                .map(targetFieldTypeArgument -> FieldValidationResult.ok(mappedField, targetField, getClass()))
                .orElseGet(() -> FieldValidationResult.error(mappedField, getClass()));
    }

    private Optional<String> getActualTypeArgument(final Field field) {
        final var genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return Optional.empty();
        }
        final var parameterizedType = (ParameterizedType) genericType;
        return Optional.of(parameterizedType.getActualTypeArguments()[0].getTypeName());
    }

    @Override
    public String getErrorMessage() {
        return FIELD_PARAMETERIZED_TYPE_ON_TARGET_CLASS_ERROR;
    }
}
