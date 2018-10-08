package br.com.rnaufal.jcombiner.impl.domain;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by rnaufal
 */
public class MappedField {

    private final Field field;

    private final CombinationProperty combinationPropertyOnField;

    private final CombinationProperty combinationPropertyOnAnnotation;

    public static MappedField from(final Field field) {
        return new MappedField(field, getCombinationPropertyOnField(field), getCombinationPropertyOnAnnotation(field));
    }

    private MappedField(final Field field,
                        final CombinationProperty combinationPropertyOnField,
                        final CombinationProperty combinationPropertyOnAnnotation) {
        this.field = field;
        this.combinationPropertyOnField = combinationPropertyOnField;
        this.combinationPropertyOnAnnotation = combinationPropertyOnAnnotation;
    }

    public Field getField() {
        return field;
    }

    public String getTargetFieldName() {
        return getCombinationPropertyAnnotation()
                .map(this::getFieldName)
                .orElseThrow(() -> new InvalidCombinationFieldException(String.format("Mapped field %s is invalid",
                        field.getName())));
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(field.getType()) &&
                field.getGenericType() instanceof ParameterizedType;
    }

    public boolean matchesCombinationProperty() {
        return getCombinationPropertyAnnotation().isPresent();
    }

    public int getCombinationSize() {
        return getCombinationPropertyAnnotation()
                .map(CombinationProperty::size)
                .orElseThrow(() -> new InvalidCombinationFieldException(String.format("Mapped field %s is invalid",
                        field.getName())));
    }

    private String getFieldName(final CombinationProperty combinationProperty) {
        return combinationProperty.name().equals("") ? field.getName() : combinationProperty.name();
    }

    private Optional<CombinationProperty> getCombinationPropertyAnnotation() {
        return Optional
                .ofNullable(combinationPropertyOnField)
                .or(() -> Optional.ofNullable(combinationPropertyOnAnnotation));
    }

    private static CombinationProperty getCombinationPropertyOnAnnotation(final Field field) {
        return Arrays
                .stream(field.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(CombinationProperty.class))
                .findFirst()
                .map(annotation -> annotation.annotationType().getAnnotation(CombinationProperty.class))
                .orElse(null);
    }

    private static CombinationProperty getCombinationPropertyOnField(final Field field) {
        return field.getAnnotation(CombinationProperty.class);
    }
}
