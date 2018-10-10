package br.com.rnaufal.jcombiner.client.annotation;

import br.com.rnaufal.jcombiner.api.annotation.CombinationProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnaufal
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CombinationProperty(size = 2)
public @interface TwoSizeCombination {
}
