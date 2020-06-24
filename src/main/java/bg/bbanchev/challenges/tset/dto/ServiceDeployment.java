package bg.bbanchev.challenges.tset.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import bg.bbanchev.challenges.tset.data.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO to represent the {@link Service} as Json.
 * 
 * @author Borislav Banchev
 *
 */
@JsonInclude(value = Include.NON_NULL)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDeployment {

	@JsonProperty
	private String name;
	@JsonProperty
	private Integer version;
	


}
