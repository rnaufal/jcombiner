package br.com.rnaufal.jcombiner.validator.message;

import br.com.rnaufal.jcombiner.validator.FieldValidator;
import br.com.rnaufal.jcombiner.validator.impl.CollectionFieldTypeValidator;
import br.com.rnaufal.jcombiner.validator.impl.FieldExistsOnTargetClassValidator;

import java.util.Map;
import java.util.Optional;

/**
 * Created by rafael.naufal
 */
public class ValidationMessages {

    private final Map<Class<? extends FieldValidator>, String> messagesByFieldValidatorClass;

    public ValidationMessages() {
        messagesByFieldValidatorClass = Map.of(CollectionFieldTypeValidator.class,
                "Field [%s] is not assignable from Collection!",
                FieldExistsOnTargetClassValidator.class, "Field [%s] not exists on target class [%s]!");
    }

    public Optional<String> getErrorMessage(final Class<? extends FieldValidator> fieldValidatorClass) {
        return Optional.ofNullable(messagesByFieldValidatorClass.get(fieldValidatorClass));
    }
}
