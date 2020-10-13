package au.com.deepak.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Embeddable
public class Address {

	@NotEmpty(message = "Street name is required")
	@Column(name = "street")
    private String street;
	
	@NotEmpty(message = "City name is required")
	@Column(name = "city")
    private String city;
	
	@NotEmpty(message = "State name is required")
	@Column(name = "state")
    private String state;
	
	@NotEmpty(message = "Postcode is required")
	@Column(name = "postcode")
    private String postcode;

    public Address() {}
}
