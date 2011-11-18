package glmnet;

/**
 * Error occurrent in GLMNet learning routines.
 *
 * @author Thomas Down
 */

public class LearnerException extends RuntimeException {
    public LearnerException(int err) {
	super(String.format("Error in GLMNet (err=%d)", err));
    }
}