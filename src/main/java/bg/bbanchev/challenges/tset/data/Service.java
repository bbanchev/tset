package bg.bbanchev.challenges.tset.data;

import java.util.Objects;

import lombok.Getter;

/**
 * Represents a service in a release - service name and version
 * 
 * @author Borislav Banchev
 *
 */
@Getter
public class Service {
	private String name;
	private Integer version;

	public Service(String name, Integer version) {
		this.name = name;
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		return Objects.equals(name, other.name) && Objects.equals(version, other.version);
	}

}
