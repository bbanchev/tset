package bg.bbanchev.challenges.tset.service;

/**
 * Indicates conflict with the request - newer version of service already
 * deployed.
 * 
 * @author Borislav Banchev
 *
 */
public class NewerVersionAlreadyReleasedException extends RuntimeException {

	private static final long serialVersionUID = -6839788296678173836L;

	public NewerVersionAlreadyReleasedException(String message) {
		super(message);
	}

}
