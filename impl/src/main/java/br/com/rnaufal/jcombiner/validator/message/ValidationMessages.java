package br.com.rnaufal.jcombiner.validator.message;

import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.impl.CollectionFieldTypeValidator;
import br.com.rnaufal.jcombiner.validator.impl.FieldExistsOnTargetClassValidator;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by rafael.naufal
 */
public enum ValidationMessages {

    COLLECTION_FIELD_TYPE_VALIDATOR(CollectionFieldTypeValidator.class,
            "Field [%s] is not assignable from Collection!"),

    FIELD_NOT_EXISTS_ON_TARGET_CLASS(FieldExistsOnTargetClassValidator.class,
            "Field [%s] not exists on target class [%s]!");

    private final Class<? extends FieldValidator> fieldValidatorClass;

    private final String errorMessage;

    private static final Map<Class<? extends FieldValidator>, String> messagesByValidatorClass =
            Arrays.stream(ValidationMessages.values())
                    .collect(Collectors.toUnmodifiableMap(validationMessages -> validationMessages.fieldValidatorClass,
                            validationMessages -> validationMessages.errorMessage));

    ValidationMessages(final Class<? extends FieldValidator> fieldValidatorClass,
                       final String errorMessage) {
        this.fieldValidatorClass = fieldValidatorClass;
        this.errorMessage = errorMessage;
    }

    public static Optional<String> getErrorMessage(final Class<? extends FieldValidator> fieldValidatorClass) {
        return Optional.ofNullable(messagesByValidatorClass.get(fieldValidatorClass));
    }
}
