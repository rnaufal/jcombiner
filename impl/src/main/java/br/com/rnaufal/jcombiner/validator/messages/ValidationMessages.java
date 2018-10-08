package br.com.rnaufal.jcombiner.validator.messages;

import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * Created by rnaufal
 */
public class ValidationMessages {

    private final Map<Class<? extends FieldValidator>, String> messagesByFieldValidatorClass;

    public ValidationMessages() {
        messagesByFieldValidatorClass = Maps.newConcurrentMap();
    }

    public void add(final FieldValidator fieldValidator) {
        messagesByFieldValidatorClass.putIfAbsent(fieldValidator.getClass(), fieldValidator.getErrorMessage());
    }

    public Optional<String> getErrorMessage(final FieldValidationResult validationResult) {
        return Optional.ofNullable(messagesByFieldValidatorClass.get(validationResult.getValidatorResultClass()))
                       .map(validationResult::formatErrorMessage);
    }
}
