package br.com.rnaufal.jcombiner.validator;

import br.com.rnaufal.jcombiner.validator.impl.FieldValidationResult;

import java.lang.reflect.Field;

/**
 * Created by rafael.naufal
 */
public interface FieldValidator {

    FieldValidationResult validate(Field field, Class<?> targetClass);
}
