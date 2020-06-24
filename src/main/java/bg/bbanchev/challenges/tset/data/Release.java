package bg.bbanchev.challenges.tset.data;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

/**
 * Represents a release - systemVersionNumber + list of services.
 * 
 * @author Borislav Banchev
 *
 */
@Getter
@Document(collection = "releases")
public class Release {

	@Id
	private String id;

	private Integer systemVersionNumber;

	private List<Service> services;

	public Release(Integer systemVersionNumber, List<Service> services) {
		this.systemVersionNumber = systemVersionNumber;
		this.services = services;
	}
	
}