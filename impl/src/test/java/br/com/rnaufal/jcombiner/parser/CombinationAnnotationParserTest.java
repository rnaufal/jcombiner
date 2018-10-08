package br.com.rnaufal.jcombiner.parser;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;
import br.com.rnaufal.jcombiner.api.domain.Combinations;
import br.com.rnaufal.jcombiner.exception.InvalidCombinationFieldException;
import br.com.rnaufal.jcombiner.impl.domain.CombinationClass;
import br.com.rnaufal.jcombiner.impl.domain.CombinationField;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

        assertCombinationClass(combinationClass, CombinationsClass.CombinationsTargetClass.class, 2);
        assertCombinationField(combinationClass.getCombinationFields().get(0), "strings", "strings", 2);
        assertCombinationField(combinationClass.getCombinationFields().get(1), "integers", "integers", 3);
    }

    @Test
    void shouldParseClassWithCustomCombinationPropertyAnnotation() {

        class ValidCombinationClass {

            @CombinationProperty(size = 4, name = "someStrings")
            private List<String> strings;

            @ThreeSizeCombinationProperty
            private Collection<Integer> integers;

            @ThreeSizeCombinationProperty
            private Collection<Float> values;

            class ValidCombinationsTargetClass {
                private Combinations<String> someStrings;

                private Combinations<Integer> integers;

                private Combinations<Float> values;
            }
        }

        final var annotationParser = new CombinationAnnotationParserImpl<ValidCombinationClass.ValidCombinationsTargetClass>();

        final var combinationClass = annotationParser.parse(ValidCombinationClass.class, ValidCombinationClass.ValidCombinationsTargetClass.class);

        assertCombinationClass(combinationClass, ValidCombinationClass.ValidCombinationsTargetClass.class, 3);
        assertCombinationField(combinationClass.getCombinationFields().get(0), "strings", "someStrings", 4);
        assertCombinationField(combinationClass.getCombinationFields().get(1), "integers", "integers", 3);
        assertCombinationField(combinationClass.getCombinationFields().get(2), "values", "values", 3);
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
    void shouldThrowExceptionWhenSourceClassHasNoCombinationFieldsMapped() {

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
    void shouldThrowExceptionWhenTargetClassHasNoFieldsMapped() {

        class CombinationsClass {

            @StringsTwoSizeCombinationProperty
            private List<Float> numbers;

            @StringsTwoSizeCombinationProperty
            private Collection<Integer> integers;

            @CombinationProperty(size = 4)
            private Collection<String> strings;

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

    private void assertCombinationField(final CombinationField combinationField, final String sourceFieldName,
                                        final String targetFieldName,
                                        final int combinationSize) {
        assertThat(combinationField.getSourceField().getName(), is(equalTo(sourceFieldName)));
        assertThat(combinationField.getTargetField().getName(), is(equalTo(targetFieldName)));
        assertThat(combinationField.getCombinationSize(), is(equalTo(combinationSize)));
    }

    private void assertCombinationClass(final CombinationClass<?> combinationClass,
                                        final Class<?> resultClass,
                                        final int combinationFieldsQuantity) {
        assertThat(combinationClass, is(notNullValue()));
        assertThat(combinationClass.getResultClass(), is(equalTo(resultClass)));
        assertThat(combinationClass.getCombinationFields(), is(notNullValue()));
        assertThat(combinationClass.getCombinationFields(), hasSize(combinationFieldsQuantity));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @CombinationProperty(size = 3)
    private @interface ThreeSizeCombinationProperty {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @CombinationProperty(size = 2, name = "strings")
    private @interface StringsTwoSizeCombinationProperty {

    }
}