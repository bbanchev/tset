package bg.bbanchev.challenges.tset.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bg.bbanchev.challenges.tset.data.Service;
import bg.bbanchev.challenges.tset.dto.ServiceDeployment;
import bg.bbanchev.challenges.tset.service.ReleaseManager;

/**
 * Controller to serve API requests to deploy/redeploy service as part of the
 * current release.
 * 
 * @author Borislav Banchev
 *
 */
@RestController
@RequestMapping("/deploy")
public class DeployController {
	@Autowired
	private ReleaseManager relManager;

	/**
	 * Deploy the request service.

	 * @param service is the service to deploy
	 * 
	 * @return the latest release system version. 
	 */
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String deployService(@RequestBody ServiceDeployment service) {
		return relManager.releaseService(new Service(service.getName(), service.getVersion())).toString();
	}
}
