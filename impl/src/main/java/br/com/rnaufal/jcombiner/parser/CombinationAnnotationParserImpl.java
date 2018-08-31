package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.annotation.CombinationsTarget;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import br.com.rnaufal.jcombiner.impl.domain.CombinationDescriptor;
import br.com.rnaufal.jcombiner.impl.domain.CombinationsDescriptor;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.impl.CollectionFieldTypeValidator;
import br.com.rnaufal.jcombiner.validator.impl.FieldExistsOnTargetClassValidator;
import br.com.rnaufal.jcombiner.validator.impl.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.message.ValidationMessages;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rafael.naufal
 */
public class CombinationAnnotationParserImpl implements CombinationAnnotationParser {

    private final Map<Class<?>, Optional<CombinationsDescriptor>> descriptorsByClass;

    private final FieldValidator validator;

    private final ValidationMessages validationMessages;

    public CombinationAnnotationParserImpl() {
        descriptorsByClass = Maps.newConcurrentMap();
        validator = new CollectionFieldTypeValidator(new FieldExistsOnTargetClassValidator());
        validationMessages = new ValidationMessages();
    }

    @Override
    public Optional<CombinationsDescriptor> parse(final Class<?> clazz) {
        Objects.requireNonNull(clazz, "Clazz should not be null");

        return descriptorsByClass.computeIfAbsent(clazz, this::buildDescriptor);
    }

    private Optional<CombinationsDescriptor> buildDescriptor(final Class<?> clazz) {
        final var combinationsAnnotation = clazz.getAnnotation(CombinationsTarget.class);

        if (combinationsAnnotation == null) {
            return Optional.empty();
        }

        final Class<?> targetClass = combinationsAnnotation.value();

        final var validatedFieldsByName = validateFields(clazz, targetClass);

        if (!hasCombinationProperties(validatedFieldsByName)) {
            throw new InvalidCombinationFieldException("Target result class [" + targetClass + "] has no fields mapped");
        }

        getErrorMessage(validatedFieldsByName)
                .ifPresent(errorMessage -> {
                    throw new InvalidCombinationFieldException(errorMessage);
                });

        return Optional.of(new CombinationsDescriptor(targetClass, buildDescriptorsByFieldName(validatedFieldsByName)));
    }

    private Map<String, CombinationDescriptor> buildDescriptorsByFieldName(final Map<Boolean, Map<String, FieldValidationResult>> validatedFieldsByName) {
        return getValidFields(validatedFieldsByName)
                .entrySet()
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toConcurrentMap(Map.Entry::getKey, entry -> {
                    final var combinationAnnotation = entry.getValue().getField().getAnnotation(CombinationProperty.class);
                    return new CombinationDescriptor(entry.getKey(), combinationAnnotation.size());
                }), Collections::unmodifiableMap));
    }

    private boolean hasCombinationProperties(final Map<Boolean, Map<String, FieldValidationResult>> validatedFields) {
        return validatedFields
                .values()
                .stream()
                .anyMatch(MapUtils::isNotEmpty);
    }

    private Map<String, FieldValidationResult> getValidFields(final Map<Boolean, Map<String, FieldValidationResult>> fields) {
        return fields.get(true);
    }

    private Optional<String> getErrorMessage(final Map<Boolean, Map<String, FieldValidationResult>> fields) {
        final var errorMessage = fields.get(false)
                .values()
                .stream()
                .map(validationResult -> validationMessages.getErrorMessage(validationResult.getValidatorResultClass()))
                .flatMap(Optional::stream)
                .reduce(StringUtils.EMPTY, (errorMsg, otherErrorMsg) -> errorMsg + "\n" + otherErrorMsg);

        return !StringUtils.equals(errorMessage, StringUtils.EMPTY) ?
                Optional.of(errorMessage) :
                Optional.empty();
    }

    private Map<Boolean, Map<String, FieldValidationResult>> validateFields(final Class<?> clazz,
                                                                            final Class<?> targetClass) {
        return Arrays
                .stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CombinationProperty.class))
                .map(field -> validator.validate(field, targetClass))
                .collect(Collectors.collectingAndThen(Collectors.partitioningBy(FieldValidationResult::isValid,
                        Collectors.toMap(fieldValidation -> fieldValidation.getField().getName(), Function.identity())),
                        Collections::unmodifiableMap));
    }
}
