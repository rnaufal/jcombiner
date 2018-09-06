package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.annotation.CombinationClass;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasKey;
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
            List<String> strings;

            @CombinationProperty(size = 3)
            Collection<Integer> integers;

            class CombinationsTargetClass {
                Combinations<String> strings;

                Combinations<Integer> integers;
            }
        }

        final var optionalDescriptor = annotationParser.parse(CombinationsClass.class);
        assertThat(optionalDescriptor.isPresent(), is(equalTo(true)));

        final var descriptor = optionalDescriptor.get();
        assertThat(descriptor.getResultClass(), is(equalTo(CombinationsClass.CombinationsTargetClass.class)));
        assertThat(descriptor.getFieldDescriptorsByName(), is(notNullValue()));
        assertThat(descriptor.getFieldDescriptorsByName().values(), hasSize(2));
        assertThat(descriptor.getFieldDescriptorsByName(), hasKey("strings"));
        assertThat(descriptor.getFieldDescriptorsByName(), hasKey("integers"));
    }

    @Test
    void shouldThrowExceptionWhenTargetFieldTypesAreInvalid() {

        @CombinationClass(CombinationsClass.InvalidTargetFieldTypeCombinationsClass.class)
        class CombinationsClass {

            @CombinationProperty(size = 2)
            List<String> strings;

            @CombinationProperty(size = 3)
            Collection<Integer> integers;

            class InvalidTargetFieldTypeCombinationsClass {
                List<String> strings;

                List<Integer> integers;
            }
        }

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(CombinationsClass.class));
    }

    @Test
    void shouldThrowExceptionWhenTargetFieldTypesTypeParametersAreMissing() {

        @CombinationClass(CombinationsClass.TargetFieldTypeParametersMissingClass.class)
        class CombinationsClass {

            @CombinationProperty(size = 2)
            List<String> strings;

            @CombinationProperty(size = 3)
            Collection<Integer> integers;

            class TargetFieldTypeParametersMissingClass {
                Combinations strings;

                Combinations integers;
            }
        }

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(CombinationsClass.class));
    }

    @Test
    void shouldNotParseClassWithoutCombinationsAnnotation() {

        class CombinationsClass {

            @CombinationProperty(size = 2)
            List<String> strings;

            @CombinationProperty(size = 3)
            Collection<Integer> integers;
        }

        final var optionalDescriptor = annotationParser.parse(CombinationsClass.class);
        assertThat(optionalDescriptor.isPresent(), is(equalTo(false)));
    }

    @Test
    void shouldThrowExceptionWhenTargetClassHasNoCombinationFieldsMapped() {

        @CombinationClass(CombinationsClass.CombinationsTargetClass.class)
        class CombinationsClass {

            List<String> strings;

            Collection<Integer> integers;

            class CombinationsTargetClass {
            }
        }

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(CombinationsClass.class));
    }

    @Test
    void shouldThrowExceptionWhenFieldsAreInvalid() {

        @CombinationClass(InvalidCombinationsClass.CombinationsTargetClass.class)
        class InvalidCombinationsClass {

            @CombinationProperty(size = 3)
            String value;

            @CombinationProperty(size = 2)
            Integer sum;

            @CombinationProperty(size = 1)
            Collection<String> values;

            class CombinationsTargetClass {

                String value;

                Integer sum;

                Collection<String> values;
            }
        }

        assertThrows(InvalidCombinationFieldException.class, () -> annotationParser.parse(InvalidCombinationsClass.class));
    }
}