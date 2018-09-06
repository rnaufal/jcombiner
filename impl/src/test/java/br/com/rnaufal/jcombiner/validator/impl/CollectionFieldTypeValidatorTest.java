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
    void shouldReturnValidationErrorWhenFieldIsNotCollectionType() throws NoSuchFieldException {
        final var integerField = IntegerFieldClass.class.getField("integerField");

        final var fieldValidationResult = collectionFieldTypeValidator.validate(integerField, Object.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(false)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("integerField")));
    }

    @Test
    void shouldReturnValidationErrorWhenFieldHasNotParameterizedType() throws NoSuchFieldException {
        final var valuesField = InvalidCollectionClass.class.getField("values");

        final var fieldValidationResult = collectionFieldTypeValidator.validate(valuesField, Object.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(false)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("values")));
    }

    @Test
    void shouldDelegateToNextValidatorWhenFieldTypeIsCollection() throws NoSuchFieldException {
        final var stringsField = CollectionFieldClass.class.getField("strings");

        when(nextFieldValidator.validate(stringsField, Object.class)).thenReturn(FieldValidationResult.ok(stringsField,
                collectionFieldTypeValidator.getClass()));

        final var fieldValidationResult = collectionFieldTypeValidator.validate(stringsField, Object.class);

        assertThat(fieldValidationResult.isValid(), is(equalTo(true)));
        assertThat(fieldValidationResult.getField().getName(), is(equalTo("strings")));
    }

    private static class IntegerFieldClass {

        public Integer integerField;
    }

    private static class CollectionFieldClass {

        public Set<String> strings;
    }

    private static class InvalidCollectionClass {

        public Collection values;
    }
}