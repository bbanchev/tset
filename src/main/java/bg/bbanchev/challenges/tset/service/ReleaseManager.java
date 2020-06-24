package bg.bbanchev.challenges.tset.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import bg.bbanchev.challenges.tset.data.Release;
import bg.bbanchev.challenges.tset.data.ReleaseRepository;
import bg.bbanchev.challenges.tset.data.Service;

/**
 * The {@link ReleaseManager} manages the release requests. The service may
 * create a new release and may query in the existing releases for specific
 * version.
 * 
 * @author Borislav Banchev
 *
 */
@org.springframework.stereotype.Service
public class ReleaseManager {
	private static Lock lock = new ReentrantLock();

	@Autowired
	private ReleaseRepository repo;

	/**
	 * Adds/Updates the service deployment to the latest release.
	 * 
	 * 
	 * @param deployment is the service to be released. Required argument.
	 * 
	 * @return the release version that contains the provided service deployment.
	 *
	 * @throws IllegalArgumentException             in case the deployment parameter
	 *                                              is invalid
	 * @throws VersionAlreadyExistsException        if the service is already
	 *                                              deployed with that version
	 * @throws NewerVersionAlreadyReleasedException if the deployment has lower
	 *                                              version than the latest release.
	 */
	public Integer releaseService(Service deployment) {
		validate(deployment);
		try {
			// Prevent dirty read for concurrent deployments requests by allowing only one
			// instance at a time. Other possible solutions is to use queue for request processing
			// (we should insure not losing a request in case release manager server goes
			// down) or use persistent message broker to store requests. In both cases we
			// will need websocket to notify the api client.
			lock.lock();
			// query for the latest release - we might use the MongoTemplate as well with custom repository
			Page<Release> latestReleases = repo.findAll(PageRequest.of(0, 1, Sort.by(Order.desc("_id"))));

			// find latest release or create a new empty one as initial
			Release latestRelease = latestReleases.get().findFirst().orElse(new Release(0, new ArrayList<>(1)));

			// check if our service is part of the latest release
			Optional<Service> latestServiceRel = latestRelease.getServices().stream()
					.filter(s -> deployment.getName().equals(s.getName())).findFirst();

			

			// if release version is the same as the current deploy
			if (latestServiceRel.isPresent() && latestServiceRel.get().getVersion() == deployment.getVersion()) {
				// already deployed latest version
				throw new VersionAlreadyExistsException(latestRelease.getSystemVersionNumber());
			}
			// otherwise update the current release - either update the service in the list,
			// or append a new service at the end
			List<Service> services = updateReleasetServicesList(deployment, latestRelease, latestServiceRel);

			// create and save the new release
			Release newRelease = repo.save(new Release(latestRelease.getSystemVersionNumber() + 1, services));
			return newRelease.getSystemVersionNumber();
		} finally {
			lock.unlock();
		}
	}

	// validate the request
	private void validate(Service deployment) {
		if (deployment == null || deployment.getVersion() == null || deployment.getName() == null) {
			throw new IllegalArgumentException("Invalid service deployment information : " + deployment);
		}
	}

	// update the current snapshost of released services
	private ArrayList<Service> updateReleasetServicesList(Service deployment, Release latestRelease,
			Optional<Service> latestServiceRel) {
		ArrayList<Service> services = new ArrayList<>(latestRelease.getServices());
		if (latestServiceRel.isPresent()) {
			if (latestServiceRel.get().getVersion() > deployment.getVersion()) {
				throw new NewerVersionAlreadyReleasedException(
						"Newer version [" + latestServiceRel.get().getVersion() + "] already deployed!");
			}
			int index = services.indexOf(latestServiceRel.get());
			services.set(index, new Service(deployment.getName(), deployment.getVersion()));
		} else {
			services.add(new Service(deployment.getName(), deployment.getVersion()));
		}
		return services;
	}

	/**
	 * List all services for the specified version.
	 * 
	 * @param systemVersion the system version to retrieve
	 * @return the list of services for that version or empty list if version is not
	 *         found.
	 */
	public List<Service> getDeploymentsByVersion(Integer systemVersion) {
		Release findBySystemVersionNumber = repo.findBySystemVersionNumber(systemVersion);
		if (findBySystemVersionNumber != null) {
			return findBySystemVersionNumber.getServices();
		}
		return Collections.emptyList();
	}

}
