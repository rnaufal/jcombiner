package br.com.rnaufal.jcombiner.exception;

/**
 * Created by rnaufal
 */
public class InvalidCombinationFieldException extends RuntimeException {

    public InvalidCombinationFieldException(final String message) {
        super(message);
    }

    public InvalidCombinationFieldException(final String message,
                                            final Throwable throwable) {
        super(message, throwable);
    }
}
