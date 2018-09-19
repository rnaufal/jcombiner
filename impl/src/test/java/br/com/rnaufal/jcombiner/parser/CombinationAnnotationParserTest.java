package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.api.annotation.CombinationClass;
import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import org.junit.jupiter.api.BeforeEach;
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

    private CombinationAnnotationParser annotationParser;

    @BeforeEach
    void setUp() {
        annotationParser = new CombinationAnnotationParserImpl();
    }

    @Test
    void shouldParseClassWithCombinationsAnnotationSuccessfully() {

        @CombinationClass(CombinationsClass.CombinationsTargetClass.class)
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

        final var optionalDescriptor = annotationParser.parse(new CombinationsClass());
        assertThat(optionalDescriptor.isPresent(), is(equalTo(true)));

        final var descriptor = optionalDescriptor.get();
        assertThat(descriptor.getResultClass(), is(equalTo(CombinationsClass.CombinationsTargetClass.class)));
        assertThat(descriptor.getCombinationFields(), is(notNullValue()));
        assertThat(descriptor.getCombinationFields(), hasSize(2));

        final var firstCombinationField = descriptor.getCombinationFields().get(0);
        final var secondCombinationField = descriptor.getCombinationFields().get(1);

        assertThat(firstCombinationField.getSourceField().getName(), is(equalTo("strings")));
        assertThat(firstCombinationField.getTargetField().getName(), is(equalTo("strings")));
        assertThat(firstCombinationField.getCombinationSize(), is(equalTo(2)));

        assertThat(secondCombinationField.getSourceField().getName(), is(equalTo("integers")));
        assertThat(secondCombinationField.getTargetField().getName(), is(equalTo("integers")));
        assertThat(secondCombinationField.getCombinationSize(), is(equalTo(3)));
    }

    @Test
    void shouldThrowExceptionWhenTargetFieldTypesAreInvalid() {

        @CombinationClass(CombinationsClass.InvalidTargetFieldTypeCombinationsClass.class)
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

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(new CombinationsClass()));
    }

    @Test
    void shouldThrowExceptionWhenTargetFieldTypesTypeParametersAreMissing() {

        @CombinationClass(CombinationsClass.TargetFieldTypeParametersMissingClass.class)
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

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(new CombinationsClass()));
    }

    @Test
    void shouldNotParseClassWithoutCombinationsAnnotation() {

        class CombinationsClass {

            @CombinationProperty(size = 2)
            private List<String> strings;

            @CombinationProperty(size = 3)
            private Collection<Integer> integers;
        }

        final var optionalDescriptor = annotationParser.parse(new CombinationsClass());
        assertThat(optionalDescriptor.isPresent(), is(equalTo(false)));
    }

    @Test
    void shouldThrowExceptionWhenTargetClassHasNoCombinationFieldsMapped() {

        @CombinationClass(CombinationsClass.CombinationsTargetClass.class)
        class CombinationsClass {

            private List<String> strings;

            private Collection<Integer> integers;

            class CombinationsTargetClass {
            }
        }

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(new CombinationsClass()));
    }

    @Test
    void shouldThrowExceptionWhenFieldsAreInvalid() {

        @CombinationClass(InvalidCombinationsClass.CombinationsTargetClass.class)
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

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(new InvalidCombinationsClass()));
    }
}