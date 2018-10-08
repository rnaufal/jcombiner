package br.com.rnaufal.jcombiner.impl.domain;

import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by rnaufal
 */
class MappedFieldTest {

    @Test
    void shouldThrowExceptionWhenGettingCombinationSizeFromInvalidMappedField() {
        final var integersField = getDeclaredField(InvalidCombinationClass.class, "integers", true);
        final var mappedField = MappedField.from(integersField);

        assertThrows(InvalidCombinationFieldException.class, mappedField::getCombinationSize);
    }

    @Test
    void shouldThrowExceptionWhenGettingTargetFieldNameFromInvalidMappedField() {
        final var integersField = getDeclaredField(InvalidCombinationClass.class, "integers", true);
        final var mappedField = MappedField.from(integersField);

        assertThrows(InvalidCombinationFieldException.class, mappedField::getTargetFieldName);
    }

    private static class InvalidCombinationClass {

        private List<Integer> integers;
    }
}