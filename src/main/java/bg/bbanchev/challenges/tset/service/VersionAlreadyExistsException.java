package bg.bbanchev.challenges.tset.service;

/**
 * Indicates conflict with the request - same version of service already
 * deployed.
 * 
 * @author Borislav Banchev
 *
 */
public class VersionAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -2119240317324964260L;

	private int systemVersionNumber;

	public VersionAlreadyExistsException(int systemVersionNumber) {
		super(Integer.toString(systemVersionNumber));
		this.systemVersionNumber = systemVersionNumber;
	}

	public int getSystemVersionNumber() {
		return systemVersionNumber;
	}

}
