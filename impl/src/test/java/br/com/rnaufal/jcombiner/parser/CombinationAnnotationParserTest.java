package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by rnaufal
 */
class CombinationAnnotationParserTest {

    @Test
    void shouldParseClassWithCombinationsAnnotationSuccessfully() {

        class CombinationsClass {

            @CombinationProperty(size = 2)
            private List<String> strings;

            @CombinationProperty(size = 3)
            private Collection<Integer> integers;

            class CombinationsTargetClass {
                private Combinations<String> strings;

                private Combinations<Integer> integers;
            }
        }

        final var annotationParser = new CombinationAnnotationParserImpl<CombinationsClass.CombinationsTargetClass>();

        final var combinationClass = annotationParser.parse(CombinationsClass.class, CombinationsClass.CombinationsTargetClass.class);
        assertThat(combinationClass, is(notNullValue()));

        assertThat(combinationClass.getResultClass(), is(equalTo(CombinationsClass.CombinationsTargetClass.class)));
        assertThat(combinationClass.getCombinationFields(), is(notNullValue()));
        assertThat(combinationClass.getCombinationFields(), hasSize(2));

        final var firstCombinationField = combinationClass.getCombinationFields().get(0);
        final var secondCombinationField = combinationClass.getCombinationFields().get(1);

        assertThat(firstCombinationField.getSourceField().getName(), is(equalTo("strings")));
        assertThat(firstCombinationField.getTargetField().getName(), is(equalTo("strings")));
        assertThat(firstCombinationField.getCombinationSize(), is(equalTo(2)));

        assertThat(secondCombinationField.getSourceField().getName(), is(equalTo("integers")));
        assertThat(secondCombinationField.getTargetField().getName(), is(equalTo("integers")));
        assertThat(secondCombinationField.getCombinationSize(), is(equalTo(3)));
    }

    @Test
    void shouldThrowExceptionWhenTargetFieldTypesAreInvalid() {

        class CombinationsClass {

            @CombinationProperty(size = 2)
            private List<String> strings;

            @CombinationProperty(size = 3)
            private Collection<Integer> integers;

            class InvalidTargetFieldTypeCombinationsClass {
                private List<String> strings;

                private List<Integer> integers;
            }
        }

        final var annotationParser = new CombinationAnnotationParserImpl<CombinationsClass.InvalidTargetFieldTypeCombinationsClass>();

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(CombinationsClass.class,
                CombinationsClass.InvalidTargetFieldTypeCombinationsClass.class));
    }

    @Test
    void shouldThrowExceptionWhenTargetFieldTypesTypeParametersAreMissing() {

        class CombinationsClass {

            @CombinationProperty(size = 2)
            private List<String> strings;

            @CombinationProperty(size = 3)
            private Collection<Integer> integers;

            class TargetFieldTypeParametersMissingClass {
                private Combinations strings;

                private Combinations integers;
            }
        }

        final var annotationParser = new CombinationAnnotationParserImpl<CombinationsClass.TargetFieldTypeParametersMissingClass>();

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(CombinationsClass.class,
                CombinationsClass.TargetFieldTypeParametersMissingClass.class));
    }

    @Test
    void shouldThrowExceptionWhenTargetClassHasNoCombinationFieldsMapped() {

        class CombinationsClass {

            private List<String> strings;

            private Collection<Integer> integers;

            class CombinationsTargetClass {
            }
        }

        final var annotationParser = new CombinationAnnotationParserImpl<CombinationsClass.CombinationsTargetClass>();

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(CombinationsClass.class,
                CombinationsClass.CombinationsTargetClass.class));
    }

    @Test
    void shouldThrowExceptionWhenFieldsAreInvalid() {

        class InvalidCombinationsClass {

            @CombinationProperty(size = 3)
            private String value;

            @CombinationProperty(size = 2)
            private Integer sum;

            @CombinationProperty(size = 1)
            private Collection<String> values;

            class CombinationsTargetClass {

                private String value;

                private Integer sum;

                private Collection<String> values;
            }
        }

        final var annotationParser = new CombinationAnnotationParserImpl<InvalidCombinationsClass.CombinationsTargetClass>();

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(InvalidCombinationsClass.class,
                InvalidCombinationsClass.CombinationsTargetClass.class));
    }
}