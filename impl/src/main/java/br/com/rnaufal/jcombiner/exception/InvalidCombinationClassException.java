package br.com.rnaufal.jcombiner.exception;

/**
 * Created by rnaufal
 */
public class InvalidCombinationClassException extends RuntimeException {

    public InvalidCombinationClassException(final String message) {
        super(message);
    }

    public InvalidCombinationClassException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
