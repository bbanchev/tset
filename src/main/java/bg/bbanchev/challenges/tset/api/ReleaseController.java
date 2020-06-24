package bg.bbanchev.challenges.tset.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bg.bbanchev.challenges.tset.data.Service;
import bg.bbanchev.challenges.tset.dto.ServiceDeployment;
import bg.bbanchev.challenges.tset.service.ReleaseManager;

/**
 * Controller to serve API requests for the current release status.
 * 
 * @author Borislav Banchev
 *
 */
@RestController
@RequestMapping("/services")
public class ReleaseController {
	@Autowired
	private ReleaseManager relManager;

	/**
	 * Get the list of services for the requested release id.
	 * 
	 * @param systemVersion is the release id to list
	 * 
	 * @return the list of deployed services+version for that release
	 */
	//we might provide openapi documentation if needed as well
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public List<ServiceDeployment> getDeploymentStatus(
			@RequestParam(required = true, name = "systemVersion") Integer systemVersion) {
		return relManager.getDeploymentsByVersion(systemVersion).stream().map(this::serviceToServiceDeployment)
				.collect(Collectors.toList());
	}

	private ServiceDeployment serviceToServiceDeployment(Service service) {
		return new ServiceDeployment(service.getName(), service.getVersion());
	}
}
