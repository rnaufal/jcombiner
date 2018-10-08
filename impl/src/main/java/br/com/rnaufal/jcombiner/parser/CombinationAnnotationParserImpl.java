package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import br.com.rnaufal.jcombiner.impl.domain.CombinationClass;
import br.com.rnaufal.jcombiner.impl.domain.CombinationField;
import br.com.rnaufal.jcombiner.impl.domain.MappedField;
import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.impl.CollectionFieldTypeValidator;
import br.com.rnaufal.jcombiner.validator.impl.CombinationsTargetFieldTypeValidator;
import br.com.rnaufal.jcombiner.validator.impl.FieldExistsOnTargetClassValidator;
import br.com.rnaufal.jcombiner.validator.impl.TargetFieldParameterizedTypeValidator;
import br.com.rnaufal.jcombiner.validator.messages.ValidationMessages;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rnaufal
 */
public class CombinationAnnotationParserImpl<R> implements CombinationAnnotationParser<R> {

    private final Map<Class<?>, CombinationClass<R>> descriptorsByClass;

    private final FieldValidator validator;

    private final ValidationMessages validationMessages;

    public CombinationAnnotationParserImpl() {
        descriptorsByClass = Maps.newConcurrentMap();
        validator = new CollectionFieldTypeValidator(new FieldExistsOnTargetClassValidator(
                new CombinationsTargetFieldTypeValidator(new TargetFieldParameterizedTypeValidator())));
        validationMessages = new ValidationMessages();
        validator.registerTo(validationMessages);
    }

    @Override
    public <T> CombinationClass<R> parse(final Class<T> sourceClass,
                                         final Class<R> targetClass) {
        Objects.requireNonNull(sourceClass, "sourceClass cannot be null");

        return descriptorsByClass.computeIfAbsent(sourceClass, clazz -> buildDescriptor(clazz, targetClass));
    }

    private CombinationClass<R> buildDescriptor(final Class<?> sourceClass,
                                                final Class<R> targetClass) {
        final var validatedFieldsByName = validateFields(sourceClass, targetClass);

        if (!hasCombinationProperties(validatedFieldsByName)) {
            throw new InvalidCombinationFieldException(String.format("Target result class [%s] has no Combination properties",
                    targetClass.getSimpleName()));
        }

        getErrorMessage(validatedFieldsByName)
                .ifPresent(errorMessage -> {
                    throw new InvalidCombinationFieldException(errorMessage);
                });

        return new CombinationClass<>(targetClass, buildCombinationFields(validatedFieldsByName));
    }

    private List<CombinationField> buildCombinationFields(final Map<Boolean, List<FieldValidationResult>> validatedFieldsByName) {
        return getValidFields(validatedFieldsByName)
                .stream()
                .map(this::buildCombinationField)
                .flatMap(Optional::stream)
                .collect(Collectors.toUnmodifiableList());
    }

    private Optional<CombinationField> buildCombinationField(final FieldValidationResult fieldValidationResult) {
        return fieldValidationResult
                .getTargetField()
                .map(targetField -> new CombinationField(fieldValidationResult.getMappedField().getField(),
                        targetField,
                        fieldValidationResult.getMappedField().getCombinationSize()));
    }

    private boolean hasCombinationProperties(final Map<Boolean, List<FieldValidationResult>> validatedFields) {
        return validatedFields
                .values()
                .stream()
                .anyMatch(CollectionUtils::isNotEmpty);
    }

    private List<FieldValidationResult> getValidFields(final Map<Boolean, List<FieldValidationResult>> fields) {
        return fields.get(true);
    }

    private Optional<String> getErrorMessage(final Map<Boolean, List<FieldValidationResult>> fields) {
        final var errorMessage = fields.get(false)
                .stream()
                .map(validationMessages::getErrorMessage)
                .flatMap(Optional::stream)
                .reduce(StringUtils.EMPTY, (errorMsg, otherErrorMsg) -> errorMsg + "\n" + otherErrorMsg);

        return StringUtils.equals(errorMessage, StringUtils.EMPTY) ? Optional.empty() : Optional.of(errorMessage);
    }

    private Map<Boolean, List<FieldValidationResult>> validateFields(final Class<?> clazz,
                                                                     final Class<R> targetClass) {
        return Arrays
                .stream(clazz.getDeclaredFields())
                .map(MappedField::from)
                .filter(MappedField::matchesCombinationProperty)
                .map(field -> validator.validate(field, targetClass))
                .collect(Collectors.partitioningBy(FieldValidationResult::isValid, Collectors.toUnmodifiableList()));
    }
}
