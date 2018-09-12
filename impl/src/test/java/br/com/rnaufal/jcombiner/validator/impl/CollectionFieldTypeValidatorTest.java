package br.com.rnaufal.jcombiner.validator.impl;

import br.com.rnaufal.jcombiner.validator.FieldValidationResult;
import br.com.rnaufal.jcombiner.validator.FieldValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Set;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by rnaufal
 */
@ExtendWith(MockitoExtension.class)
class CollectionFieldTypeValidatorTest {

    private CollectionFieldTypeValidator collectionFieldTypeValidator;

    @Mock
    private FieldValidator nextFieldValidator;

    @BeforeEach
    void setUp() {
        collectionFieldTypeValidator = new CollectionFieldTypeValidator(nextFieldValidator);
    }

    @Test
    void shouldReturnValidationErrorWhenFieldIsNotCollectionType() {
        final var integerField = getDeclaredField(IntegerFieldClass.class, "integerField", true);

        final var fieldValidationResult = collectionFieldTypeValidator.validate(integerField, Object.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(false)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("integerField")));
    }

    @Test
    void shouldReturnValidationErrorWhenFieldHasNotParameterizedType() {
        final var valuesField = getDeclaredField(InvalidCollectionClass.class, "values", true);

        final var fieldValidationResult = collectionFieldTypeValidator.validate(valuesField, Object.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(false)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("values")));
    }

    @Test
    void shouldDelegateToNextValidatorWhenFieldTypeIsCollection() {
        final var stringsField = getDeclaredField(CollectionFieldClass.class, "strings", true);

        when(nextFieldValidator.validate(stringsField, Object.class))
                .thenReturn(FieldValidationResult.ok(stringsField, stringsField, collectionFieldTypeValidator.getClass()));

        final var fieldValidationResult = collectionFieldTypeValidator.validate(stringsField, Object.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(true)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("strings")));
    }

    private static class IntegerFieldClass {

        private Integer integerField;
    }

    private static class CollectionFieldClass {

        private Set<String> strings;
    }

    private static class InvalidCollectionClass {

        private Collection values;
    }
}