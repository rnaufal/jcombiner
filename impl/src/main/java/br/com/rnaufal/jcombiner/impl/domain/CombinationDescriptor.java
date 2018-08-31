package br.com.rnaufal.jcombiner.impl.domain;

/**
 * Created by rafael.naufal
 */
public class CombinationDescriptor {

    private final String targetFieldName;

    private final int size;

    public CombinationDescriptor(final String targetFieldName,
                                 final int size) {
        this.targetFieldName = targetFieldName;
        this.size = size;
    }
}
